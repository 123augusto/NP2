import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import pagamento.*;
import modelo.palestra.*;
import modelo.pessoa.*;

public class TestFluxo {
    static String java, classes;
    static int passed;
    static void check(boolean ok, String why) { if (!ok) throw new AssertionError(why); }
    static int count(String text, String needle) {
        int n = 0, pos = 0;
        while ((pos = text.indexOf(needle, pos)) >= 0) { n++; pos += needle.length(); }
        return n;
    }
    static String run(String input, Path dir) throws Exception {
        Process p = start(dir);
        p.getOutputStream().write(input.getBytes(StandardCharsets.UTF_8));
        p.getOutputStream().close();
        check(p.waitFor(15, TimeUnit.SECONDS), "timeout");
        check(p.exitValue() == 0, "processo falhou");
        return Files.readString(dir.resolve("saida.txt"));
    }
    static Process start(Path dir) throws Exception {
        return new ProcessBuilder(java, "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8",
                "-cp", classes, "main.Main")
                .directory(dir.toFile()).redirectErrorStream(true)
                .redirectOutput(dir.resolve("saida.txt").toFile()).start();
    }
    public static void main(String[] args) throws Exception {
        java = args[0]; classes = args[1];
        Path dir = Files.createTempDirectory("fluxo-campos-");
        String input = String.join("\n",
            "1", "", "Ana", "abc", "01234567890", "x", "-1", "20", "9", "3", "2",
            "x", "8", "1", "9", "3", "abc", "1234567812345678", "13/2099", "01/2000", "12/2099",
            "a", "007", "3", "x", "9", "1", "x", "9", "2", "1", "0") + "\n";
        String output = run(input, dir);
        check(count(output, "Nome: ") == 2, "nome repetido indevidamente");
        check(count(output, "CPF (11 dígitos): ") == 2, "CPF");
        check(count(output, "Idade: ") == 3, "idade");
        check(count(output, "Número fictício do cartão: ") == 2, "numero cartao");
        check(count(output, "Validade (MM/AAAA): ") == 3, "validade");
        check(count(output, "CVV fictício: ") == 2, "CVV");
        check(output.contains("Entrada negada.") && output.contains("Entrada liberada."), "credencial: " + output);
        check(count(output, "Cartão autorizado") == 1, "cobranca duplicada");
        List<String> compras = Files.readAllLines(dir.resolve("compras.txt"));
        check(compras.size() == 1 && compras.get(0).endsWith(";Ana;ESTUDANTE;CARTAO_CREDITO;50.00;50;25.00"), "compra");
        passed++;

        // Nova execução lê o histórico preservado.
        check(run("2\n0\n", dir).contains("Ana (ESTUDANTE)"), "reinicio historico");
        passed++;
        dir = Files.createTempDirectory("fluxo-pix-");
        output = run("1\nBia\n01234567890\n70\n3\n1\n1\n\nchave\n0\n", dir);
        check(count(output, "Chave Pix fictícia: ") == 2 && count(output, "Nome: ") == 1, "pix campo");
        passed++;
        dir = Files.createTempDirectory("fluxo-visitante-");
        output = run("1\nBia\n01234567890\n70\n5\n0\n", dir);
        check(output.contains("Visitante cadastrado") && !Files.exists(dir.resolve("compras.txt")), "visitante");
        passed++;
        dir = Files.createTempDirectory("fluxo-cancelar-");
        output = run("1\nBia\n01234567890\n30\n1\n1\n0\n3\n0\n", dir);
        check(output.contains("Nenhuma credencial") && !Files.exists(dir.resolve("compras.txt")), "cancelar pagamento");
        passed++;
        dir = Files.createTempDirectory("fluxo-eof-");
        check(run("1\nAna\n", dir).contains("Entrada encerrada"), "EOF");
        passed++;

        // Falha real de gravação: compras.txt é diretório. Recuperação sem nova cobrança.
        dir = Files.createTempDirectory("fluxo-gravacao-");
        Files.createDirectory(dir.resolve("compras.txt"));
        Process p = start(dir);
        p.getOutputStream().write("1\nAna\n01234567890\n20\n2\n1\n2\n".getBytes(StandardCharsets.UTF_8));
        p.getOutputStream().flush();
        long deadline = System.currentTimeMillis() + 10000;
        do {
            output = Files.readString(dir.resolve("saida.txt"));
            if (output.contains("pressione Enter")) break;
            Thread.sleep(30);
        } while (System.currentTimeMillis() < deadline);
        check(output.contains("registro pendente") && !output.contains("Credencial 1 ativa"), "pendencia");
        Files.delete(dir.resolve("compras.txt"));
        p.getOutputStream().write("\n0\n".getBytes(StandardCharsets.UTF_8));
        p.getOutputStream().close();
        check(p.waitFor(10, TimeUnit.SECONDS), "recuperacao timeout");
        output = Files.readString(dir.resolve("saida.txt"));
        check(count(output, "recebido.") == 1 && count(output, "Credencial 1 ativa") == 1, "repetiu cobranca");
        check(Files.readAllLines(dir.resolve("compras.txt")).size() == 1, "duplicou compra");
        passed++;

        Cartao card = new CartaoCredito("1234567812345678", "12/2099", "007");
        check(!card.autorizar(Float.NaN) && !card.autorizar(-1), "valor cartao");
        check(!new PagamentoPix("").pagar(50) && !new PagamentoDinheiro().pagar(Float.POSITIVE_INFINITY), "valor pix dinheiro");
        check(!new PagamentoCartao(null).pagar(50), "null cartao");
        check(!Cartao.validadeValida("13/2099") && !Cartao.validadeValida("01/2000"), "data");
        Pessoa pessoa = new Estudante();
        pessoa.cadastrar("Ana", "01234567890", 20, 2);
        try { pessoa.cadastrar("Bia", "errado", 20, 2); throw new AssertionError("cpf invalido"); }
        catch (IllegalArgumentException e) { check(pessoa.getNomePessoa().equals("Ana"), "alterou estado"); }
        Sessao s = new Sessao(new Palestra("Java", "1h", "POO", new Palestrante()), new Sala(1,1), "14h");
        s.ocuparVaga();
        try { s.ocuparVaga(); throw new AssertionError("excedeu capacidade"); }
        catch (IllegalStateException e) { check(s.getVagasDisponiveis() == 0, "capacidade negativa"); }
        passed++;
        System.out.println("PASS: " + passed + " grupos de testes de fluxo e domínio.");
    }
}

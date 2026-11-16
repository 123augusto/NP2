package Palestra.sistema;

import Palestra.modelo.palestra.Credencial;
import Palestra.modelo.palestra.Ingresso;
import Palestra.modelo.palestra.Sessao;
import Palestra.modelo.pessoa.Comum;
import Palestra.modelo.pessoa.Estudante;
import Palestra.modelo.pessoa.Idoso;
import Palestra.modelo.pessoa.Pcd;
import Palestra.modelo.pessoa.Pessoa;
import Palestra.modelo.pessoa.Palestrante;
import Palestra.modelo.pessoa.Visitante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// Persistência com Java NIO (Files, Path). Três arquivos de texto, campos separados por ";":
//   compras.txt    data;ingresso;credencial;sessao;pagamento;precoBase;desconto%;valorPago;cpf;nome;idade;categoria;extra1;extra2
//   entradas.txt   data;credencial
//   visitantes.txt data;cpf;nome;idade
public class Arquivo {

    private static final int CAMPOS_COMPRA = 14;
    private static final int CAMPOS_ENTRADA = 2;
    private static final int CAMPOS_VISITANTE = 4;

    private final Path compras = Paths.get("compras.txt");
    private final Path entradas = Paths.get("entradas.txt");
    private final Path visitantes = Paths.get("visitantes.txt");

    // ---------- gravação ----------

    public void salvarCompra(Ingresso ingresso, int numeroSessao) throws IOException {
        if (ingresso == null || !ingresso.isPago()) {
            throw new IllegalArgumentException("Só é possível registrar ingressos pagos.");
        }
        if (numeroSessao <= 0 || numeroSessao != ingresso.getSessao().getId()) {
            throw new IllegalArgumentException("Identificador de sessão inválido.");
        }
        Pessoa p = ingresso.getComprador();
        String[] extras = p.getDadosExtras(); // polimorfismo: cada subclasse informa seus dados
        String extra1 = "";
        String extra2 = "";
        if (extras.length > 0) {
            extra1 = extras[0];
        }
        if (extras.length > 1) {
            extra2 = extras[1];
        }
        String linha = String.format(Locale.ROOT, "%s;%s;%s;%d;%s;%.2f;%.0f;%.2f;%s;%s;%d;%s;%s;%s",
                agora(), ingresso.getCodigo(), ingresso.getCredencial().getCodigo(), numeroSessao,
                ingresso.getTipoPagamento(), ingresso.getPrecoBase(),
                ingresso.getPercentualDesconto() * 100, ingresso.calcularValor(),
                p.getCpf(), p.getNome(), p.getIdade(), p.getTipo(),
                extra1, extra2);
        acrescentarLinha(compras, linha);
    }

    public void salvarEntrada(Credencial credencial) throws IOException {
        acrescentarLinha(entradas, agora() + ";" + credencial.getCodigo());
    }

    public void salvarVisitante(Visitante v) throws IOException {
        acrescentarLinha(visitantes, agora() + ";" + v.getCpf() + ";" + v.getNome() + ";" + v.getIdade());
    }

    // Substituição atômica evita deixar uma linha parcial ao falhar a gravação.
    private void acrescentarLinha(Path arquivo, String linha) throws IOException {
        Path destino = arquivo.toAbsolutePath();
        String anterior = "";
        if (Files.exists(destino)) {
            anterior = Files.readString(destino);
        }
        Path temporario = Files.createTempFile(destino.getParent(), "np2-", ".tmp");
        try {
            Files.writeString(temporario, anterior + linha + System.lineSeparator());
            Files.move(temporario, destino, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    private static String agora() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    // ---------- leitura ----------

    private List<String[]> lerRegistros(Path arquivo, int campos) throws IOException {
        List<String[]> registros = new ArrayList<>();
        if (Files.notExists(arquivo)) {
            return registros;
        }
        for (String linha : Files.readAllLines(arquivo)) {
            if (linha.isBlank()) {
                continue;
            }
            String[] partes = linha.split(";", -1);
            if (partes.length == campos
                    || (arquivo.equals(compras) && partes.length == 7)) {
                registros.add(partes);
            } else {
                System.out.println("Aviso: registro ignorado em " + arquivo + " (formato inválido).");
            }
        }
        return registros;
    }

    // Recria os ingressos pagos pelo ID fixo da sessão.
    public List<Ingresso> carregarCompras(Evento evento) throws IOException {
        List<Ingresso> ingressos = new ArrayList<>();
        for (String[] c : lerRegistros(compras, CAMPOS_COMPRA)) {
            if (c.length == 7) {
                continue; // O histórico antigo não contém sessão e credencial.
            }
            try {
                Sessao sessao = evento.getSessaoPorId(Integer.parseInt(c[3]));
                if (sessao == null) {
                    throw new IllegalArgumentException("sessão " + c[3] + " não existe");
                }
                Pessoa pessoa = criarPessoa(c[11], c[9], c[8], Integer.parseInt(c[10]), c[12], c[13]);
                if (!pessoa.comprovarTipo() || !pessoa.podeAssistirPalestra()) {
                    throw new IllegalArgumentException("categoria não comprovada");
                }
                ingressos.add(Ingresso.restaurar(pessoa, sessao, c[1], c[2], c[4],
                        Double.parseDouble(c[5]), Double.parseDouble(c[6]) / 100,
                        Double.parseDouble(c[7])));
            } catch (IllegalArgumentException e) { // inclui NumberFormatException
                System.out.println("Aviso: compra " + c[1] + " ignorada (" + e.getMessage() + ").");
            }
        }
        return ingressos;
    }

    public Set<String> carregarEntradas() throws IOException {
        Set<String> codigos = new HashSet<>();
        for (String[] e : lerRegistros(entradas, CAMPOS_ENTRADA)) {
            codigos.add(e[1]);
        }
        return codigos;
    }

    public List<Visitante> carregarVisitantes() throws IOException {
        List<Visitante> lista = new ArrayList<>();
        for (String[] v : lerRegistros(visitantes, CAMPOS_VISITANTE)) {
            try {
                Visitante visitante = new Visitante(v[2], v[1], Integer.parseInt(v[3]));
                lista.add(visitante);
            } catch (IllegalArgumentException e) {
                System.out.println("Aviso: visitante ignorado (" + e.getMessage() + ").");
            }
        }
        return lista;
    }

    // Recria a subclasse certa de Pessoa a partir da categoria salva
    private Pessoa criarPessoa(String categoria, String nome, String cpf, int idade,
                               String extra1, String extra2) {
        Pessoa pessoa;
        switch (categoria) {
            case "COMUM": pessoa = new Comum(nome, cpf, idade); break;
            case "ESTUDANTE": pessoa = new Estudante(nome, cpf, idade, extra1, Integer.parseInt(extra2)); break;
            case "IDOSO": pessoa = new Idoso(nome, cpf, idade); break;
            case "PCD": pessoa = new Pcd(nome, cpf, idade, extra1); break;
            case "PALESTRANTE":
                pessoa = idade == 0 ? new Palestrante(nome, cpf, extra1)
                        : new Palestrante(nome, cpf, idade, extra1);
                break;
            default: throw new IllegalArgumentException("categoria desconhecida: " + categoria);
        }
        return pessoa;
    }

    // ---------- histórico ----------

    public void listarCompras() throws IOException {
        List<String[]> registros = lerRegistros(compras, CAMPOS_COMPRA);
        if (registros.isEmpty()) {
            System.out.println("Nenhuma compra registrada.");
            return;
        }
        for (String[] c : registros) {
            if (c.length == 7) {
                System.out.println(c[0] + " | " + c[1] + " (" + c[2] + ") | " + c[3]
                        + " | Original: R$ " + c[4] + " | Desconto: " + c[5] + "%"
                        + " | Pago: R$ " + c[6] + " | Histórico antigo (sem sessão/credencial)");
                continue;
            }
            System.out.println(c[0] + " | " + c[1] + " | " + c[2] + " | " + c[9]
                    + " (" + c[11] + ") | Sessão " + c[3] + " | " + c[4]
                    + " | Original: R$ " + c[5]
                    + " | Desconto: " + c[6] + "%"
                    + " | Pago: R$ " + c[7]);
        }
    }
}

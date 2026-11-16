package Palestra.modelo.palestra;

import Palestra.modelo.pessoa.Pessoa;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class Credencial {

    private static int contador = 0; // garante que cada código é único
    private static String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String formato_do_codigo = "CRED-[0-9]+-[A-Z0-9]{3}";
    private static Random random = new Random();

    private String codigo;
    private Pessoa titular;
    private Sessao sessao; // válida para UMA sessão
    private boolean ativa;
    private boolean utilizada;

    // Nova credencial: gera um código novo
    public Credencial(Pessoa titular, Sessao sessao) {
        validar(titular, sessao);
        contador++;
        this.codigo = "CRED-" + contador + "-" + gerarCodigo(); // ex: CRED-1-K7Q
        this.titular = titular;
        this.sessao = sessao;
    }

   //Sobrecarga de construtor para recriar o objeto que foi salvo no arquivo
    protected Credencial(Pessoa titular, Sessao sessao, String codigo) {
        validar(titular, sessao);
        if (codigo == null || !codigo.matches(formato_do_codigo)) {
            throw new IllegalArgumentException("Código de credencial inválido: " + codigo);
        }
        this.codigo = codigo;
        this.titular = titular;
        this.sessao = sessao;
        // o contador continua depois do maior número já emitido, evitando códigos repetidos
        int numero = Integer.parseInt(codigo.split("-")[1]);
        if (numero > contador) {
            contador = numero;
        }
    }

    private static void validar(Pessoa titular, Sessao sessao) {
        if (titular == null) {
            throw new IllegalArgumentException("Credencial exige titular.");
        }
        if (sessao == null) {
            throw new IllegalArgumentException("Credencial exige sessão.");
        }
    }

    private static String gerarCodigo() {
        Set<Character> caracteresUnicos = new LinkedHashSet<>();
        while (caracteresUnicos.size() < 3) {
            caracteresUnicos.add(alfabeto.charAt(random.nextInt(alfabeto.length())));
        }
        StringBuilder sb = new StringBuilder();
        //for each
        for (char c : caracteresUnicos) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void ativar() {
        ativa = true;
    }

    public void registrarEntrada() {
        if (ativa) {
            utilizada = true;
        }
    }

    public void exibirCredencial() {
        System.out.println("===== Credencial " + codigo + " =====");
        System.out.println("Titular: " + titular.getNome() + " (" + titular.getTipo() + ")");
        System.out.println("Palestra: " + sessao.getPalestra().getNome() + " às " + sessao.getHorario()
                + " | Sala " + sessao.getSala().getNumero());
        if (ativa==false) {
            System.out.println("Status: AGUARDANDO PAGAMENTO");
        } else if (utilizada) {
            System.out.println("Status: UTILIZADA");
        } else {
            System.out.println("Status: VÁLIDA");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public Pessoa getTitular() {
        return titular;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public boolean isUtilizada() {
        return utilizada;
    }
}

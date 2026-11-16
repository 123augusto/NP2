package Palestra.modelo.pessoa;

// Classe abstrata: não existe "pessoa genérica" no evento,
// toda pessoa é de uma categoria (Comum, Estudante, Idoso, Pcd, Visitante, Palestrante).
public abstract class Pessoa {
    public static final int IDADE_MINIMA = 18;
    public static final int IDADE_MAXIMA = 120;

    private final String nome;
    private final String cpf;
    private final int idade;

    public Pessoa(String nome, String cpf, int idade) {
        this(nome, cpf, idade, true);
    }

    // Para cadastros que não exigem idade, como a programação de palestrantes.
    protected Pessoa(String nome, String cpf) {
        this(nome, cpf, 0, false);
    }


    private Pessoa(String nome, String cpf, int idade, boolean idadeInformada) {
        if (nomeValido(nome)==false) {
            throw new IllegalArgumentException("Informe um nome sem ponto e vírgula ou quebras de linha.");
        }
        if (cpfValido(cpf)==false) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos.");
        }
        if (idadeInformada && (idade < IDADE_MINIMA || idade > IDADE_MAXIMA)) {
            throw new IllegalArgumentException("Informe uma idade entre " + IDADE_MINIMA
                    + " e " + IDADE_MAXIMA + " anos.");
        }
        this.nome = nome.trim();
        this.cpf = cpf;
        this.idade = idade;
    }

    // Métodos abstratos: cada subclasse é obrigada a implementar (polimorfismo)
    public abstract String getTipo();                 // nome da categoria
    public abstract double getPercentualDesconto();   // 0.0 = sem desconto, 0.5 = 50%, 1.0 = gratuito
    public abstract boolean comprovarTipo();          // a pessoa comprova a categoria declarada?

    // Método concreto que pode ser sobrescrito (Visitante sobrescreve)
    public boolean podeAssistirPalestra() {
        return true;
    }
    public boolean podeComprarIngresso() { return true; }

    public static boolean nomeValido(String nome) {
        if(nome != null && !nome.isBlank() && !nome.contains(";") && !nome.contains("\n") && !nome.contains("\r") && nome.length() > 2)
            return true;
        else
            return false;
    }

    public static boolean cpfValido(String cpf) {
        if(cpf != null && cpf.matches("[0-9]{11}"))
        return true;
        else
            return false;
    }

    // Campos de texto livre não podem ter ";" nem quebra de linha (separador do arquivo)
    protected static String textoSeguro(String texto, String campo) {
        if (texto == null) {
            return "";
        }
        if (texto.contains(";") || texto.contains("\n") || texto.contains("\r")) {
            throw new IllegalArgumentException(campo + " não pode conter ponto e vírgula ou quebra de linha.");
        }
        return texto.trim();
    }

    // Dados próprios de cada subclasse, usados para salvar e recriar a pessoa.
    // Sobrescrito por Estudante e Pcd.
    public String[] getDadosExtras() {
        String[] semDados = new String[0];
        return semDados;
    }
    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public int getIdade() {
        return idade;
    }
}

package modelo.pessoa;

public class Pessoa {
    private String nomePessoa;
    private String CPF;
    private int idade;
    private boolean excepcionalidade;
    private int modalidade;

    // 1 = comum, 2 = estudante, 3 = idoso, 4 = PCD, 5 = visitante
    public void cadastrar(String nomePessoa, String CPF, int idade, int modalidade) {
        if (!nomeValido(nomePessoa)) {
            throw new IllegalArgumentException("Informe um nome sem ponto e vírgula ou quebras de linha.");
        }
        if (!cpfValido(CPF)) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos.");
        }
        if (idade < 0) {
            throw new IllegalArgumentException("Idade inválida.");
        }
        if (modalidade != modalidadeDaClasse()) {
            throw new IllegalArgumentException("Modalidade incompatível com o tipo de cadastro.");
        }
        if (modalidade == 3 && idade < 60) {
            throw new IllegalArgumentException("A modalidade idoso exige 60 anos ou mais.");
        }

        this.nomePessoa = nomePessoa.trim();
        this.CPF = CPF;
        this.idade = idade;
        this.modalidade = modalidade;
        this.excepcionalidade = modalidade == 2 || modalidade == 3 || modalidade == 4;
    }

    public static boolean nomeValido(String nome) {
        return nome != null && !nome.isBlank() && !nome.contains(";")
                && !nome.contains("\n") && !nome.contains("\r");
    }

    public static boolean cpfValido(String cpf) {
        return cpf != null && cpf.matches("[0-9]{11}");
    }

    protected int modalidadeDaClasse() {
        return 1;
    }

    public String getCategoria() {
        return "COMUM";
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public String getCPF() {
        return CPF;
    }

    public int getIdade() {
        return idade;
    }

    public int getModalidade() {
        return modalidade;
    }

    public boolean isExcepcionalidade() {
        return excepcionalidade;
    }
}


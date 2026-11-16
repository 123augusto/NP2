package Palestra.modelo.pessoa;

public class Estudante extends Pessoa {
    private String instituicao;
    private int matricula;

    public Estudante(String nome, String cpf, int idade, String instituicao, int matricula) {
        super(nome, cpf, idade);
        this.instituicao = textoSeguro(instituicao, "Instituição");
        this.matricula = matricula;
    }

    @Override
    public String getTipo() {
        return "ESTUDANTE";
    }

    @Override
    public double getPercentualDesconto() {
        return 0.5;
    }

    @Override
    public boolean comprovarTipo() {
        if (matricula > 0 && instituicao != null && !instituicao.isBlank())
            return true;
        else
            return false;
    }

    @Override
    public String[] getDadosExtras() {
        String[] dados = {instituicao, String.valueOf(matricula)};
        return dados;
    }

}

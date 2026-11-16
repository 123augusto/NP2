package Palestra.modelo.pessoa;

public class Palestrante extends Pessoa {
    private String instituicao;

    public Palestrante(String nome, String cpf, int idade, String instituicao) {
        super(nome, cpf, idade);
        this.instituicao = textoSeguro(instituicao, "Instituição");
    }

    // Sobrecarga de construtor: quando a idade não é informada
    public Palestrante(String nome, String cpf, String instituicao) {
        this(nome, cpf, 0, instituicao); // chama o construtor de cima
    }

    @Override
    public String getTipo() {
        return "PALESTRANTE";
    }

    @Override
    public double getPercentualDesconto() {
        return 1; // palestrante não paga
    }

    @Override
    public String[] getDadosExtras() {
        return new String[] {instituicao};
    }

    @Override
    public boolean comprovarTipo() {
        if(instituicao != null && !instituicao.isBlank())
            return true;
        else
            return false;
    }

}

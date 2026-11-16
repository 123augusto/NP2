package Palestra.modelo.pessoa;

public class Idoso extends Pessoa {
    public Idoso(String nome, String cpf, int idade) {
        super(nome, cpf, idade);
    }

    @Override
    public String getTipo() {
        return "IDOSO";
    }

    @Override
    public double getPercentualDesconto() {
        return 0.5;
    }

    @Override
    public boolean comprovarTipo() {
        return getIdade() >= 60;
    }
}

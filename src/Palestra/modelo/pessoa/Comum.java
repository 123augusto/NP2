package Palestra.modelo.pessoa;

public class Comum extends Pessoa {
    public Comum(String nome, String cpf, int idade) {
        super(nome, cpf, idade);
    }

    @Override
    public String getTipo() {
        return "COMUM";
    }

    @Override
    public double getPercentualDesconto() {
        return 0;
    }

    @Override
    public boolean comprovarTipo() {
        return true; // não precisa comprovar nada
    }
}

package Palestra.modelo.pessoa;

// Visitante circula pelo evento, mas não paga e não assiste às palestras.
public class Visitante extends Pessoa {
    public Visitante(String nome, String cpf, int idade) {
        super(nome, cpf, idade);
    }

    @Override
    public String getTipo() {
        return "VISITANTE";
    }

    @Override
    public double getPercentualDesconto() {
        return 0;
    }

    @Override
    public boolean comprovarTipo() {
        return true;
    }

    @Override
    public boolean podeAssistirPalestra() {
        return false;
    }
}

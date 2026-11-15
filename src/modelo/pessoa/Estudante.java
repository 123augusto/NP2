package modelo.pessoa;

public class Estudante extends Pessoa {
    private String CIE; // Carteira de identificação estudantil

    @Override
    protected int modalidadeDaClasse() {
        return 2;
    }

    @Override
    public String getCategoria() {
        return "ESTUDANTE";
    }
}


package modelo.pessoa;

public class Visitante extends Pessoa {
    @Override
    protected int modalidadeDaClasse() {
        return 5;
    }

    @Override
    public String getCategoria() {
        return "VISITANTE";
    }
}


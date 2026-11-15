package modelo.pessoa;

public class Idoso extends Pessoa {
    @Override
    protected int modalidadeDaClasse() {
        return 3;
    }

    @Override
    public String getCategoria() {
        return "IDOSO";
    }
}


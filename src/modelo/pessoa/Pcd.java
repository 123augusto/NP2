package modelo.pessoa;

public class Pcd extends Pessoa {
    @Override
    protected int modalidadeDaClasse() {
        return 4;
    }

    @Override
    public String getCategoria() {
        return "PCD";
    }
}


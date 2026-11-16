package Palestra.modelo.pessoa;

public class Pcd extends Pessoa {
    private String cartaoPcd;

    public Pcd(String nome, String cpf, int idade, String cartaoPcd) {
        super(nome, cpf, idade);
        this.cartaoPcd = textoSeguro(cartaoPcd, "Cartão PCD");
    }

    @Override
    public String getTipo() {
        return "PCD";
    }

    @Override
    public double getPercentualDesconto() {
        return 0.5;
    }

    @Override
    public boolean comprovarTipo() {
        if(cartaoPcd != null && !cartaoPcd.isBlank())
        return true;
        else
            return false;
    }

    @Override
    public String[] getDadosExtras() {
        String[] dados = {cartaoPcd};
        return dados;
    }

}

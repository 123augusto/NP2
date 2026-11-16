package Palestra.pagamento;

public class CartaoCredito extends Cartao {

    public CartaoCredito(String numeroCartao, String validade, String cvv) {
        super(numeroCartao, validade, cvv);
    }

    @Override
    public String getTipo() {
        return "CARTAO_CREDITO";
    }
}

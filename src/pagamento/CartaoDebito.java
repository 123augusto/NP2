package pagamento;

public class CartaoDebito extends Cartao {

    public CartaoDebito(String numeroCartao, String cvv, String validade) {
        super(numeroCartao, validade, cvv);
    }
}
package Palestra.pagamento;

public class CartaoDebito extends Cartao {

    // Mesma ordem de parâmetros do CartaoCredito
    public CartaoDebito(String numeroCartao, String validade, String cvv) {
        super(numeroCartao, validade, cvv);
    }

    @Override
    public String getTipo() {
        return "CARTAO_DEBITO";
    }
}

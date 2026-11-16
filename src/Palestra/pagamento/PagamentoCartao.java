package Palestra.pagamento;

public class PagamentoCartao implements Pagamento {
    private Cartao cartao; // associação

    public PagamentoCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    @Override
    public boolean pagar(double valor) {
        return cartao != null && cartao.autorizar(valor);
    }

    @Override
    public String getTipo() {
        if (cartao == null) {
            return "CARTAO";
        }
        return cartao.getTipo();
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
}

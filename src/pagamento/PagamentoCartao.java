package pagamento;

public class PagamentoCartao implements Pagamento{
    private Cartao cartao; //composicao

    public PagamentoCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    @Override
    public boolean pagar(float valor) {
        return cartao.autorizar(valor);
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
}

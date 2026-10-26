package pagamento;

public class PagamentoDinheiro implements Pagamento {

    @Override
    public boolean pagar(float valor) {
        System.out.println("Pagamento" + valor + " recebido.");
        return true;
    }
}

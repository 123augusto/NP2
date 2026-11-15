package pagamento;

public class PagamentoDinheiro implements Pagamento {

    @Override
    public boolean pagar(float valor) {
        if (!Float.isFinite(valor) || valor <= 0) return false;
        System.out.println("Pagamento de R$ " + valor + " recebido.");
        return true;
    }
}


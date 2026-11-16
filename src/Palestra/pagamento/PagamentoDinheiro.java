package Palestra.pagamento;

public class PagamentoDinheiro implements Pagamento {

    @Override
    public boolean pagar(double valor) {
        if (!Double.isFinite(valor) || valor < 0) {
            return false;
        }
        System.out.printf("Pagamento de R$ %.2f recebido.%n", valor);
        return true;
    }

    @Override
    public String getTipo() {
        return "DINHEIRO";
    }
}

package Palestra.pagamento;

public interface Pagamento {
    boolean pagar(double valor);

    String getTipo(); // ex: "PIX", "DINHEIRO", "CARTAO_CREDITO"
}

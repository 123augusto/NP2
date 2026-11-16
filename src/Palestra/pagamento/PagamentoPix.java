package Palestra.pagamento;

public class PagamentoPix implements Pagamento {
    private String chave; // chave pix

    public PagamentoPix(String chave) {
        this.chave = chave;
    }

    @Override
    public boolean pagar(double valor) {
        if (!Double.isFinite(valor) || valor < 0 || chave == null || chave.isBlank()) {
            return false;
        }
        System.out.printf("Pagamento via Pix aprovado: R$ %.2f%n", valor);
        return true;
    }

    @Override
    public String getTipo() {
        return "PIX";
    }
}

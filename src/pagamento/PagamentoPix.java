package pagamento;

public class PagamentoPix implements Pagamento{
    private String key;//chave pix

    public PagamentoPix(String key) {
        this.key = key;
    }

    @Override
    public boolean pagar(float valor) {
        if (!Float.isFinite(valor) || valor <= 0 || key == null || key.isBlank()) return false;
        System.out.println("Pagamento via Pix aprovado: R$ " + valor);
        return true;
    }
}


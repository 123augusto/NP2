package pagamento;

public class PagamentoPix implements Pagamento{
    private String key;//chave pix

    public PagamentoPix(String key) {
        this.key = key;
    }

    @Override
    public boolean pagar(float valor) {
        System.out.println("Pagamento via pix " + valor + " usando a chave " + key);
        return true;
    }
}

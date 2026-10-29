package pagamento;

public class CartaoDebito implements Cartao{

    private String numeroCartao;
    private String CVV;
    private String validadeDebito;

    public CartaoDebito(String numeroCartao, String CVV, String validade) {
        this.numeroCartao = numeroCartao;
        this.CVV = CVV;
        this.validadeDebito = validade;
    }

    @Override
    public boolean validar() {
        if (numeroCartao.length() != 16){//cartao tem 16 numeros
            return false;

        }
        // CVV: precisa existir e conter exatamente 3 dígitos.
        if (CVV == null || !CVV.matches("[0-9]{3}")) {
            return false;
        }

        // Validade: precisa seguir o formato MM/AAAA.
        if (validadeDebito == null || !validadeDebito.matches("[0-9]{2}/[0-9]{4}")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean autorizar(float valor) {
        if(validar()){
            System.out.println("Cartao autorizado: R$ " + valor);
            return true;
        }else{
            System.out.println("Cartão inválido");
            return false;
        }

    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getCVV() {
        return CVV;
    }

    public String getValidadeDebito() {
        return validadeDebito;
    }
}

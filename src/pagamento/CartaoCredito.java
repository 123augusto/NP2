package pagamento;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CartaoCredito implements Cartao{

    private String numeroCartao;
    private String validadeCartao;
    private String CVV;

    public CartaoCredito(String numeroCartao, String validadeCartao, String CVV) {
        this.numeroCartao = numeroCartao;
        this.validadeCartao = validadeCartao;
        this.CVV = CVV;
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
        if (validadeCartao == null || !validadeCartao.matches("[0-9]{2}/[0-9]{4}")) {
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
}

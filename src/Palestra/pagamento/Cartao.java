package Palestra.pagamento;

import java.time.YearMonth;

// Classe abstrata: concentra o que Crédito e Débito têm em comum
public abstract class Cartao {
    private final String numeroCartao;
    private final String validade;
    private final String cvv;

    protected Cartao(String numeroCartao, String validade, String cvv) {
        this.numeroCartao = numeroCartao;
        this.validade = validade;
        this.cvv = cvv;
    }

    public abstract String getTipo();

    public static boolean numeroValido(String numero) {
        return numero != null && numero.matches("[0-9]{16}");
    }

    public static boolean cvvValido(String cvv) {
        return cvv != null && cvv.matches("[0-9]{3}");
    }

    public static boolean validadeValida(String validade) {
        if (validade == null || !validade.matches("(0[1-9]|1[0-2])/[0-9]{4}")) {
            return false;
        }
        int mes = Integer.parseInt(validade.substring(0, 2));
        int ano = Integer.parseInt(validade.substring(3));
        return !YearMonth.of(ano, mes).isBefore(YearMonth.now());
    }

    public boolean validar() {
        if(numeroValido(numeroCartao) && cvvValido(cvv) && validadeValida(validade)==true)
            return true;
        else return false;
    }

    public boolean autorizar(double valor) {
        if (!Double.isFinite(valor) || valor < 0 || !validar()) {
            return false;
        }
        System.out.printf("Cartão autorizado: R$ %.2f%n", valor);
        return true;
    }
}

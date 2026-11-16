package Palestra.modelo.palestra;

import Palestra.modelo.pessoa.Pessoa;
import Palestra.pagamento.Pagamento;
import java.util.Locale;

public class Ingresso {

    private static String prefixo = "#TICKET-";
    private static int contador = 0;

    private String codigo;
    private Pessoa comprador;
    private Sessao sessao;
    private Credencial credencial; // composição: cada ingresso tem a sua credencial
    private String tipoPagamento;        // definido quando o pagamento é aprovado
    private boolean pago;
    private double precoBase;
    private double percentualDesconto;
    private double valorCompra;

    // Novo ingresso (ainda não pago)
    public Ingresso(Pessoa comprador, Sessao sessao) {
        Credencial novaCredencial = new Credencial(comprador, sessao); // valida comprador e sessão
        this.codigo = proximoCodigo();
        this.credencial = novaCredencial;
        this.comprador = comprador;
        this.sessao = sessao;
        this.precoBase = sessao.getPalestra().getPreco();
        this.percentualDesconto = comprador.getPercentualDesconto();
        this.valorCompra = arredondarValor(precoBase * (1 - percentualDesconto));
        this.pago = false;
    }

    private Ingresso(String codigo, Credencial credencial) {
        this.codigo = codigo;
        this.credencial = credencial;
        this.comprador = credencial.getTitular();
        this.sessao = credencial.getSessao();
        this.pago = false;
    }

    private static String proximoCodigo() {
        contador++;
        return prefixo + contador;
    }

    // Recria um ingresso já pago a partir dos dados salvos no arquivo
    public static Ingresso restaurar(Pessoa comprador, Sessao sessao, String codigo,
                                     String codigoCredencial, String tipoPagamento,
                                     double precoBase, double percentualDesconto, double valorCompra) {
        if (!Double.isFinite(precoBase) || precoBase < 0
                || !Double.isFinite(percentualDesconto) || percentualDesconto < 0 || percentualDesconto > 1
                || !Double.isFinite(valorCompra) || valorCompra < 0
                || Math.abs(valorCompra - precoBase * (1 - percentualDesconto)) > 0.011) {
            throw new IllegalArgumentException("Valores da compra inválidos.");
        }
        if (codigo == null || !codigo.matches("#TICKET-[0-9]+")) {
            throw new IllegalArgumentException("Código de ingresso inválido: " + codigo);
        }
        if (tipoPagamento == null || tipoPagamento.isBlank()) {
            throw new IllegalArgumentException("Forma de pagamento não informada.");
        }
        Credencial credencialSalva = new Credencial(comprador, sessao, codigoCredencial);
        Ingresso ingresso = new Ingresso(codigo, credencialSalva);
        ingresso.precoBase = precoBase;
        ingresso.percentualDesconto = percentualDesconto;
        ingresso.valorCompra = valorCompra;
        ingresso.pago = true;
        ingresso.tipoPagamento = tipoPagamento;
        ingresso.credencial.ativar();
        int numero = Integer.parseInt(codigo.substring(prefixo.length()));
        if (numero > contador) {
            contador = numero; // próximos ingressos continuam a numeração
        }
        return ingresso;
    }

    private static double arredondarValor(double valor) {
        return Double.parseDouble(String.format(Locale.ROOT, "%.2f", valor));
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    // Preserva o valor definido na compra, inclusive após restaurar o arquivo.
    public double calcularValor() {
        return valorCompra;
    }

    // Polimorfismo: qualquer classe que implemente Pagamento serve aqui
    public boolean pagar(Pagamento forma) {
        if (pago==true) {
            System.out.println("O ingresso " + codigo + " já foi pago.");
            return true;
        }
        if (forma == null) {
            return false;
        }
        pago = forma.pagar(calcularValor());
        if (pago==true) {
            tipoPagamento = forma.getTipo();
            credencial.ativar();
            System.out.println("Pagamento do ingresso " + codigo + " aprovado. Aguardando registro.");
        } else {
            System.out.println("Pagamento do ingresso " + codigo + " recusado.");
        }
        return pago;
    }

    public void exibirResumo() {
        System.out.println("Ingresso: " + codigo);
        System.out.println("Comprador: " + comprador.getNome() + " (" + comprador.getTipo() + ")");
        System.out.println("Palestra: " + sessao.getPalestra().getNome() + " às " + sessao.getHorario());
        System.out.printf("Preço base: R$ %.2f%n", getPrecoBase());
        System.out.printf("Desconto: %.0f%%%n", getPercentualDesconto() * 100);
        System.out.printf("Valor final: R$ %.2f%n", calcularValor());
        if (pago==true) {
            System.out.println("Status: PAGO (" + tipoPagamento + ")");
        } else {
            System.out.println("Status: PENDENTE");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public Pessoa getComprador() {
        return comprador;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public Credencial getCredencial() {
        return credencial;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public boolean isPago() {
        return pago;
    }
}

package Palestra.formulario;

import Palestra.leitor.Leitor;
import Palestra.pagamento.Cartao;
import Palestra.pagamento.CartaoCredito;
import Palestra.pagamento.CartaoDebito;
import Palestra.pagamento.Pagamento;
import Palestra.pagamento.PagamentoCartao;
import Palestra.pagamento.PagamentoDinheiro;
import Palestra.pagamento.PagamentoPix;

// Lê a forma de pagamento escolhida e devolve um objeto que implementa Pagamento
public class FormularioPagamento {

    private FormularioPagamento() {
    }

    // Devolve null quando o usuário cancela
    //Polimorfismo com a interface
    public static Pagamento ler() {
        System.out.println("1 - Pix | 2 - Dinheiro | 3 - Crédito | 4 - Débito | 0 - Cancelar compra");
        int opcao = Leitor.inteiro("Forma de pagamento: ", 0, 4); //min e max
        Pagamento pagamento = null; // continua null se o usuário cancelar (opção 0)
        if (opcao == 1) {
            String chave = Leitor.verificarTextoVazio("Chave Pix fictícia: ", "Informe a chave Pix.");
            pagamento = new PagamentoPix(chave);
        } else if (opcao == 2) {
            pagamento = new PagamentoDinheiro();
        } else if (opcao == 3 || opcao == 4) {
            Cartao cartao = lerCartao(opcao == 3);//verifica se eh de debito ou credito
            pagamento = new PagamentoCartao(cartao);
        }
        return pagamento;
    }

    private static Cartao lerCartao(boolean credito) {
        String numero = Leitor.numeroCartao();
        String validade = Leitor.validadeCartao();
        String cvv = Leitor.cvv();
        Cartao cartao;
        if (credito) {
            cartao = new CartaoCredito(numero, validade, cvv);
        } else {
            cartao = new CartaoDebito(numero, validade, cvv);
        }
        return cartao;
    }
}

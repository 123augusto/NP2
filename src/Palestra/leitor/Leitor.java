package Palestra.leitor;

import Palestra.modelo.pessoa.Pessoa;
import Palestra.pagamento.Cartao;

import java.util.Scanner;

// Classe utilitária para entrada de dados
// DRY
public class Leitor {

    private static Scanner entrada = new Scanner(System.in);

    private Leitor() {
        //Nunca deve ser instanciado, por isso criado como private
    }

    public static String texto(String mensagem) {
        System.out.print(mensagem);
        return entrada.nextLine().trim();
    }

    public static String verificarTextoVazio(String mensagem, String erro) {
        String valor = texto(mensagem);
        while (valor.isEmpty()) {
            System.out.println(erro);
            valor = texto(mensagem);
        }
        return valor;
    }
    //Sera usada em partes que pecam numeros inteiros(idade,matricula,forma de pagamento)
    public static int inteiro(String mensagem, int min, int max) {
        //Pedindo pro usuario digitar um valor ate estar correto
        while (true) {
            try {
                int valor = Integer.parseInt(texto(mensagem));//Convertendo o texto em int
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }
    }

    public static String textoParaArquivo(String mensagem) {
        String valor = texto(mensagem);
        while (!Pessoa.nomeValido(valor)) {
            System.out.println("Preencha o campo sem ponto e vírgula ou quebras de linha.");
            valor = texto(mensagem);
        }
        return valor;
    }

    public static String nome() {
        String nome = texto("Nome: ");
        while (Pessoa.nomeValido(nome) == false) {
            System.out.println("Nome inválido.");
            nome = texto("Nome: ");
        }
        return nome;
    }

    public static String cpf() {
        String cpf = texto("CPF (11 dígitos): ");
        while (Pessoa.cpfValido(cpf) == false) {
            System.out.println("CPF deve conter 11 dígitos.");
            cpf = texto("CPF (11 dígitos): ");
        }
        return cpf;
    }

    public static String numeroCartao() {
        String numero = texto("Número fictício do cartão: ");
        while (Cartao.numeroValido(numero) == false) {
            System.out.println("Informe 16 dígitos.");
            numero = texto("Número fictício do cartão: ");
        }
        return numero;
    }

    public static String validadeCartao() {
        String validade = texto("Validade (MM/AAAA): ");
        while (Cartao.validadeValida(validade) == false) {
            System.out.println("Validade inválida ou vencida.");
            validade = texto("Validade (MM/AAAA): ");
        }
        return validade;
    }


    public static String cvv() {
        String cvv = texto("CVV fictício: ");
        while (Cartao.cvvValido(cvv)==false) {
            System.out.println("Informe 3 dígitos.");
            cvv = texto("CVV fictício: ");
        }
        return cvv;
    }

    public static void darEnter() {
        entrada.nextLine();
    }

    public static void fechar() {
        entrada.close();
    }
}

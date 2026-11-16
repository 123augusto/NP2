package Palestra.formulario;

import Palestra.leitor.Leitor;
import Palestra.modelo.pessoa.Comum;
import Palestra.modelo.pessoa.Estudante;
import Palestra.modelo.pessoa.Idoso;
import Palestra.modelo.pessoa.Pcd;
import Palestra.modelo.pessoa.Pessoa;
import Palestra.modelo.pessoa.Visitante;

// Lê os dados do teclado e cria a subclasse de Pessoa correspondente
public class FormularioPessoa {

    private FormularioPessoa() {
    }
    //Classe do tipo Pessoa
    public static Pessoa ler() {
        String nome = Leitor.nome();
        String cpf = Leitor.cpf();
        int idade = Leitor.inteiro("Idade: ", 0, 130);

        while (true) {
            System.out.println("1 - Comum | 2 - Estudante | 3 - Idoso | 4 - PCD | 5 - Visitante");
            int tipo = Leitor.inteiro("Categoria: ", 1, 5);
            Pessoa pessoa;
            pessoa = criar(tipo, nome, cpf, idade);
            if (pessoa.comprovarTipo()) { //Polimorfismo
                return pessoa;
            }
            System.out.println("Não foi possível comprovar a categoria " + pessoa.getTipo()
                    + ". Escolha outra categoria.");
        }
    }

    private static Pessoa criar(int tipo, String nome, String cpf, int idade) {
        Pessoa pessoa;
        if (tipo == 2) {
            String instituicao = Leitor.textoParaArquivo("Instituição de ensino: ");
            int matricula = Leitor.inteiro("Matrícula (0 - Não tenho, escolher outra categoria): ", 0, Integer.MAX_VALUE);
            pessoa = new Estudante(nome, cpf, idade, instituicao, matricula);
        } else if (tipo == 3) {
            pessoa = new Idoso(nome, cpf, idade);
        } else if (tipo == 4) {
            String cartaoPcd = Leitor.textoParaArquivo("Número do cartão PCD: ");
            pessoa = new Pcd(nome, cpf, idade, cartaoPcd);
        } else if (tipo == 5) {
            pessoa = new Visitante(nome, cpf, idade);
        } else {
            pessoa = new Comum(nome, cpf, idade);
        }
        return pessoa;
    }
}

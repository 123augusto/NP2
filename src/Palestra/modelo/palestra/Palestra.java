package Palestra.modelo.palestra;

import Palestra.modelo.pessoa.Palestrante;
import Palestra.modelo.pessoa.Pessoa;

public class Palestra {

    private static int contador = 0;

    private int id;
    private String nome;
    private String duracao;
    private String descricao;
    private double preco;
    private Palestrante palestrante; // associação

    public Palestra(String nome, String duracao, String descricao, double preco, Palestrante palestrante) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Palestra exige nome.");
        }
        if (duracao == null || duracao.isBlank()) {
            throw new IllegalArgumentException("Palestra exige duração.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Palestra exige descrição.");
        }
        if (!Double.isFinite(preco) || preco < 0) {
            throw new IllegalArgumentException("Preço da palestra não pode ser negativo.");
        }
        if (palestrante == null) {
            throw new IllegalArgumentException("Palestra exige palestrante.");
        }
        contador++;
        this.id = contador;
        this.nome = nome;
        this.duracao = duracao;
        this.descricao = descricao;
        this.preco = preco;
        this.palestrante = palestrante;
    }

    // Valida a entrada de uma credencial em uma sessão desta palestra
    public boolean validarCredencial(Credencial credencial, Sessao sessao) {
        if (credencial == null || sessao == null) {
            System.out.println("Acesso negado: credencial ou sessão inexistente.");
            return false;
        }
        if (sessao.getPalestra() != this)//Compara se a sessao que foi associada a certa palestra eh igual ao objeto dessa palaestra
        {
            System.out.println("Acesso negado: a sessão não pertence a esta palestra.");
            return false;
        }
        if (!credencial.isAtiva()) {
            System.out.println("Acesso negado: ingresso não pago.");
            return false;
        }
        if (credencial.getSessao() != sessao) {
            System.out.println("Acesso negado: credencial de outra sessão.");
            return false;
        }
        if (credencial.isUtilizada()) {
            System.out.println("Acesso negado: credencial já utilizada.");
            return false;
        }
        if (validarTipoPessoa(credencial.getTitular())==false) {
            return false;
        }
        return true;
    }

    private boolean validarTipoPessoa(Pessoa pessoa) {
        if (pessoa.podeAssistirPalestra()==false) {
            System.out.println("Acesso negado: " + pessoa.getTipo() + " não tem acesso às palestras.");
            return false;
        }
        if (pessoa.comprovarTipo()==false) {
            System.out.println("Acesso negado: " + pessoa.getNome()
                    + " não comprovou a categoria " + pessoa.getTipo() + ".");
            return false;
        }
        return true;
    }
    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }
}

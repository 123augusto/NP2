package modelo.palestra;

public class Palestra {

    private String nome;
    private String duracao;
    private String data;
    private String descricao;
    private String sala;
    private String palestrante; //pesquisar como relacionar a classe palestrante com a classe palestra se precisa criar uma instancia aqui nessa classe
    private String sessao;

    public Palestra(String nome, String duracao, String data, String descricao, String sala, String palestrante, String sessao) {
        this.nome = nome;
        this.duracao = duracao;
        this.data = data;
        this.descricao = descricao;
        this.sala = sala;
        this.palestrante = palestrante;
        this.sessao = sessao;
    }
    private void validarCredencial(){//depois verificar se o retorno é void ou nao

    }
    private void validarTipoPessoa(){

    }
}

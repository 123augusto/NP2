package modelo.palestra;

import modelo.pessoa.Credencial;
import modelo.pessoa.Palestrante;
import modelo.pessoa.Pessoa;
import modelo.pessoa.Visitante;

public class Palestra {

    private String nome;
    private String duracao;
    private String descricao;
    private Palestrante palestrante;

    public Palestra(String nome, String duracao, String descricao, Palestrante palestrante) {
        this.nome = nome;
        this.duracao = duracao;
        this.descricao = descricao;
        this.palestrante = palestrante;
    }

    public String getNome() {
        return nome;
    }

    public boolean validarCredencial(Credencial credencial, Sessao sessao) {
        if (credencial == null || sessao == null) {
            return false;
        }

        if (sessao.getPalestra() != this || credencial.getSessao() != sessao) {
            return false;
        }

        return credencial.isAtiva() && validarTipoPessoa(credencial.getTitular());
    }

    private boolean validarTipoPessoa(Pessoa pessoa) {
        return pessoa != null && !(pessoa instanceof Visitante);
    }
}

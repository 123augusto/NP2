package modelo.pessoa;

import modelo.palestra.Sessao;

public class Credencial {

    private final Pessoa titular;
    private final Sessao sessao;
    private boolean ativa;

    public Credencial(Pessoa titular, Sessao sessao) {
        this.titular = titular;
        this.sessao = sessao;
    }

    public Pessoa getTitular() {
        return titular;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void ativar() {
        ativa = true;
    }
}

package Palestra.sistema;

import Palestra.modelo.palestra.Sessao;

import java.util.ArrayList;
import java.util.List;

// Guarda a programação do evento (sessões das palestras)
public class Evento {

    private final String nome;
    private final ArrayList<Sessao> sessoes = new ArrayList<>();

    public Evento(String nome) {
        this.nome = nome;
    }

    public void adicionarSessao(Sessao sessao) {
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        if (getSessaoPorId(sessao.getId()) != null) {
            throw new IllegalArgumentException("Identificador de sessão repetido.");
        }
        sessoes.add(sessao);
    }

    public void listarSessoes() {
        for (int i = 0; i < sessoes.size(); i++) {
            Sessao s = sessoes.get(i);
            System.out.printf("%d - %s | %s | Sala %d | R$ %.2f | Vagas: %d%n",
                    i + 1, s.getPalestra().getNome(), s.getHorario(), s.getSala().getNumero(),
                    s.getPalestra().getPreco(), s.getVagasDisponiveis());
        }
    }

    // numero começa em 1 (como aparece no menu); devolve null se não existir
    public Sessao getSessao(int numero) {
        if (numero < 1 || numero > sessoes.size()) {
            return null;
        }
        return sessoes.get(numero - 1);
    }

    public Sessao getSessaoPorId(int id) {
        for (Sessao sessao : sessoes) {
            if (sessao.getId() == id) {
                return sessao;
            }
        }
        return null;
    }

    // Posição da sessão no menu. Retorna 0 quando ela não pertence ao evento.
    public int numeroDa(Sessao sessao) {
        return sessoes.indexOf(sessao) + 1;
    }

    public int quantidadeSessoes() {
        return sessoes.size();
    }

    public List<Sessao> getSessoes() {
        return sessoes;
    }

    public String getNome() {
        return nome;
    }
}

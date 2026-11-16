package Palestra.modelo.palestra;

public class Sessao {

    private static int contador = 0;
    private final int id;
    private Palestra palestra;//associação
    private Sala sala;
    private String horario;
    private int ocupadas;

    public Sessao(Palestra palestra, Sala sala, String horario) {
        this(contador + 1, palestra, sala, horario);
    }

    // O ID informado deve continuar o mesmo ao reordenar a programação.
    public Sessao(int id, Palestra palestra, Sala sala, String horario) {
        if (id <= 0) {
            throw new IllegalArgumentException("Identificador da sessão deve ser positivo.");
        }
        if (palestra == null || sala == null || horario == null || horario.isBlank()) {
            throw new IllegalArgumentException("Sessão exige palestra, sala e horário.");
        }
        this.id = id;
        contador = Math.max(contador, id);
        this.palestra = palestra;
        this.sala = sala;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public Palestra getPalestra() {
        return palestra;
    }
    public Sala getSala() {
        return sala;
    }

    public String getHorario() {
        return horario;
    }

    public int getVagasDisponiveis() {
        return sala.getCapacidade() - ocupadas;
    }

    public void ocuparVaga() {
        if (getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Sessão lotada.");
        }
        ocupadas++;
    }
}


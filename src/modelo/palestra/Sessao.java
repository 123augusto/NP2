package modelo.palestra;

public class Sessao {

    private Palestra palestra;//associação
    private Sala sala;
    private String horario;
    private int ocupadas;

    public Sessao(Palestra palestra, Sala sala, String horario) {
        if (palestra == null || sala == null || horario == null || horario.isBlank()) {
            throw new IllegalArgumentException("Sessão exige palestra, sala e horário.");
        }
        this.palestra = palestra;
        this.sala = sala;
        this.horario = horario;
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


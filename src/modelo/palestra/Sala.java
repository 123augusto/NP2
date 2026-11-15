package modelo.palestra;

public class Sala {

    private int numero;
    private int capacidade;

    public Sala(int numero, int capacidade) {
        if (numero <= 0 || capacidade <= 0) {
            throw new IllegalArgumentException("Número e capacidade da sala devem ser positivos.");
        }
        this.numero = numero;
        this.capacidade = capacidade;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidade() {
        return capacidade;
    }
}


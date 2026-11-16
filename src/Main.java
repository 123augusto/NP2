import Palestra.menu.Menu;
import Palestra.leitor.Leitor;
import Palestra.modelo.palestra.Palestra;
import Palestra.modelo.palestra.Sala;
import Palestra.modelo.palestra.Sessao;
import Palestra.modelo.pessoa.Palestrante;
import Palestra.sistema.Bilheteria;
import Palestra.sistema.Evento;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        Evento evento = criarEvento();
        Bilheteria bilheteria = new Bilheteria(evento);
        try {
            while (true) {
                try {
                    bilheteria.carregar();
                    break;
                } catch (IOException e) {
                    System.out.println("Não foi possível carregar os dados salvos: " + e.getMessage());
                    if (Leitor.texto("Enter - Tentar novamente | 0 - Sair: ").equals("0")) {
                        Leitor.fechar();
                        return;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Entrada encerrada.");
            Leitor.fechar();
            return;
        }
        Menu menu = new Menu(evento, bilheteria);
        menu.executar();
    }

    // Dados demonstrativos da programação
    private static Evento criarEvento() {
        Palestrante ana = new Palestrante("Dra. Ana Souza", "11122233344", 45, "Inatel");
        Palestrante carlos = new Palestrante("Eng. Carlos Lima", "55566677788", "Hospital Escola"); // construtor sobrecarregado

        Palestra poo = new Palestra("Introdução à POO", "50 minutos",
                "Classes, objetos e interfaces", 50.0, ana);
        Palestra biomedica = new Palestra("Engenharia Biomédica na Indústria", "60 minutos",
                "Produção de equipamentos médicos", 40.0, carlos);

        Sala sala10 = new Sala(10, 80);
        Sala sala12 = new Sala(12, 40);

        Sessao pooTarde = new Sessao(1, poo, sala10, "14h");
        Sessao pooNoite = new Sessao(2, poo, sala10, "19h");
        Sessao biomedicaTarde = new Sessao(3, biomedica, sala12, "16h");

        Evento evento = new Evento("Semana de Engenharia");
        evento.adicionarSessao(pooTarde);
        evento.adicionarSessao(pooNoite);
        evento.adicionarSessao(biomedicaTarde);
        return evento;
    }
}
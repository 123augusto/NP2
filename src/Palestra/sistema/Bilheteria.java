package Palestra.sistema;

import Palestra.modelo.palestra.Credencial;
import Palestra.modelo.palestra.Ingresso;
import Palestra.modelo.palestra.Sessao;
import Palestra.modelo.pessoa.Pessoa;
import Palestra.modelo.pessoa.Visitante;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

// Concentra as regras de venda, entrada e cadastro que antes ficavam no Main
public class Bilheteria {

    private Evento evento;
    private Arquivo arquivo = new Arquivo();
    private ArrayList<Ingresso> ingressos = new ArrayList<>();
    private HashMap<String, Credencial> credenciais = new HashMap<>(); // código -> credencial
    private ArrayList<Visitante> visitantes = new ArrayList<>();

    public Bilheteria(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Bilheteria exige um evento.");
        }
        this.evento = evento;
    }

    // Recupera o que foi salvo nas execuções anteriores:
    // ingressos e credenciais, vagas ocupadas, entradas já feitas e visitantes.
    public void carregar() throws IOException {
        // Conclui todas as leituras antes de alterar listas ou vagas.
        Set<String> entradasSalvas = arquivo.carregarEntradas();
        List<Visitante> visitantesSalvos = arquivo.carregarVisitantes();
        List<Ingresso> comprasSalvas = arquivo.carregarCompras(evento);
        for (Ingresso ingresso : comprasSalvas) {
            if (jaRegistrado(ingresso)) {
                continue;
            }
            try {
                ingresso.getSessao().ocuparVaga(); // recalcula as vagas
                adicionar(ingresso);
            } catch (IllegalStateException e) {
                System.out.println("Aviso: ingresso " + ingresso.getCodigo() + " ignorado (sessão lotada).");
            }
        }
        for (String codigo : entradasSalvas) {
            Credencial credencial = credenciais.get(codigo);
            if (credencial != null) {
                credencial.registrarEntrada(); // credencial volta como UTILIZADA
            }
        }
        for (Visitante visitante : visitantesSalvos) {
            if (!visitanteCadastrado(visitante)) {
                visitantes.add(visitante);
            }
        }
    }

    // Cria um ingresso ainda não pago
    public Ingresso reservar(Pessoa pessoa, Sessao sessao) {
        if (pessoa == null || sessao == null) {
            throw new IllegalArgumentException("Pessoa e sessão são obrigatórias.");
        }
        if (evento.numeroDa(sessao) == 0) {
            throw new IllegalArgumentException("A sessão não pertence ao evento.");
        }
        if (!pessoa.podeAssistirPalestra()) {
            throw new IllegalArgumentException(pessoa.getTipo() + " não compra ingresso para palestras.");
        }
        if (!pessoa.comprovarTipo()) {
            throw new IllegalArgumentException("Categoria " + pessoa.getTipo() + " não comprovada.");
        }
        if (sessao.getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Sessão lotada.");
        }
        Ingresso ingresso = new Ingresso(pessoa, sessao);
        return ingresso;
    }

    // Grava a compra (NIO) e só depois ocupa a vaga e guarda a credencial.
    // Se a gravação falhar, nada muda e pode-se tentar de novo sem cobrar outra vez.
    public void registrar(Ingresso ingresso) throws IOException {
        if (ingresso == null || !ingresso.isPago()) {
            throw new IllegalStateException("O ingresso precisa estar pago para ser registrado.");
        }
        if (jaRegistrado(ingresso)) {
            return;
        }
        Sessao sessao = ingresso.getSessao();
        if (evento.numeroDa(sessao) == 0) {
            throw new IllegalArgumentException("A sessão não pertence ao evento.");
        }
        if (sessao.getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Sessão lotada.");
        }
        if (!ingresso.getComprador().podeAssistirPalestra() || !ingresso.getComprador().comprovarTipo()) {
            throw new IllegalArgumentException("Categoria não permitida para a compra.");
        }
        arquivo.salvarCompra(ingresso, sessao.getId());
        ingresso.getSessao().ocuparVaga();
        adicionar(ingresso);
    }

    private boolean jaRegistrado(Ingresso ingresso) {
        if (credenciais.containsKey(ingresso.getCredencial().getCodigo())) {
            return true;
        }
        for (Ingresso existente : ingressos) {
            if (existente.getCodigo().equals(ingresso.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    private void adicionar(Ingresso ingresso) {
        ingressos.add(ingresso);
        Credencial credencial = ingresso.getCredencial();
        credenciais.put(credencial.getCodigo(), credencial);
    }

    // Valida a entrada na palestra e, se liberada, grava para não poder reutilizar depois
    public boolean validarEntrada(Credencial credencial, Sessao sessao) throws IOException {
        if (credencial == null || sessao == null || evento.numeroDa(sessao) == 0
                || credenciais.get(credencial.getCodigo()) != credencial) {
            System.out.println("Acesso negado: credencial ou sessão não cadastrada.");
            return false;
        }
        boolean liberada = sessao.getPalestra().validarCredencial(credencial, sessao);
        if (liberada) {
            arquivo.salvarEntrada(credencial);
            credencial.registrarEntrada();
            System.out.println("Entrada liberada: " + credencial.getTitular().getNome());
        }
        return liberada;
    }

    public void cadastrarVisitante(Visitante visitante) throws IOException {
        if (visitanteCadastrado(visitante)) {
            return;
        }
        arquivo.salvarVisitante(visitante);
        visitantes.add(visitante);
    }

    private boolean visitanteCadastrado(Visitante visitante) {
        for (Visitante existente : visitantes) {
            if (existente.getCpf().equals(visitante.getCpf())) {
                return true;
            }
        }
        return false;
    }

    public Credencial buscarCredencial(String codigo) {
        if (codigo == null) {
            return null;
        }
        return credenciais.get(codigo.trim().toUpperCase());
    }

    public void listarHistorico() throws IOException {
        arquivo.listarCompras();
    }

    public void listarIngressosVendidos() {
        if (ingressos.isEmpty()) {
            System.out.println("Nenhum ingresso vendido.");
            return;
        }
        for (Ingresso ingresso : ingressos) {
            ingresso.exibirResumo();

            String status;
            if (ingresso.getCredencial().isUtilizada()) {
                status = " (UTILIZADA)";
            } else {
                status = " (VÁLIDA)";
            }
            System.out.println("Credencial: " + ingresso.getCredencial().getCodigo() + status);
            System.out.println("--------------------");
        }
    }

    public void listarVisitantes() {
        if (visitantes.isEmpty()) {
            System.out.println("Nenhum visitante cadastrado.");
            return;
        }
        for (Visitante v : visitantes) {
            System.out.println(v.getNome() + " | CPF " + v.getCpf() + " | " + v.getIdade() + " anos");
        }
    }

    public boolean temCredenciais() {
        return !credenciais.isEmpty();
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public List<Visitante> getVisitantes() {
        return visitantes;
    }
}

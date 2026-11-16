package Palestra.menu;

import Palestra.formulario.FormularioPagamento;
import Palestra.formulario.FormularioPessoa;
import Palestra.leitor.Leitor;
import Palestra.modelo.palestra.Credencial;
import Palestra.modelo.palestra.Ingresso;
import Palestra.modelo.palestra.Sessao;
import Palestra.modelo.pessoa.Pessoa;
import Palestra.modelo.pessoa.Visitante;
import Palestra.pagamento.Pagamento;
import Palestra.sistema.Bilheteria;
import Palestra.sistema.Evento;

import java.io.IOException;
import java.util.NoSuchElementException;

// Telas do sistema: conversa com o usuário e delega as regras para Bilheteria e Evento
public class Menu {

    private Evento evento;
    private Bilheteria bilheteria;
    private boolean registroPendente = false;

    public Menu(Evento evento, Bilheteria bilheteria) {
        this.evento = evento;
        this.bilheteria = bilheteria;
    }
    public void executar() {
        try {
            boolean executando = true;
            while (executando) {
                System.out.println("\n=== " + evento.getNome() + " ===");
                System.out.println("1 - Comprar ingresso | 2 - Histórico de compras | 3 - Validar credencial"
                        + " | 4 - Ingressos vendidos | 5 - Visitantes | 0 - Sair");
                switch (Leitor.texto("Opção: ")) {
                    case "0": executando = false; break;
                    case "1": comprarIngresso(); break;
                    case "2": mostrarHistorico(); break;
                    case "3": validarCredencial(); break;
                    case "4": bilheteria.listarIngressosVendidos(); break;
                    case "5": bilheteria.listarVisitantes(); break;
                    default: System.out.println("Opção inválida."); break;
                }
            }
        } catch (NoSuchElementException e) {
            if (registroPendente) {
                System.out.println("Entrada encerrada com pagamento aprovado e registro pendente. A compra não foi concluída.");
            } else {
                System.out.println("Entrada encerrada.");
            }
        } finally {
            Leitor.fechar();
        }
    }
    private void comprarIngresso() {
        Pessoa pessoa = FormularioPessoa.ler();
        if (pessoa instanceof Visitante) {
            cadastrarVisitante((Visitante) pessoa);
            return;
        }
        Ingresso ingresso = escolherSessao(pessoa);
        if (ingresso == null || !pagar(ingresso)) {
            System.out.println("Compra cancelada.");
            return;
        }
        registrar(ingresso);
        System.out.println("Compra do ingresso " + ingresso.getCodigo() + " concluída.");
        ingresso.getCredencial().exibirCredencial();
        System.out.println("Guarde o código da credencial para entrar na palestra.");
    }

    private void cadastrarVisitante(Visitante visitante) {
        while (true) {
            try {
                bilheteria.cadastrarVisitante(visitante);
                System.out.println("Visitante cadastrado. Entrada gratuita no evento, sem acesso às palestras.");
                return;
            } catch (IOException e) {
                System.out.println("Não foi possível salvar o visitante: " + e.getMessage());
                if (Leitor.texto("Enter - Tentar salvar novamente | 0 - Cancelar cadastro: ").equals("0")) {
                    System.out.println("Cadastro cancelado.");
                    return;
                }
            }
        }
    }

    // Devolve o ingresso reservado, ou null se o usuário cancelar
    private Ingresso escolherSessao(Pessoa pessoa) {
        while (true) {
            System.out.println("Escolha a sessão:");
            evento.listarSessoes();
            int escolha = Leitor.inteiro("Sessão (0 - Cancelar compra): ", 0, evento.quantidadeSessoes());
            if (escolha == 0) {
                return null;
            }
            try {
                return bilheteria.reservar(pessoa, evento.getSessao(escolha));
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage()); // sessão lotada: escolhe outra
            }
        }
    }

    // Devolve false se o usuário cancelar
    private boolean pagar(Ingresso ingresso) {
        ingresso.exibirResumo();
        while (!ingresso.isPago()) {
            Pagamento forma = FormularioPagamento.ler();
            if (forma == null) {
                return false;
            }
            if (!ingresso.pagar(forma)) {
                System.out.println("Escolha outra forma de pagamento ou cancele.");
            }
        }
        return true;
    }

    // Após aprovação, repete somente a gravação: nunca cobra novamente
    private void registrar(Ingresso ingresso) {
        registroPendente = true;
        while (registroPendente) {
            try {
                bilheteria.registrar(ingresso);
                registroPendente = false;
            } catch (IOException e) {
                System.out.println("Pagamento aprovado, registro pendente: " + e.getMessage());
                System.out.println("Corrija o acesso ao arquivo e pressione Enter para tentar salvar novamente.");
                Leitor.darEnter();
            }
        }
    }

    private void mostrarHistorico() {
        while (true) {
            try {
                bilheteria.listarHistorico();
                return;
            } catch (IOException e) {
                System.out.println("Erro ao ler histórico: " + e.getMessage());
                if (Leitor.texto("Enter - Tentar novamente | 0 - Voltar: ").equals("0")) {
                    return;
                }
            }
        }
    }

    private void validarCredencial() {
        if (!bilheteria.temCredenciais()) {
            System.out.println("Nenhuma credencial cadastrada.");
            return;
        }
        Credencial credencial = null;
        while (credencial == null) {
            String codigo = Leitor.texto("Código da credencial (0 - Voltar): ");
            if (codigo.equals("0")) {
                return;
            }
            credencial = bilheteria.buscarCredencial(codigo);
            if (credencial == null) {
                System.out.println("Credencial inexistente.");
            } else if (credencial.isUtilizada()) {
                System.out.println("Acesso negado: credencial já utilizada. Informe outra credencial.");
                credencial = null;
            } else if (!credencial.isAtiva()) {
                System.out.println("Acesso negado: ingresso não pago. Informe outra credencial.");
                credencial = null;
            }
        }
        System.out.println("Credencial válida");
        evento.listarSessoes();
        while (true) {
            int escolha = Leitor.inteiro("Sessão de entrada (0 - Voltar): ", 0, evento.quantidadeSessoes());
            if (escolha == 0) {
                return;
            }
            Sessao sessao = evento.getSessao(escolha);
            while (true) {
                try {
                    if (bilheteria.validarEntrada(credencial, sessao)) {
                        return;
                    }
                    break; // Sessão incorreta: preserva a credencial e repete a escolha.
                } catch (IOException e) {
                    System.out.println("Entrada pendente: não foi possível registrá-la: " + e.getMessage());
                    if (Leitor.texto("Enter - Tentar registrar novamente | 0 - Voltar: ").equals("0")) {
                        return;
                    }
                }
            }
        }
    }
}

package main;

import modelo.palestra.Palestra;
import modelo.palestra.Sala;
import modelo.palestra.Sessao;
import modelo.pessoa.Credencial;
import modelo.pessoa.Estudante;
import modelo.pessoa.Idoso;
import modelo.pessoa.Palestrante;
import modelo.pessoa.Pcd;
import modelo.pessoa.Pessoa;
import modelo.pessoa.Visitante;
import pagamento.Cartao;
import pagamento.CartaoCredito;
import pagamento.CartaoDebito;
import pagamento.Pagamento;
import pagamento.PagamentoCartao;
import pagamento.PagamentoDinheiro;
import pagamento.PagamentoPix;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Arquivo arquivo = new Arquivo();
        ArrayList<Credencial> credenciais = new ArrayList<>();
        // Dados demonstrativos da programação.
        Palestrante palestrante = new Palestrante();
        palestrante.cadastrar("Palestrante de exemplo", "00000000000", 30, 1);
        Palestra palestra = new Palestra("Introdução à POO", "50 minutos",
                "Classes, objetos e interfaces", palestrante);
        Sala sala = new Sala(10, 80);
        Sessao[] sessoes = {new Sessao(palestra, sala, "14h"), new Sessao(palestra, sala, "19h")};
        float valorOriginal = 50.0f;
        boolean pagamentoSemRegistro = false;

        try {
            boolean executando = true;
            while (executando) {
                System.out.println("\n1 - Cadastrar participante | 2 - Histórico de compras | 3 - Validar credencial | 0 - Sair");
                String acao = entrada.nextLine().trim();
                switch (acao) {
                    case "0":
                        executando = false;
                        break;
                    case "1": {
                        String nome;
                        do {
                            System.out.print("Nome: ");
                            nome = entrada.nextLine().trim();
                            if (!Pessoa.nomeValido(nome)) System.out.println("Nome inválido.");
                        } while (!Pessoa.nomeValido(nome));
                        String cpf;
                        do {
                            System.out.print("CPF (11 dígitos): ");
                            cpf = entrada.nextLine().trim();
                            if (!Pessoa.cpfValido(cpf)) System.out.println("CPF deve conter 11 dígitos.");
                        } while (!Pessoa.cpfValido(cpf));

                        int idade = -1;
                        while (idade < 0) {
                            System.out.print("Idade: ");
                            try {
                                idade = Integer.parseInt(entrada.nextLine().trim());
                                if (idade < 0) System.out.println("Idade não pode ser negativa.");
                            } catch (NumberFormatException e) {
                                System.out.println("Digite uma idade inteira.");
                            }
                        }

                        Pessoa pessoa = null;
                        while (pessoa == null) {
                            System.out.println("1 - Comum | 2 - Estudante | 3 - Idoso | 4 - PCD | 5 - Visitante");
                            try {
                                int modalidade = Integer.parseInt(entrada.nextLine().trim());
                                Pessoa candidato;
                                switch (modalidade) {
                                    case 1: candidato = new Pessoa(); break;
                                    case 2: candidato = new Estudante(); break;
                                    case 3: candidato = new Idoso(); break;
                                    case 4: candidato = new Pcd(); break;
                                    case 5: candidato = new Visitante(); break;
                                    default: throw new IllegalArgumentException("Modalidade inválida.");
                                }
                                candidato.cadastrar(nome, cpf, idade, modalidade);
                                pessoa = candidato;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Revise a modalidade: " + e.getMessage());
                            }
                        }
                        if (pessoa instanceof Visitante) {
                            System.out.println("Visitante cadastrado. Sem cobrança e sem acesso às palestras.");
                            break;
                        }

                        Sessao sessao = null;
                        boolean cancelar = false;
                        while (sessao == null && !cancelar) {
                            System.out.println("Escolha a sessão (0 - Cancelar compra):");
                            for (int i = 0; i < sessoes.length; i++) {
                                System.out.println((i + 1) + " - " + sessoes[i].getPalestra().getNome()
                                        + " | " + sessoes[i].getHorario() + " | Vagas: " + sessoes[i].getVagasDisponiveis());
                            }
                            try {
                                int escolha = Integer.parseInt(entrada.nextLine().trim());
                                if (escolha == 0) cancelar = true;
                                else if (escolha < 1 || escolha > sessoes.length) System.out.println("Sessão inválida.");
                                else if (sessoes[escolha - 1].getVagasDisponiveis() <= 0) System.out.println("Sessão lotada.");
                                else sessao = sessoes[escolha - 1];
                            } catch (NumberFormatException e) {
                                System.out.println("Digite o número da sessão.");
                            }
                        }
                        if (cancelar) break;

                        int desconto = pessoa.isExcepcionalidade() ? 50 : 0;
                        float valorPago = valorOriginal * (100 - desconto) / 100;
                        System.out.printf("Total: R$ %.2f (%d%% de desconto)%n", valorPago, desconto);
                        boolean aprovado = false;
                        String tipoPagamento = "";
                        while (!aprovado && !cancelar) {
                            System.out.println("1 - Pix | 2 - Dinheiro | 3 - Crédito | 4 - Débito | 0 - Cancelar compra");
                            String opcao = entrada.nextLine().trim();
                            Pagamento pagamento = null;
                            switch (opcao) {
                                case "0": cancelar = true; break;
                                case "1": {
                                    String chave;
                                    do {
                                        System.out.print("Chave Pix fictícia: ");
                                        chave = entrada.nextLine().trim();
                                        if (chave.isEmpty()) System.out.println("Informe a chave Pix.");
                                    } while (chave.isEmpty());
                                    pagamento = new PagamentoPix(chave);
                                    tipoPagamento = "PIX";
                                    break;
                                }
                                case "2":
                                    pagamento = new PagamentoDinheiro();
                                    tipoPagamento = "DINHEIRO";
                                    break;
                                case "3":
                                case "4": {
                                    String numero;
                                    do {
                                        System.out.print("Número fictício do cartão: ");
                                        numero = entrada.nextLine().trim();
                                        if (!Cartao.numeroValido(numero)) System.out.println("Informe 16 dígitos.");
                                    } while (!Cartao.numeroValido(numero));
                                    String validade;
                                    do {
                                        System.out.print("Validade (MM/AAAA): ");
                                        validade = entrada.nextLine().trim();
                                        if (!Cartao.validadeValida(validade)) System.out.println("Validade inválida ou vencida.");
                                    } while (!Cartao.validadeValida(validade));
                                    String cvv;
                                    do {
                                        System.out.print("CVV fictício: ");
                                        cvv = entrada.nextLine().trim();
                                        if (!Cartao.cvvValido(cvv)) System.out.println("Informe 3 dígitos.");
                                    } while (!Cartao.cvvValido(cvv));
                                    if (opcao.equals("3")) {
                                        pagamento = new PagamentoCartao(new CartaoCredito(numero, validade, cvv));
                                        tipoPagamento = "CARTAO_CREDITO";
                                    } else {
                                        pagamento = new PagamentoCartao(new CartaoDebito(numero, cvv, validade));
                                        tipoPagamento = "CARTAO_DEBITO";
                                    }
                                    break;
                                }
                                default: System.out.println("Forma de pagamento inválida."); break;
                            }
                            if (pagamento != null) {
                                aprovado = pagamento.pagar(valorPago);
                                if (!aprovado) System.out.println("Pagamento recusado. Escolha outra forma ou cancele.");
                            }
                        }
                        if (cancelar) break;

                        // Após aprovação, repetir somente a gravação: nunca cobrar novamente.
                        pagamentoSemRegistro = true;
                        boolean registrado = false;
                        while (!registrado) {
                            try {
                                arquivo.salvarCompra(pessoa.getNomePessoa(), pessoa.getCategoria(),
                                        tipoPagamento, valorOriginal, desconto, valorPago);
                                registrado = true;
                                pagamentoSemRegistro = false;
                            } catch (IOException e) {
                                System.out.println("Pagamento aprovado, registro pendente: " + e.getMessage());
                                System.out.println("Corrija o acesso ao arquivo e pressione Enter para tentar salvar novamente.");
                                entrada.nextLine();
                            }
                        }
                        sessao.ocuparVaga();
                        Credencial credencial = new Credencial(pessoa, sessao);
                        credencial.ativar();
                        credenciais.add(credencial);
                        System.out.println("Compra registrada. Credencial " + credenciais.size() + " ativa para "
                                + pessoa.getNomePessoa() + " às " + sessao.getHorario() + ".");
                        break;
                    }
                    case "2": {
                        boolean concluido = false;
                        while (!concluido) {
                            try {
                                arquivo.listarCompras();
                                concluido = true;
                            } catch (IOException e) {
                                System.out.println("Erro ao ler histórico: " + e.getMessage());
                                System.out.println("Enter - Tentar novamente | 0 - Voltar");
                                concluido = entrada.nextLine().trim().equals("0");
                            }
                        }
                        break;
                    }
                    case "3": {
                        if (credenciais.isEmpty()) {
                            System.out.println("Nenhuma credencial emitida nesta execução.");
                            break;
                        }
                        int numero = 0;
                        boolean cancelar = false;
                        while (numero == 0 && !cancelar) {
                            System.out.print("Número da credencial (0 - Voltar): ");
                            try {
                                int escolha = Integer.parseInt(entrada.nextLine().trim());
                                if (escolha == 0) cancelar = true;
                                else if (escolha < 1 || escolha > credenciais.size()) System.out.println("Credencial inexistente.");
                                else numero = escolha;
                            } catch (NumberFormatException e) {
                                System.out.println("Digite o número da credencial.");
                            }
                        }
                        if (cancelar) break;
                        boolean liberado = false;
                        while (!liberado && !cancelar) {
                            System.out.print("Número da sessão (1 - 14h | 2 - 19h | 0 - Voltar): ");
                            try {
                                int escolha = Integer.parseInt(entrada.nextLine().trim());
                                if (escolha == 0) cancelar = true;
                                else if (escolha < 1 || escolha > sessoes.length) System.out.println("Sessão inexistente.");
                                else {
                                    Sessao sessao = sessoes[escolha - 1];
                                    liberado = sessao.getPalestra().validarCredencial(credenciais.get(numero - 1), sessao);
                                    System.out.println(liberado ? "Entrada liberada." : "Entrada negada. Revise a sessão ou volte.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Digite o número da sessão.");
                            }
                        }
                        break;
                    }
                    default: System.out.println("Opção inválida."); break;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println(pagamentoSemRegistro
                    ? "Entrada encerrada com pagamento aprovado e registro pendente. A compra não foi concluída."
                    : "Entrada encerrada.");
        } finally {
            entrada.close();
        }
    }
}


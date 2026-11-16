# Projeto Palestra
Augusto Leite Del Carlo Carneiro - GES - 866
Felipe Lamy Rosa Renno - GES - 507
Vitor Augusto do Couto ES 536

Sistema de bilheteria de eventos acadêmicos feito em Java para a disciplina C06 — Programação Orientada a Objetos (NP2). O projeto roda no terminal e inclui uma programação demonstrativa com palestras, salas e horários.

## O que o sistema faz

- Cadastra pessoas de 18 a 120 anos. Estudante, idoso e PCD têm 50% de desconto quando a categoria é comprovada. Visitantes entram gratuitamente no evento e são cadastrados sem ingresso para palestras.
- Permite escolher uma sessão com vagas e comprar um ingresso com pagamento simulado por Pix, dinheiro, cartão de crédito ou de débito.
- Emite uma credencial após a compra, confere a sessão escolhida na entrada e impede a reutilização da credencial.
- Exibe o histórico de compras, os ingressos vendidos e os visitantes cadastrados.

## Como executar

Use um JDK 17 ou mais recente. No IntelliJ IDEA, abra a raiz do repositório, configure o JDK do projeto e execute `src/Main.java`. Use a raiz do repositório como diretório de trabalho.

Se preferir o PowerShell, execute na raiz do projeto:

```powershell
$fontes = (Get-ChildItem src -Recurse -Filter *.java).FullName
javac -encoding UTF-8 -d out $fontes
java -cp out Main
```

O programa usa os arquivos `compras.txt`, `entradas.txt` e `visitantes.txt` no diretório de trabalho. Os registros são gravados quando usados e recuperados na próxima execução. Use dados fictícios na demonstração. O pagamento é simulado, e o programa registra a forma de pagamento usada sem gravar as informações do cartão.

## Orientação a objetos

| Conceito | Exemplo no projeto |
| --- | --- |
| Herança e classes abstratas | `Pessoa` reúne o comportamento comum das categorias. `Cartao` é a base para `CartaoCredito` e `CartaoDebito`. |
| Polimorfismo | `Ingresso` chama `getPercentualDesconto()` pela referência `Pessoa`. A categoria concreta define o percentual aplicado. |
| Interface | `Pagamento` define o contrato usado por `Ingresso`. Pix, dinheiro e cartão têm implementações próprias. |
| Associação e composição | `Sessao` referencia uma `Palestra` e uma `Sala`. Cada `Ingresso` cria sua `Credencial`. |
| Encapsulamento e tratamento de erros | `Bilheteria` aplica as regras de reserva, registro e entrada. O menu trata falhas de arquivo e oferece nova tentativa nas etapas correspondentes. |

Em [Menu.java](src/Palestra/menu/Menu.java#L65), o menu usa a chamada `podeComprarIngresso()` para separar o cadastro gratuito da compra. `Visitante` sobrescreve esse método e devolve `false`. A [Bilheteria](src/Palestra/sistema/Bilheteria.java#L70) também verifica a permissão para assistir à palestra ao reservar o ingresso, usando o método `podeAssistirPalestra()`.

## Diagrama UML

O UML de Victor foi revisado com base nas classes, métodos e relações da versão publicada.

- [Diagrama completo em PDF](docs/UML_ProjetoPalestra_Victor_Corrigido.pdf)
- [Arquivo editável no Draw.io](docs/UML_ProjetoPalestra_Victor_Corrigido.drawio)
- [PDF de referência enviado com a versão da equipe](UML_ProjetoPalestra_Aula.pdf)

## Declaração de uso de IA

**Sim, houve uso de inteligência artificial.** Usamos o **Codex, da OpenAI**, para tirar dúvidas de POO, discutir a organização das classes e dos pagamentos, revisar e ajustar o código, testar os fluxos de cadastro, compra, gravação e credenciais, comparar e corrigir o UML e preparar a documentação. Algumas sugestões de código e correções foram implementadas com esse auxílio. Também usamos o **Copilot** para criar um rascunho de UML, que foi conferido com o código Java.

Felipe também usou **Claude, da Anthropic**, para esclarecer dúvidas de Java e Git e avaliar ajustes no menu e na organização da `Main`. O [relato dessa etapa](docs/USO_IA_CLAUDE.md) traz a versão da ferramenta e as dúvidas discutidas.

A [transcrição da conversa com o Codex](docs/CONVERSA_COM_IA.md) apresenta as perguntas, decisões e respostas ao longo do trabalho. Caminhos locais e identificadores pessoais foram ocultados na cópia publicada.

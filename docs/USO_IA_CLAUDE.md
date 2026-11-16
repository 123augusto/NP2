# Uso de Inteligência Artificial no Projeto PalestraPOO (NP2)

> Relato de Felipe sobre uma etapa de desenvolvimento. A descrição de problemas de compilação se refere ao código examinado naquela etapa. A versão final já declara `podeComprarIngresso()` em `Pessoa` e chama `cadastrarVisitante` com o argumento `Visitante`.

Este documento registra como foi usada uma ferramenta de Inteligência Artificial no desenvolvimento do projeto final de Programação Orientada a Objetos.

## Ferramenta utilizada

| Item | Informação |
|---|---|
| IA | Claude, da Anthropic |
| Modelo / versão | Claude Opus 5.5 (`claude-opus-5-5`) |
| Plataforma | claude.ai (chat), dentro de um Projeto com os slides da disciplina |
| Data de uso | 16/11/2026 |
| Quem usou | Felipe |

## Como a IA foi usada

A IA foi usada como **apoio para tirar dúvidas**. Ela não gerou o projeto. O sistema (classes, regras de negócio, menus, persistência em arquivo) já existia e foi escrito pelo grupo. A IA explicou conceitos, revisou trechos do código e sugeriu alternativas, que foram analisadas antes de serem aplicadas.

## Dúvidas tiradas


### 1. Trocar o `switch` por `if / else if / else` em `Menu.executar()`
- **O que foi aplicado:** a opção digitada passou a ser guardada em uma variável (`String opcao`) e comparada com `.equals()`.
- **Explicação recebida:** em Java, `==` com `String` compara referências e não o conteúdo. Os `break` só são necessários no `switch`.


### 2. Como fazer commit no repositório
- **Explicação recebida:** fluxo `git pull` → `git add` → `git commit -m "..."` → `git push`. Também foi explicada a necessidade de ser colaborador do repositório e de usar um token de acesso no GitHub.

### 3. Revisão da classe `Menu`
- **Problema apontado:** a classe não compilava. Ela chamava `podeComprarIngresso()`, que não existe em `Pessoa`, e `cadastrarVisitante()` sem o argumento obrigatório.
- **Correção aplicada:** a mesma descrita no item 1.

### 4. Duvidas sobre como enxutar mais a main
- **Problema apontado:** a classe main continha mais de 240 linhas de codigo, com muita logica. Com isso, perguntamos para IA como nos poderiamos enxutar mais e transformar o projeto mais a cara de POO. A ideia foi a criacao de classes, Menu, FormularioPagamento, FormularioPessoa/


## Conteúdos da disciplina relacionados

- Modificadores de acesso / encapsulamento (Aula 07)
- Construtores e `static` (Aula 08)
- Herança e polimorfismo (Aula 09)
- Classes abstratas (Aula 10)
- Exceções: `try / catch / finally` (Aula 13)

## Responsabilidade

Todo o código entregue foi lido, entendido e testado pelo grupo. As sugestões da IA foram usadas como material de estudo e revisão, e as decisões finais sobre o código são do grupo.

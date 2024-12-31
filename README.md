# Controle de Vendas

## Descrição
O **Controle de Vendas** é uma aplicação desenvolvida em Java para gerenciar processos de vendas. O sistema permite o cadastro e consulta de informações essenciais, como **Clientes**, **Produtos**, **Vendas** e **Itens de Venda**, oferecendo uma interface simples e eficiente para o usuário.

## Funcionalidades
### Cadastro
O sistema possui um menu de cadastro para gerenciar as seguintes entidades:
- **Cliente**: Registro e manutenção de informações sobre os clientes.
- **Produto**: Cadastro de produtos disponíveis para venda.
- **Venda**: Registro de vendas, associando clientes, produtos e itens de venda.

### Consulta
A aplicação também permite consultar informações específicas:
- **Venda por Cliente**: Visualizar todas as vendas realizadas por um cliente.
- **Venda por Produto**: Listar vendas associadas a um produto específico.

## Tecnologias Utilizadas
- **Java**: Linguagem principal utilizada para o desenvolvimento.
- **Swing**: Biblioteca gráfica para construção da interface do usuário.
- **Programação Orientada a Objetos (POO)**: Código estruturado seguindo boas práticas de encapsulamento, herança e polimorfismo.
- **PostgreSQL**: Gerenciamento e armazenamento das informações.

## Requisitos para Execução
1. Ter o **Java JDK** instalado (versão 8 ou superior).
2. Clonar o repositório ou baixar os arquivos do projeto.
3. Compilar e executar o projeto em uma IDE (como **Eclipse** ou **IntelliJ IDEA**) ou utilizar o **javac** no terminal.
4. Possuir um server postgreSQL instalado e configurado na máquina.

## Como Utilizar
1. Dentro de com/mcompany/controlevenda/constants, existe o arquivo "DatabaseConstants.java". Altere cada constant com os seus dados para conexão a base de dados.
2. Execute a aplicação.
3. Na tela principal, utilize o menu para selecionar a funcionalidade desejada:
   - **Cadastro**: Escolha entre Cliente, Produto ou Venda.
   - **Consulta**: Selecione entre Venda por Cliente ou Venda por Produto.
4. Preencha os campos obrigatórios e realize as operações conforme necessário.

## Estrutura do Projeto
O projeto é dividido nas seguintes entidades principais:
- **Cliente**: Dados relacionados aos clientes, como nome e contato.
- **Produto**: Informações sobre produtos disponíveis para venda.
- **Venda**: Gerenciamento de vendas realizadas, associando clientes e produtos.
- **ItemVenda**: Detalhamento dos itens contidos em uma venda.

## Melhorias Futuras
- **Persistência de Dados**: Integração com um banco de dados para armazenar informações de forma persistente.
- **Relatórios**: Implementação de geração de relatórios para análise detalhada de vendas.
- **Validações**: Aprimorar a validação de entradas nos formulários.
- **Interface Gráfica**: Modernizar o design da interface para torná-la mais amigável e responsiva.
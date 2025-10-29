# Biblioteca RMI

Este é um projeto acadêmico desenvovlido para a disciplina Desenvolvimento de Aplicações Móveis e Distribuídas. Focado no estudo e aplicação de Java RMI (Remote Method Invocation).

O sistema simula o back-end de uma biblioteca simples com um servidor que gerencia os livros e um cliente de terminal que pode interagir remotamente para adicionar consultar, emprestar e devolver livros.

## Estrutura do Projeto

O código-fonte está separado em dois módulos, cliente e servidor.

```
.
├──  Client/                  <-- 1. Código do Cliente
│   └──  RMI/
│       └──  BibliotecaClient.java  (Cliente)
│       ├──  Biblioteca.java        (A Interface remota)
│       └──  Livro.java             (O Objeto serializável)
│
├──  Server/                  <-- 2. Código do Servidor
│   └──  RMI/
│       ├──  BibliotecaImpl.java    (Implementação da interface)
│       └──  BibliotecaServer.java  (Servidor)
│       ├──  Biblioteca.java        (A Interface remota)
│       └──  Livro.java             (O Objeto serializável)
│
├──  start_client.sh                (Script para Execução do Cliente)
└──  start_server.sh                (Script para Execução do Servidor)
```

## Pré-requisitos

1. **JDK (Java Development Kit):** Necessário para a compilação (javac) e execução (java) do projeto.
2. **Ambiente**: Os scripts de execução (.sh) são feitos para um terminal shell.
    - Windows: É recomendado o uso do WSL para rodar o projeto.

Para verificar se você tem o Java e o compilador, abra um terminal e digite:

```bash
java -version
javac -version
```

## Guia de Execução (Linux / macOS / WSL)

Você precisa de dois terminais abertos na raíz desse projeto e conectados na mesma rede.

### 1. Dar permissão aos scripts

(Execute este passo apenas uma vez)

No terminal que atuará como servidor, dê permissão para a execução do script start_server.sh

```bash
chmod +x start_server.sh
```

No terminal que atuará como cliente, dê permissão para a execução do script start_client.sh

```bash
chmod +x start_client.sh
```

### 2. Rodar o Servidor

No terminal que atuará como servidor, execute o script:

```bash
./start_server.sh
```

O scripot irá:
1. Compilar o código-fonte necessário.
2. Encontrar o IP da sua máquina na rede local
3. Iniciar o servidor RMI, que ficará aguardando conexões.

### 3. Rodar o Cliente

No terminal que atuará como cliente, execute o script:

```bash
./start_client.sh
```

O script irá:
1. Compilar o código-fonte necessário.
2. Pedir que você digite o IP do servidor (utilize o IP que estará informado na execução do servidor)
3. Conectar ao servidor e exibir o menu interativo da biblioteca. 

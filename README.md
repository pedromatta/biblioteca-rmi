# Biblioteca RMI

Este é um projeto acadêmico desenvovlido para a disciplina Desenvolvimento de Aplicações Móveis e Distribuídas. Focado no estudo e aplicação de Java RMI (Remote Method Invocation).

O sistema simula o back-end de uma biblioteca simples com um servidor que gerencia os livros e um cliente de terminal que pode interagir remotamente para adicionar consultar, emprestar e devolver livros.

[RMI](#rmi)
- [RMI vs RPC](#rmi-vs-rpc)
- [Componentes do RMI](#componentes-do-rmi)

[Estrutura do Projeto](#estrutura-do-projeto) 
- [Aplicação do RMI no Projeto](#aplicação-do-rmi-no-projeto)

[Pré-requisitos](#pré-requisitos)

[Gua de Execução](#guia-de-execução-linux--macos--git-bash)

## RMI

O RMI (Remote Method Invocation) é um mecanismo da plataforma Java que permite que um objeto em uma JVM invoque métodos de um objeto que está em outra JVM, como se ambos estivessem no mesmo local.

Ele abstrai toda a complexidade da comunicação em rede (como sockets, serialização e gerenciamento de conexões), fazendo com que uma chamada remota pareça uma chamada de método local.

### RMI vs RPC

O RMI é, essencialmente, a implementação orientada a objetos do Java para o conceito de RPC.
- RPC é o paradigma de comunicação entre processos que permite a execução de um procedimento ou função em um espaço de endereço remoto.
- RMI aplica esse paradigm ao modelo de objetos do Java, permitindo a invocação de um método em um objeto remoto. Ele é uma solução Java-para-Java que preserva o comportamento oriendado a objetos, ocmo a passagem de objetos serializáveis como parâmetros.

### Componentes do RMI

O RMI funciona com base em alguns componentes-chave:

- Interface Remota (`java.rmi.Remote`): Define a assinatura dos métodos que estarão disponíveis para invocação remota. Ela serve como o contrato de serviço entre o cliente e o servidor. Todo método deve declarar que lança `java.rmi.RemoteException`.
- Objeto Remoto (U`nicastRemoteObject`): A classe no lado do servidor que implementa a interface remota. Ela contém a lógica de negócios real que será executada. Ao estender `UnicastRemoteObject`, a classe ganha a capacidade de ser exportada e receber chamadas remotas.
- RMI Registry: Um serviço de nomes (nameserver) que atua como um diretório. O servidor registra (bind) seus objetos remotos no Registry usando um nome único.
- Stub: Um objeto proxy gerado pelo RMI que implementa a mesma interface remota. O cliente obtém uma referência a esse Stub ao consultar o Registry. O Stup é rsponsável por serializar os argumenos do método (marshallling) e enviá-los ao servidor.
- Skeleton: A contraparte do Stub no servidor. É responsável por receber a solicitação de rede, deserializar os argumentos (unmarshalling) e invocar o método correspondente no objeto remoto real.
- Serialização (`java.io.Serializable`): O Processo pelo qual os parâmetros e valores de retorno (objetos) são convertidos em um fluxo de bytes para transmissão pela rede e, posteriormente, reconstruídos no destino.

## Estrutura do Projeto

O código-fonte está separado em dois módulos, cliente e servidor.

```
.
├── Client/                  <-- 1. Código do Cliente
│   └── RMI/
│       └── BibliotecaClient.java  (Cliente)
│       ├── Biblioteca.java        (A Interface remota)
│       └── Livro.java             (O Objeto serializável)
│
├── Server/                  <-- 2. Código do Servidor
│   └── RMI/
│       ├── BibliotecaImpl.java    (Implementação da interface)
│       └── BibliotecaServer.java  (Servidor)
│       ├── Biblioteca.java        (A Interface remota)
│       └── Livro.java             (O Objeto serializável)
│
├── start_client.sh                (Script para Execução do Cliente)
└── start_server.sh                (Script para Execução do Servidor)
```

### Aplicação do RMI no Projeto

Nesta aplicação, o RMI permite que o `BibliotecaClient` (executndo em uma JVM) gerencie o acervo de livros que existe dentro do `BibliotecaServer` (executado em outra JVM).

`RMI.Biblioteca.java` (Interface Remota)
- Define a API remota. Especifica os métodos (`adicionarLivro`, `consultarLivro`, etc.) que o servidor implementa e que o cliente pode invocar.
- Estende `Remote` e todos os métodos lançam `RemoteException`.

`RMI.Livro.java` (DTO)
- Um DTO que encapsula os dados de um livro.
- Implementa `Serializable`. Isso é o que permite que instâncias de `Livro` possam ser serializadas e enviadas do servidor para o cliente como valor de retorno (por exemplo, em `consultarLivro`).

`RMI.BibliotecaImpl.java` (Objeto Remoto)
- A implementação concreta da lógica de negócios. Gerencia o estado (o `HashMap` de livros) e executa as operações definidas pela interface `Biblioteca`.
- Estende `UnicastRemoteObject`, o que a torna um objeto remoto capaz de receber invocações.

`RMI.BibliotecaServer.java` (Host e Registry)
- O ponto de entrada (main) do processo servidor.
- Executa duas operações críticas:
    - `LocateRegistry.createRegistry(1099)`: Inicia o RMI Registry na porta 1099, tornando o nameserver disponível.
    - `Naming.rebind(".../BibService", biblioteca)`: instancia `BibliotecaImpl` e associa essa instância ao nome `BibService` dentro do Registry.

`RMI.BibliotecaClient.java` (Cliente)
- O ponto de entrada (main) do processo cliente.
- Executa o lookup:
    - `Naming.lookup(".../BibService")`: Consulta o RMI Registry no endereço do servidor, solicitando a referência associada ao nome "BibService".
    - O Registry retorna o Stub (o proxy) para o cliente.
    - O cliente armazena o Stub em uma variável do tipo da interface (`Biblioteca bib`). Quaisquer chamdas subsequentes a `bib.adicionarLivros(...)` são, na verdade, invocações de método no Stub, que delega a chamada pela rede ao servidor.

#### Fluxo de uma chamada

Cliente chama `consultarLivro`:
- Inicialização: O `BibliotecaServer` é executado, cria `BibliotecaImpl` e o registra no RMI Registry sob o nome "BibService".
- Lookup: O `BibliotecaClient` é executado e solicita ao Registry a referência para "BibService".
- Proxy: O Registry retorna ao cliente um Stub que implementa `Biblioteca`.
- Invocação: O cliente chama `bib.consultarLivro("123-ABC)`. Esta é uma chamada de método local no objeto Stub.
- Comunicação (Cliente): O Stub realiza o marshalling (serialização) do argumento ("123-ABC") e o envia, via TCP/IP, para o processo servidor.
- Execução (Servidor): O runtime do RMI no servidor (Skeleton) recebe a solicitação, realiza o unmarshalling (deserialização) do argumento e invoca o método `consultarLivro("123-ABC")` no objeto `BibliotecaImpl` real.
- Retorno (Servidor): O `BibliotecaImpl` localiza o `Livro`, e o retorna. O Skeleton serializa o objeto `Livro` e o envia de volta como resposta.
- Recebimento (Cliente): O Stub recebe os dados, deserializa, e entrega o objeto `Livro` ao cliente como valor de retorno da chamada.


## Pré-requisitos

1. **JDK (Java Development Kit):** Necessário para a compilação (javac) e execução (java) do projeto.
2. **Ambiente**: Os scripts de execução (.sh) são feitos para um terminal shell.
    - Windows: É necessário o uso de um terminal shell como o git bash ou o WSL para executar o projeto

Para verificar se você tem o Java e o compilador, abra um terminal e digite:

```bash
java -version
javac -version
```

## Guia de Execução (Linux / macOS / Git Bash)

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

package RMI;

import java.rmi.Naming;
import java.util.Scanner;

public class BibliotecaClient {
    public static void main(String[] args) {
        try {

            Scanner sc = new Scanner(System.in);

            System.out.print("Insira o IP do servidor: ");
            String ip = sc.nextLine();
            Biblioteca bib = (Biblioteca) Naming.lookup("rmi://" + ip + ":1099/BibService");

            int opcao = -1;

            System.out.println("Cliente RMI conectado ao servidor!");

            while (opcao != 0) {
                imprimirMenu();
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1:
                        adicionar(bib, sc);
                        break;
                    case 2:
                        consultar(bib, sc);
                        break;
                    case 3:
                        emprestar(bib, sc);
                        break;
                    case 4:
                        devolver(bib, sc);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente.");
                }
                System.out.println("---------------------------------");
            }
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void imprimirMenu() {
        System.out.println("\n=== Biblioteca ===");
        System.out.println("1. Adicionar Livro");
        System.out.println("2. Consultar Livro");
        System.out.println("3. Emprestar Livro");
        System.out.println("4. Devolver Livro");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    private static void adicionar(Biblioteca bib, Scanner scanner) {
        try {
            System.out.print("Digite o Titulo: ");
            String titulo = scanner.nextLine();
            System.out.print("Digite o Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Digite o ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Digite a Quantidade: ");
            int qtd = scanner.nextInt();
            scanner.nextLine();

            String resposta = bib.adicionarLivro(titulo, autor, isbn, qtd);
            System.out.println("\n[Servidor diz]: " + resposta);

        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }

    private static void consultar(Biblioteca bib, Scanner scanner) {
        try {
            System.out.print("Digite o ISBN para consulta: ");
            String isbn = scanner.nextLine();

            Livro livro = bib.consultarLivro(isbn);

            if (livro != null) {
                System.out.println("\n[Livro Encontrado]:");
                System.out.println(livro.getInfo());
            } else {
                System.out.println("\n[Servidor diz]: Livro nao encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }

    private static void emprestar(Biblioteca bib, Scanner scanner) {
        try {
            System.out.print("Digite o ISBN para emprestar: ");
            String isbn = scanner.nextLine();

            String resposta = bib.emprestarLivro(isbn);
            System.out.println("\n[Servidor diz]: " + resposta);

        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }

    private static void devolver(Biblioteca bib, Scanner scanner) {
        try {
            System.out.print("Digite o ISBN para devolver: ");
            String isbn = scanner.nextLine();

            String resposta = bib.devolverLivro(isbn);
            System.out.println("\n[Servidor diz]: " + resposta);

        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }
}

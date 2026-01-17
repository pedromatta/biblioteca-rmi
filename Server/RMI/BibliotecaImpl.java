package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class BibliotecaImpl extends UnicastRemoteObject implements Biblioteca {

    private Map<String, Livro> listaLivros;
    private static final String NOME_ARQUIVO = "biblioteca_db.txt";

    protected BibliotecaImpl() throws RemoteException {
        super();
        this.listaLivros = new HashMap<>();
        carregarDados();
        System.out.println("Serviço de biblioteca instanciado e dados carregados.");
    }

    @Override
    public synchronized String adicionarLivro(String titulo, String autor, int quantidade) throws RemoteException {
        
        String isbn = UUID.randomUUID().toString();

        Livro novoLivro = new Livro(titulo, autor, isbn, quantidade);
        listaLivros.put(isbn, novoLivro);
        
        salvarDados();

        System.out.println("[LOG] Livro adicionado: " + titulo + " (ISBN: " + isbn + ")");
        return "Livro '" + titulo + "' adicionado com sucesso! (ISBN: " + isbn + ")";
    }

    @Override
    public Livro consultarLivro(String isbn) throws RemoteException {
        Livro livro = listaLivros.get(isbn);
        if (livro != null) {
            System.out.println("[LOG] Consulta com sucesso: " + livro.getTitulo() + " (ISBN: " + isbn + ")");
        } else {
            System.out.println("[LOG] Consulta falhou: ISBN " + isbn + " nao encontrado.");
        }
        return livro;
    }

    @Override
    public synchronized String emprestarLivro(String isbn) throws RemoteException {
        Livro livro = listaLivros.get(isbn);
        if (livro == null) {
            System.out.println("[LOG] Falha ao emprestar: ISBN " + isbn + " nao encontrado.");
            return "ERRO: Livro nao encontrado.";
        }

        if (livro.emprestar()) {
            salvarDados();
            System.out.println("[LOG] Livro emprestado: " + livro.getTitulo() + " (ISBN: " + isbn + ")");
            return "Livro '" + livro.getTitulo() + "' emprestado.";
        } else {
            System.out.println("[LOG] Falha ao emprestar: " + livro.getTitulo() + " (ISBN: " + isbn + ") sem estoque.");
            return "ERRO: Livro '" + livro.getTitulo() + "' sem estoque.";
        }
    }

    @Override
    public synchronized String devolverLivro(String isbn) throws RemoteException {
        Livro livro = listaLivros.get(isbn);
        if (livro == null) {
            System.out.println("[LOG] Falha ao devolver: ISBN " + isbn + " nao encontrado.");
            return "ERRO: Livro nao encontrado.";
        }

        livro.devolver();
        salvarDados();
        System.out.println("[LOG] Livro devolvido: " + livro.getTitulo() + " (ISBN: " + isbn + ")");
        return "Livro '" + livro.getTitulo() + "' devolvido. Estoque atual: " + livro.getQuantidade();
    }

    @Override
    public List<Livro> listarTodosLivros() throws RemoteException {
        System.out.println("[LOG] Listando todos os livros.");
        return new ArrayList<>(listaLivros.values());
    }

    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("[IO] Nenhum arquivo de dados (" + NOME_ARQUIVO + ") encontrado. Iniciando com base vazia.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 4) {
                    String isbn = partes[0];
                    String titulo = partes[1];
                    String autor = partes[2];
                    int quantidade = Integer.parseInt(partes[3]);
                    listaLivros.put(isbn, new Livro(titulo, autor, isbn, quantidade));
                }
            }
            System.out.println("[IO] Dados carregados de " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.err.println("[IO-ERRO] Falha ao carregar dados: " + e.getMessage());
        } catch (NumberFormatException e) {
             System.err.println("[IO-ERRO] Falha ao parsear quantidade: " + e.getMessage());
        }
    }

    private synchronized void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Livro livro : listaLivros.values()) {
                writer.write(String.format("%s;%s;%s;%d\n",
                        livro.getIsbn(),
                        livro.getTitulo(),
                        livro.getAutor(),
                        livro.getQuantidade()));
            }
        } catch (IOException e) {
            System.err.println("[IO-ERRO] Falha ao salvar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

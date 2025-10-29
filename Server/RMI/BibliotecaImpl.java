package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class BibliotecaImpl extends UnicastRemoteObject implements Biblioteca {

    private Map<String, Livro> listaLivros;

    protected BibliotecaImpl() throws RemoteException {
        super();
        this.listaLivros = new HashMap<>();
        System.out.println("Serviço de biblioteca instanciado.");
    }

    @Override
    public String adicionarLivro(String titulo, String autor, String isbn, int quantidade) throws RemoteException {
        if (listaLivros.containsKey(isbn)) {
            System.out.println("[LOG] Falha ao adicionar: ISBN " + isbn + " ja existe.");
            return "ERRO: Livro com este ISBN ja cadastrado.";
        }
        Livro novoLivro = new Livro(titulo, autor, isbn, quantidade);
        listaLivros.put(isbn, novoLivro);

        System.out.println("[LOG] Livro adicionado: " + titulo + " (ISBN: " + isbn + ")");
        return "Livro '" + titulo + "' adicionado com sucesso!";
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
    public String emprestarLivro(String isbn) throws RemoteException {
        Livro livro = listaLivros.get(isbn);
        if (livro == null) {
            System.out.println("[LOG] Falha ao emprestar: ISBN " + isbn + " nao encontrado.");
            return "ERRO: Livro nao encontrado.";
        }

        if (livro.emprestar()) {
            System.out.println("[LOG] Livro emprestado: " + livro.getTitulo() + " (ISBN: " + isbn + ")");
            return "Livro '" + livro.getTitulo() + "' emprestado.";
        } else {
            System.out.println("[LOG] Falha ao emprestar: " + livro.getTitulo() + " (ISBN: " + isbn + ") sem estoque.");
            return "ERRO: Livro '" + livro.getTitulo() + "' sem estoque.";
        }
    }

    @Override
    public String devolverLivro(String isbn) throws RemoteException {
        Livro livro = listaLivros.get(isbn);
        if (livro == null) {
            System.out.println("[LOG] Falha ao devolver: ISBN " + isbn + " nao encontrado.");
            return "ERRO: Livro nao encontrado.";
        }

        livro.devolver();
        System.out.println("[LOG] Livro devolvido: " + livro.getTitulo() + " (ISBN: " + isbn + ")");
        return "Livro '" + livro.getTitulo() + "' devolvido. Estoque atual: " + livro.getQuantidade();
    }
}

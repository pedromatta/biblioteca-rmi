package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Biblioteca extends Remote {

    String adicionarLivro(String titulo, String autor, int quantidade) throws RemoteException;

    Livro consultarLivro(String isbn) throws RemoteException;

    String emprestarLivro(String isbn) throws RemoteException;

    String devolverLivro(String isbn) throws RemoteException;

    List<Livro> listarTodosLivros() throws RemoteException;
}

package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Biblioteca extends Remote {

    String adicionarLivro(String titulo, String autor, String isbn, int quantidade) throws RemoteException;

    Livro consultarLivro(String isbn) throws RemoteException;

    String emprestarLivro(String isbn) throws RemoteException;

    String devolverLivro(String isbn) throws RemoteException;
}

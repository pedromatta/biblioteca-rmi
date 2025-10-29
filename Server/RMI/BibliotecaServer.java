package RMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class BibliotecaServer {
    public static void main(String[] args) {
        try {
            Biblioteca biblioteca = new BibliotecaImpl();

            LocateRegistry.createRegistry(1099);

            Naming.rebind("rmi://localhost:1099/BibService", biblioteca);


            System.out.println("Servidor RMI da Biblioteca pronto!");
            if(args[0] != null)
            {
                System.out.println("Servindo em: " + args[0]);
            }
            System.out.println("Aguardando conexoes...");

        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}

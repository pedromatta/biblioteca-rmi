package RMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class BibliotecaServer {
    public static void main(String[] args) {
        try {
            Biblioteca biblioteca = new BibliotecaImpl();

            LocateRegistry.createRegistry(1099);

            if(args[0] != null)
            {
                Naming.rebind("rmi://" + args[0] + ":1099/BibService", biblioteca);
                System.out.println("Servidor RMI da Biblioteca pronto!");
                System.out.println("Servindo em: " + args[0]);
            }
            else
            {
                Naming.rebind("rmi://localhost:1099/BibService", biblioteca);
                System.out.println("Servindo do localhost");
                System.out.println("Servidor RMI da Biblioteca pronto!");
            }

            System.out.println("Aguardando conexoes...");

        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}

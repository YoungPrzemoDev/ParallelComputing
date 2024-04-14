import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            int numberOfAgents = args.length;

            RemoteObj remoteObj = new RemoteObj(numberOfAgents);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Server", remoteObj);// jak bedzie rzucac wyjatek to rebind zrob

            System.out.println("Server is ready.");
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

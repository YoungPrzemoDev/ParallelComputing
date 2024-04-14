import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Agent {

    public static void main(String[] args) {


        try{
            String agentId = args[0];
            SortingAgentObj obj = new SortingAgentObj();
            AgentInterface stub = (AgentInterface) UnicastRemoteObject.exportObject(obj,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(("Agent" + agentId),stub);
            System.err.println("Sorting Agent " + agentId + " bound");
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("SortingAgent exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

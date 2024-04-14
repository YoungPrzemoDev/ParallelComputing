import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgentInterface extends Remote {

    int[] sortChunk(int[] chunk) throws RemoteException;
}

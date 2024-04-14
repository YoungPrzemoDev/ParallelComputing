import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote{
    int[] sortArray(int[] arrayToSort) throws RemoteException, NotBoundException;

}

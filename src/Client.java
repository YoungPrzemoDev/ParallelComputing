import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {
    private Client() {
    }

    public static void main(String[] args) {
        try {

            Registry registry = LocateRegistry.getRegistry(null);// na dowolnym porcie

            //Laczenie sie z serwerem
            RemoteInterface stub = (RemoteInterface) registry.lookup("Server");
            int[] arrayToSort = {5, 3, 2, 1, 4};

            int[] sortedArray = stub.sortArray(arrayToSort);
            System.err.println("Sorted array: " + Arrays.toString(sortedArray));
        }
        catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

import java.rmi.RemoteException;
import java.util.Arrays;

public class SortingAgentObj implements AgentInterface{


    @Override
    public int[] sortChunk(int[] chunk) throws RemoteException {
        System.out.println("Agent received chunk: " + Arrays.toString(chunk));
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            // Perform odd indexed passes
            for (int i = 1; i <= chunk.length - 2; i = i + 2) {
                if (chunk[i] > chunk[i + 1]) {
                    swap(chunk, i, i + 1);
                    isSorted = false;
                }
            }
            // Perform even indexed passes
            for (int i = 0; i <= chunk.length - 2; i = i + 2) {
                if (chunk[i] > chunk[i + 1]) {
                    swap(chunk, i, i + 1);
                    isSorted = false;
                }
            }
        }
        System.out.println("Agent sorted chunk: " + Arrays.toString(chunk));
        return chunk;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class RemoteObj extends UnicastRemoteObject implements RemoteInterface {
    private ExecutorService executor;
    private int number_of_agents;
    private Registry registry;

    public RemoteObj(int number_of_agents) throws RemoteException {
        super();
        this.number_of_agents = number_of_agents;
        this.registry = LocateRegistry.getRegistry();
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public int[] sortArray(int[] arrayToSort) throws RemoteException, NotBoundException {
        boolean sorted = false;
        int[] combinedArray = arrayToSort;

        while (!sorted) {
            sorted = true;
            combinedArray = doSortingPhase(combinedArray, 0);  // Odd phase
            sorted = isSorted(combinedArray) && sorted;

            combinedArray = doSortingPhase(combinedArray, 1);  // Even phase
            sorted = isSorted(combinedArray) && sorted;
        }

        return combinedArray;
    }

    private int[] doSortingPhase(int[] array, int phaseStart) throws RemoteException, NotBoundException {
        List<Future<int[]>> results = new ArrayList<>();

        // Assume each agent will handle one pair during each phase
        int chunkSize = 2; // since each agent is responsible for comparing a pair

        for (int i = phaseStart; i < array.length - 1; i += number_of_agents * chunkSize) {
            for (int j = 0; j < number_of_agents && (i + j * chunkSize) < array.length - 1; j++) {
                int start = i + j * chunkSize;
                int end = start + chunkSize;

                // Make sure the last chunk does not exceed array length
                if (end > array.length) {
                    end = array.length;
                }

                int[] chunk = Arrays.copyOfRange(array, start, end);

                // If it's not a valid pair, don't bother sorting
                if (chunk.length < 2) {
                    results.add(CompletableFuture.completedFuture(chunk));
                    continue;
                }

                AgentInterface agent = (AgentInterface) registry.lookup("Agent" + j);
                Callable<int[]> task = () -> agent.sortChunk(chunk);
                results.add(executor.submit(task));
            }
        }

        // Combine chunks back into one array
        int[] combinedArray = Arrays.copyOf(array, array.length);
        for (int i = 0, currentIdx = phaseStart; i < results.size(); i++, currentIdx += chunkSize) {
            try {
                int[] sortedChunk = results.get(i).get();
                for (int k = 0; k < sortedChunk.length; k++) {
                    if (currentIdx + k < combinedArray.length) {
                        combinedArray[currentIdx + k] = sortedChunk[k];
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RemoteException("Error while combining sorted chunks.", e);
            }
        }

        return combinedArray;
    }


    private boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}

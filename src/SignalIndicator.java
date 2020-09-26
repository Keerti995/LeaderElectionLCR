import java.util.concurrent.Semaphore;

/**
 * SignalIndicator class maintains consistency among all the thread processes using a counter and semaphore permit count of 1
 */
public class SignalIndicator {
    private static int counter = 0;
    public Semaphore semaphore = new Semaphore(1);

    /**
     * Whenever a thread process is accessing the counter no other process can access it to increment the counter
     */
    public void increment(){
        try {
            semaphore.acquire();
            counter++;
            semaphore.release();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Whenever a thread process is accessing the counter no other process can access it to decrement the counter
     */
    public void decrement(){
        try {
            semaphore.acquire();
            counter--;
            semaphore.release();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRoundCompleted(){
        boolean isComplete = false;
        try {
            semaphore.acquire();
            isComplete = counter == 0;
            semaphore.release();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        return isComplete;
    }
}

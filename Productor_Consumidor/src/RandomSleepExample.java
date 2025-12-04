import java.util.concurrent.ThreadLocalRandom;

public class RandomSleepExample {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            try {
                // Sleep between 0 and 9 milliseconds
                long sleepTime = ThreadLocalRandom.current().nextLong(5000);
                Thread.sleep(sleepTime);

                System.out.println("Iteration " + i + " slept for " + sleepTime + " ms");
            } catch (InterruptedException e) {
                // Restore interrupted status and exit loop
                Thread.currentThread().interrupt();
                System.err.println("Thread was interrupted, stopping loop.");
                break;
            }
        }
    }
}
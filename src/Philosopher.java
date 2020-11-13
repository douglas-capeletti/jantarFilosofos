import java.util.Random;
import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {

    private final int id;
    private final int runningTime;
    private final Random randomizer = new Random();

    public volatile Semaphore leftFork;
    public volatile Semaphore rightFork;

    private States state = States.THINKING;
    private int thinkingCount = 0;
    private int hungryCount = 0;
    private int eatingCount = 0;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork, int secondsRunning) {
        this.id = id + 1;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.runningTime = secondsRunning; // +1 second for the overhead
    }

    private void thinking() {
        this.state = States.THINKING;
        this.delaySeconds(5);
        this.thinkingCount++;
        this.log();
    }

    private void hungry() {
        this.state = States.HUNGRY;
        this.delaySeconds(randomizer.nextInt(4)); //0 -> 4 exclusive on top limit
        this.hungryCount++;
        this.log();
    }

    private void eating() {
        this.state = States.EATING;
        this.delaySeconds(2);
        this.eatingCount++;
        this.log();
    }

    @Override
    public void run() {
        long finalClockMillis = System.currentTimeMillis() + (runningTime * 1000);
        while (System.currentTimeMillis() < finalClockMillis) {
            this.thinking();
            if (leftFork.tryAcquire()) {
                if (rightFork.tryAcquire()) {
                    this.eating();
                    rightFork.release();
                } else {
                    this.hungry();
                }
                leftFork.release();
            } else {
                this.hungry();
            }
        }
    }

    private void delaySeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            System.err.println("Erro ao aplicar delay no filósofo: " + this.id);
        }
    }

    private void log() {
        System.out.println("Filósofo: " + this.id + " \tStatus: " + this.state);
    }

    public void logReport() {
        System.out.println("Filósofo:" + this.id +
            "\n\t->Pensou: " + this.thinkingCount +
            "\n\t->Tentou comer: " + this.hungryCount +
            "\n\t->Comeu: " + this.eatingCount + "\n");
    }
}

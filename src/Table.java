import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Table {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Digite o número de filósofos: ");
        int philosopherCount = requestPositiveNumber();
        System.out.println("Digite o tempo de duração do programa: ");
        int runningTime = requestPositiveNumber();
        run(philosopherCount, runningTime);
    }

    public static void run(int philosopherCount, int runningTime) throws InterruptedException {
        Philosopher[] philosophers = new Philosopher[philosopherCount];

        philosophers[0] = new Philosopher(0, new Semaphore(1), new Semaphore(1), runningTime);
        int index = 1;

        while (index < philosopherCount - 1) {
            Semaphore leftFork = philosophers[index - 1].rightFork;
            Semaphore rightFork = new Semaphore(1);

            philosophers[index] = new Philosopher(index, leftFork, rightFork, runningTime);
            index++;
        }

        Semaphore leftFork = philosophers[index - 1].rightFork;
        Semaphore rightFork = philosophers[0].leftFork;
        philosophers[index] = new Philosopher(index, leftFork, rightFork, runningTime);

        System.out.println("------- Iniciando filósofos -------");
        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }
        for (Philosopher philosopher : philosophers) {
            philosopher.join();
        }

        System.out.println("------- Relatório filósofos -------");
        for (Philosopher philosopher : philosophers) {
            philosopher.logReport();
        }
    }

    private static int requestPositiveNumber() {
        int value = scanner.nextInt();
        while (value < 1) {
            System.out.println("Valor precisa ser um número inteiro e positivo, tente novamente: ");
            value = scanner.nextInt();
        }
        return value;
    }

}

package Task9;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DiningPhilosophers philosophers = new DiningPhilosophers();

        Thread.sleep(1000);

        philosophers.CancelSimulation();
    }
}

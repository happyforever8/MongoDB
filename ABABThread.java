//考了一个多线程的题，让两个线程打印出"ABABABAB"，一个线程只打A，另一个只打B，最后结束之后再在主线程里面打印一个\n
public class ABPrinter {

    private final Object lock = new Object();
    private boolean turnA = true; // Flag to control the turn

    public void printA() {
        for (int i = 0; i < 4; i++) { // Loop 4 times to print 4 'A's
            synchronized (lock) {
                while (!turnA) {
                    try {
                        lock.wait(); // Wait if it's not A's turn
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted: " + e.getMessage());
                    }
                }
                System.out.print("A");
                turnA = false; // Set turn to B
                lock.notifyAll(); // Notify the other thread
            }
        }
    }

    public void printB() {
        for (int i = 0; i < 4; i++) { // Loop 4 times to print 4 'B's
            synchronized (lock) {
                while (turnA) {
                    try {
                        lock.wait(); // Wait if it's not B's turn
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted: " + e.getMessage());
                    }
                }
                System.out.print("B");
                turnA = true; // Set turn to A
                lock.notifyAll(); // Notify the other thread
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ABPrinter printer = new ABPrinter();

        Thread threadA = new Thread(printer::printA);
        Thread threadB = new Thread(printer::printB);

        threadA.start();
        threadB.start();

        threadA.join(); // Wait for thread A to finish
        threadB.join(); // Wait for thread B to finish

        System.out.println("\nDone");
    }
}

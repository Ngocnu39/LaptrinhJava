class Chopstick {
    private boolean isAvailable = true;

    public synchronized void pickUp(String philosopher) throws InterruptedException {
        while (!isAvailable) {
            wait();
        }
        isAvailable = false;
        System.out.println(philosopher + " picked up chopstick.");
    }

    public synchronized void putDown(String philosopher) {
        isAvailable = true;
        System.out.println(philosopher + " put down chopstick.");
        notify();
    }
}

class Philosopher extends Thread {
    private String name;
    private Chopstick leftChopstick, rightChopstick;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.leftChopstick = left;
        this.rightChopstick = right;
    }

    public void run() {
        try {
            while (true) {
                System.out.println(name + " is thinking...");
                Thread.sleep((int) (Math.random() * 1000));

                leftChopstick.pickUp(name);
                rightChopstick.pickUp(name);

                System.out.println(name + " is eating...");
                Thread.sleep((int) (Math.random() * 1000));

                leftChopstick.putDown(name);
                rightChopstick.putDown(name);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class DiningPhilosophers {
    public static void main(String[] args) {
        Chopstick[] chopsticks = new Chopstick[5];
        for (int i = 0; i < 5; i++) {
            chopsticks[i] = new Chopstick();
        }

        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(
                    "Philosopher " + (i + 1),
                    chopsticks[i],
                    chopsticks[(i + 1) % 5]
            );
            philosophers[i].start();
        }
    }
}


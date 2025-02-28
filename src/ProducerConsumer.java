import java.util.LinkedList;

class Buffer {
    private LinkedList<Integer> queue = new LinkedList<>();
    private int capacity = 5;

    public synchronized void produce(int value) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // Chờ nếu buffer đầy
        }
        queue.add(value);
        System.out.println("Produced: " + value);
        notify(); // Báo cho consumer có thể lấy dữ liệu
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // Chờ nếu buffer rỗng
        }
        int value = queue.removeFirst();
        System.out.println("Consumed: " + value);
        notify(); // Báo cho producer có thể thêm dữ liệu
        return value;
    }
}

class Producer extends Thread {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        int value = 0;
        try {
            while (true) {
                buffer.produce(value++);
                Thread.sleep(1000); // Tạo sản phẩm sau mỗi giây
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                buffer.consume();
                Thread.sleep(1500); // Tiêu thụ sản phẩm sau mỗi 1.5 giây
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        producer.start();
        consumer.start();
    }
}


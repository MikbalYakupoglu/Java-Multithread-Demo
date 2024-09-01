package section5.demo1;

public class App {

    // Atomatic Operation --> tek adımlı operasyonlardır. (set, get gibi)
    // Burada ki items++ veya items-- gibi işlemler 3 aşamadan oluşur. (değeri al, 1 ekle, değeri yaz)
    // Bu yüzden bu işlemler atomic sayılmaz.
    // Atomic olmayan işlemlerde thread kullanımına dikkat edilmelidir. (Aynı objeye 2 thread aynı anda erişmemelidir)
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Items in inventory: " + inventoryCounter.getItems());
    }


    public static  class DecrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    public static  class IncrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }


    public static class InventoryCounter{
        private int items = 0;

        public void increment() {
            items++;
        }

        public void decrement() {
            items--;
        }

        public int getItems() {
            return items;
        }
    }
}

package section6.demo2;

public class App {

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

        final Object lock = new Object();

        public void increment() {
            // another code which does not need to be synchronized
            synchronized (lock){
                items++;
            }
            // another code which does not need to be synchronized
        }

        public void decrement() {
            // another code which does not need to be synchronized
            synchronized (lock){
                items--;
            }
            // another code which does not need to be synchronized
        }

        public int getItems() {
            // another code which does not need to be synchronized
            synchronized (lock){
                return items;
            }
            // another code which does not need to be synchronized
        }
    }
}

package section7.demo2;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {
    public static final int HIGHEST_PRICE = 1000;
    public static void main(String[] args) throws InterruptedException {
        DatabaseInventory databaseInventory = new DatabaseInventory();

        Random random = new Random();

        for(int i = 0; i < 100000 ; i++){
            databaseInventory.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(() -> {
            while(true){
                databaseInventory.addItem(random.nextInt(HIGHEST_PRICE));
                databaseInventory.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();

        for(int readerIndex = 0; readerIndex < numberOfReaderThreads ; readerIndex++){
            Thread reader = new Thread(() -> {
               for(int i = 0; i < 100000 ; i++){
                   int upperBound = random.nextInt(HIGHEST_PRICE);
                   int lowerBound = upperBound > 0 ? random.nextInt(upperBound) : 0;

                   databaseInventory.getNumberOfItemsInPriceList(lowerBound, upperBound);
               }
            });

            reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();

        for(Thread reader : readers){
            reader.start();
        }

        for(Thread reader : readers){
            reader.join();
        }

        long endReadingTime = System.currentTimeMillis();

        System.out.printf("Reading time %s ms\n", endReadingTime - startReadingTime);

    }


    public static class DatabaseInventory {
        private final TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private final ReentrantLock lock = new ReentrantLock();

        // writeLock, degisken modify edilirken, diğer bütün threadlerin erismesini engeller
        // readLock, değisken okunurken, okuma islemi yapmak icin gelen threadlerin erisimine izin verir
        // (readLock lockluyken, writeLock yazilan yere, thread girisi yapilamaz (deger modify edilemez))
        // böylelikle, read operasyonlarında, threadler birbirini beklememis olur
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock readLock = readWriteLock.readLock();
        private final Lock writeLock = readWriteLock.writeLock();


        // writeLock yalnizca 1 thread girisine izin verirken,
        // readLock, birden fazla threadin ayni anda erismesine izin verir
        public int getNumberOfItemsInPriceList(int lowerBound, int upperBound) {
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }

                return sum;
            } finally {
                readLock.unlock();
            }
        }

        public void addItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItemsPrice = priceToCountMap.get(price);

                if (numberOfItemsPrice == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsPrice + 1);
                }
            } finally {
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItemsPrice = priceToCountMap.get(price);

                if (numberOfItemsPrice == null || numberOfItemsPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsPrice - 1);
                }
            } finally {
                writeLock.unlock();
            }
        }
    }
}

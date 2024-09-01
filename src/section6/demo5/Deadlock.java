package section6.demo5;

import java.util.Random;

public class Deadlock {
    public static void main(String[] args) {
        Intersection intersection = new Intersection();

        TrainA trainA = new TrainA(intersection);
        TrainB trainB = new TrainB(intersection);

        trainA.start();
        trainB.start();
    }


    public static class TrainA extends Thread {
        private Intersection intersection;
        private final Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(random.nextInt(5));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                intersection.takeRoadA();
            }
        }
    }

    public static class TrainB extends Thread {
        private Intersection intersection;
        private Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(random.nextInt(5));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                intersection.takeRoadB();
            }
        }
    }


    public static class Intersection {
        private final Object roadA = new Object();
        private final Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road A");

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        // Burada eper ki, ilk lock roadB kilitlenseydi, deadlock riski dogardı
        // Bunun nedeni, roadA methodu roadA objesine tek thread alır, ve roadB farklı bir thread alırsa
            // iki threadde diger objenin lockunu bekleyeceginden, ikiside ilerleyemez ve deadlock olusur
        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road B");

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

    }
}

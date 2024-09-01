package section6.demo4;

import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();

        Thread thread1 = new Thread(() -> {
            while (true) {
                sharedClass.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                sharedClass.checkForDataRace();
            }
        });

        thread1.start();
        thread2.start();

    }

    // volatile int veya AtomicInteger kullanılarak, datarace sorunu çözülebilir.
    // datarace, methoeddaki arttırılan değişkenler, birbirine bağımlı olmadığında yaşanır
    // örneğin y değişkeni x değişkenine bağımlı olsaydı, datarace gözlenmezdi
    // dataracenin nedeni, CPU'nun daha optimize çalışması için, operasyonların sırasını kendini düzenlemesidir
    public static class SharedClass {
        private AtomicInteger x = new AtomicInteger(0);
        private AtomicInteger y = new AtomicInteger(0);

        public void increment() {
            x.getAndIncrement();
            y.getAndIncrement();
        }

        public void checkForDataRace() {
            if (y.get() > x.get()) {
                System.out.println("y > x - Data Race Detected");
            }
        }
    }
}

package demo5;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputs = List.of(64L, 10L, 25L, 786L, 1547L, 2768L, 5564L, 1_000_000L);

        List<FactorialThread> threads = new ArrayList<>();

        for (long input : inputs) {
            threads.add(new FactorialThread(input));
        }

        // deamon olarak ayarlanan threadler, main thread bittiğinde sonlanır
        // deamon threadların tamamlanmaları beklenmez
        for (Thread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }

        // thread joinlenirse, threadlerin hepsi bitene kadar bekler
        // parametre olarak verdiğimiz 2 saniye, joinin maksimum bekleme süresidir
        for (Thread thread : threads) {
            thread.join(2000);
        }

        for (int i = 0; i < inputs.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + inputs.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("The calculation for " + inputs.get(i) + " is still in progress");
            }
        }
    }
}

class FactorialThread extends Thread {
    private BigInteger result;
    private BigInteger factorial = BigInteger.ONE;
    private boolean isFinished;

    private final long input;

    public FactorialThread(long input) {
        this.input = input;
    }

    @Override
    public void run() {
        this.result = getFactorial(input);
        isFinished = true;
    }

    public BigInteger getFactorial(long input) {
        for (long i = input; i >= 1; i--) {
            factorial = factorial.multiply(new BigInteger(Long.toString(i)));
        }
        return factorial;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public BigInteger getResult() {
        return result;
    }
}
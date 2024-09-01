package section3.demo2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ComplexCalculation {

    public static void main(String[] args) throws InterruptedException {
        ComplexCalculation complexCalculation = new ComplexCalculation();
        BigInteger result = complexCalculation.calculateResult(BigInteger.valueOf(2), BigInteger.valueOf(6), BigInteger.valueOf(2), BigInteger.valueOf(2));
        System.out.println(result);
    }

    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result = BigInteger.ZERO;
        List<PowerThread> threads = new ArrayList<>();

        threads.add(new PowerThread(base1, power1));
        threads.add(new PowerThread(base2, power2));

        for (Thread thread : threads) {
            thread.start();
        }

        for (PowerThread thread : threads) {
            thread.join();
            result = result.add(thread.getResult());
        }

        return result;
    }

    private static class PowerThread extends Thread {
        private BigInteger base;
        private BigInteger power;
        private BigInteger result = BigInteger.ONE;;


        public PowerThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            result = getPower(base, power);
        }


        private BigInteger getPower(BigInteger base, BigInteger power) {

            for (; power.compareTo(BigInteger.ZERO) > 0; power = power.subtract(BigInteger.ONE)) {
                result = result.multiply(base);
            }

            return result;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
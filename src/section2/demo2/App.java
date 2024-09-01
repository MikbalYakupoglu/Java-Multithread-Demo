package section2.demo2;

class Runner implements Runnable{
    private final int id;

    public Runner(int id){
        this.id = id;
    }


    @Override
    public void run(){
        for (int i=0;i<10;i++){
            System.out.println(id +" - Hello " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}

public class App {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runner(1));
        Thread t2 = new Thread(new Runner(2));

        t1.start();
        t2.start();

    }
}

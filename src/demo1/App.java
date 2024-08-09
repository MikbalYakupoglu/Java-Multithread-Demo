package demo1;

class Runner extends Thread{
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
                throw new RuntimeException(e);
            }
        }
    }
}



public class App {
    public static void main(String[] args) {
        Runner runner1 = new Runner(1);
        runner1.start();

        Runner runner2 = new Runner(2);
        runner2.start();
    }
}

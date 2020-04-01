import Schedulers.RoundRobin.LoopingNumber;
import Schedulers.RoundRobin.RoundRobin;

public class Main {

    public static void main(String[] args){
        RoundRobin rr = new RoundRobin(5);
        rr.addProcess(1);
        rr.addProcess(2);
        rr.addProcess(5);
        rr.addProcess(4);
        rr.addProcess(3);
        rr.addProcess(10);
        rr.addProcess(7);
        rr.addProcess(rr.getNextAvailableID());
        System.out.println(rr.getNextAvailableID());
        /*for(int x = 0; x < 30; x++){
            System.out.println(rr.getProcess());
        }
        System.out.println("---------------------------------------------------------------------------------------");
        rr.removeProcess(2);
        rr.addProcess(8);
        for (int x = 0; x < 30; x++){
            System.out.println(rr.getProcess());
        }
        for (int x = 0; x < 30; x++){
            System.out.println(rr.getProcess());
        }*/

    }
}

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MasterProcess implements Runnable {
    private int[] process_ids;
    private static Set<Thread> myProcesses;
    public SignalIndicator signalIndicator = new SignalIndicator();
    public MessageChannel myMessages;
    public Iterator<Thread> iterator;
    private boolean isLeaderElected;

    public MasterProcess(int[] process_ids){
        this.process_ids = process_ids;
        this.myMessages = new MessageChannel(process_ids.length);
        this.isLeaderElected = false;
    }

    public void spawnProcesses() throws InterruptedException{
        myProcesses = new HashSet<>();
        for(int i=0;i<process_ids.length;i++){
            Thread process = new Thread(new Process(i,process_ids[i],signalIndicator,myMessages));
            process.setName(Integer.toString(process_ids[i]));
            signalIndicator.increment();
            process.start();
            myProcesses.add(process);
        }

        while(!isLeaderElected){
            while(!signalIndicator.isRoundCompleted()){
                Thread.sleep(10);
            }
            myMessages.messageList  = myMessages.newMessageList;
            //round has completed
            synchronized (signalIndicator){
                iterator = myProcesses.iterator();
                while(iterator.hasNext()){
                    Thread thread = iterator.next();
                    if(thread.getState()!= Thread.State.TERMINATED){
                        signalIndicator.increment();
                    }
                }
                if(this.signalIndicator.isRoundCompleted()) this.isLeaderElected = true;
                signalIndicator.notifyAll();
            }
        }
    }

    public void run(){
        try{
            spawnProcesses();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

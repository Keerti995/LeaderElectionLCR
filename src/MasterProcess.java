import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * MasterProcess Class is a thread class which spawns all the processes and elects the leader using LCR Algorithm
 */
public class MasterProcess implements Runnable {
    private int[] process_ids;
    private static Set<Thread> myProcesses;
    public SignalIndicator signalIndicator = new SignalIndicator();
    public MessageChannel myMessages;
    public Iterator<Thread> iterator;
    private boolean isLeaderElected;
    FileWriter output_file = new FileWriter("src/output.dat");
    BufferedWriter writer = new BufferedWriter(output_file);

    public MasterProcess(int[] process_ids) throws IOException {
        this.process_ids = process_ids;
        this.myMessages = new MessageChannel(process_ids.length);
        this.isLeaderElected = false;
        writer.write("");
    }

    /**
     * Creates all the process threads with their respective UIDs mentioned in the input file.
     * Each round, a message is sent from one process to its adjacent process until the original UID is received by its orginal process which matches with that UID.
     * Thus electing the leader and printing the same.
     * @throws InterruptedException
     */
    public void spawnProcesses() throws InterruptedException{
        myProcesses = new HashSet<>();
        for(int i=0;i<process_ids.length;i++){
            try
            {
                Thread process = new Thread(new Process(i,process_ids[i],signalIndicator,myMessages));
                process.setName(Integer.toString(process_ids[i]));
                signalIndicator.increment();
                process.start();
                myProcesses.add(process);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

        while(!isLeaderElected){
            while(!signalIndicator.isRoundCompleted()){
                Thread.sleep(10);
            }
            for(int k=0;k<myMessages.newMessageList.length;k++)
                myMessages.messageList[k] = myMessages.newMessageList[k];
            Arrays.fill(myMessages.newMessageList,"");
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

    /**
     * Spawning the processes using spawnProcesses method
     */
    public void run(){
        try{
            spawnProcesses();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

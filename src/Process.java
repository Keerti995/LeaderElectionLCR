import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Process class is a thread class which receives the message from the previous process and checks if the UID is greater that itself,
 * if so then proceeds to send it further else discards and waits to finish the round.
 * This process is continued in rounds until a leader is elected amongst all the other processes.
 */
public class Process implements Runnable{
    int processId;
    private int processIndex;
    SignalIndicator signalIndicator;
    private MessageChannel myMessages;
    private String sendMessage;
    private boolean isLeaderElected;

    FileWriter output_file = new FileWriter("src/output.dat",true);
    BufferedWriter writer = new BufferedWriter(output_file);

    public Process(int id,int pid,SignalIndicator signal,MessageChannel messages) throws IOException {
        processIndex = id;
        processId = pid;
        signalIndicator = signal;
        myMessages = messages;
        sendMessage = Integer.toString(pid);
        isLeaderElected = false;
        writer.write("");
    }

    /**
     * receives the message from the previous process and checks if the UID is greater that itself,
     * if so then proceeds to send it further else discards and waits to finish the round.
     * This process is continued in rounds until a leader is elected amongst all the other processes.
     */
    public void run(){
        while(!isLeaderElected){
            String incomingMessage = myMessages.getInputMessage(processIndex);
            if(incomingMessage.length() >0){
                if(incomingMessage.contains("LEADER_ELECTED")){
                    isLeaderElected = true;
                    sendMessage = incomingMessage;
                    String leaderPID = incomingMessage.split(" ")[1];
                    System.out.println("My process ID: "+processId+" Leader ID: "+leaderPID);
                    try {
                        writer.write("My process ID: "+processId+" Leader ID: "+leaderPID);
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    int v = Integer.parseInt(incomingMessage);
                    if(v > processId)
                        sendMessage = incomingMessage;
                    else if(v == processId) {
                        sendMessage = "LEADER_ELECTED " + incomingMessage;
                        isLeaderElected = true;
                        System.out.println("I am the Leader and my ID is "+processId);
                        try {
                            writer.write("I am the Leader and my ID is "+processId);
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            myMessages.sendOutputMessage(processIndex,sendMessage);
            signalIndicator.decrement();
            sendMessage = "";
            if(!isLeaderElected){
                synchronized (signalIndicator){
                    try{
                        signalIndicator.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

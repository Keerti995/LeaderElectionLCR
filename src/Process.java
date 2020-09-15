public class Process implements Runnable{
    int processId;
    private int processIndex;
    SignalIndicator signalIndicator;
    private MessageChannel myMessages;
    private String sendMessage;
    private boolean isLeaderElected;

    public Process(int id,int pid,SignalIndicator signal,MessageChannel messages){
        processIndex = id;
        processId = pid;
        signalIndicator = signal;
        myMessages = messages;
        sendMessage = Integer.toString(pid);
        isLeaderElected = false;
    }

    public void run(){
        while(!isLeaderElected){
            String incomingMessage = myMessages.getInputMessage(processIndex);
            if(incomingMessage.length() >0){
                if(incomingMessage.contains("LEADER_ELECTED")){
                    isLeaderElected = true;
                    sendMessage = incomingMessage;
                    String leaderPID = incomingMessage.split(" ")[1];
                    System.out.println("My process ID: "+processId+" Leader ID: "+leaderPID);
                }
                else{
                    int v = Integer.parseInt(incomingMessage);
                    if(v > processId)
                        sendMessage = incomingMessage;
                    else if(v == processId) {
                        sendMessage = "LEADER_ELECTED " + incomingMessage;
                        System.out.println("I am the Leader and my ID is "+processId);
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
    }
}

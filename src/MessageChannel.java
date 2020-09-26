import java.util.Arrays;

/**
 * MessageChannel class holds the messages in order to be consumed by the processes and updates them after each round
 */
public class MessageChannel {
    private int size;
    public String[] messageList;
    public String[] newMessageList;

    /**
     * Creates the empty messageList for all the processes
     * @param size
     */
    public MessageChannel(int size){
        this.size = size;
        messageList = new String[size];
        Arrays.fill(messageList,"");

        newMessageList = new String[size];
        Arrays.fill(newMessageList,"");
    }

    /**
     * message from particular index in the MessageList is read and sent it to the processes to be read
     * @param idx
     * @return
     */
    public String getInputMessage(int idx){
        String message =  messageList[idx];
        messageList[idx] = "";
        return message;
    }

    /**
     * newMessageList being updated by the processes based on the LCR algorithm
     * @param idx
     * @param send
     */
    public void sendOutputMessage(int idx,String send){
        newMessageList[(idx+1)%size] = send;
    }

}

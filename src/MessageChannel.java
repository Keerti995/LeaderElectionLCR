import java.util.Arrays;

public class MessageChannel {
    private int size;
    public String[] messageList;
    public String[] newMessageList;

    public MessageChannel(int size){
        this.size = size;
        messageList = new String[size];
        Arrays.fill(messageList,"");

        newMessageList = new String[size];
        Arrays.fill(newMessageList,"");
    }

    public String getInputMessage(int idx){
        String message =  messageList[idx];
        messageList[idx] = "";
        return message;
    }

    public void sendOutputMessage(int idx,String send){
        newMessageList[(idx+1)%size] = send;
    }

}

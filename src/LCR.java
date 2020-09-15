import java.io.*;

public class LCR  {
    private static int[] process_ids;

    public static void readProcessesInfo() throws IOException{
        File input_file = new File("src/input.dat");
        BufferedReader reader = new BufferedReader(new FileReader(input_file.getAbsolutePath()));
        int process_count = Integer.valueOf(reader.readLine());
        process_ids = new int[process_count];
        for(int i=0;i<process_count;i++) {
            process_ids[i] = Integer.valueOf(reader.readLine());
        }
        reader.close();
    }

    public static void main(String[] args) throws IOException{
        readProcessesInfo();
        Thread MasterProcess = new Thread(new MasterProcess(process_ids));
        MasterProcess.start();
    }
}

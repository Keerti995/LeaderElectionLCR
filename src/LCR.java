import java.io.*;

/**
 * LCR - is the main class which reads the input file regarding the processes information and creates a master process thread to elect a leader among all the process
 */
public class LCR  {
    private static int[] process_ids;

    /**
     * Reads the input file, information regarding the processes count and their UIDs.
     * @throws IOException
     */
    public static void readProcessesInfo() throws IOException{
        File input_file = new File("src/input.dat"); //input file which read the number of processes and their UIDx.
        BufferedReader reader = new BufferedReader(new FileReader(input_file.getAbsolutePath()));
        int process_count = Integer.valueOf(reader.readLine());
        process_ids = new int[process_count];
        for(int i=0;i<process_count;i++) {
            process_ids[i] = Integer.valueOf(reader.readLine());
        }
        reader.close();
    }

    /**
     * Populates the process information and using that Creates a master process thread to elect a leader amongst the processes
     * @param args
     * @throws IOException
     */
    public static void main(String[] args){
        try
        {
            readProcessesInfo();
            Thread MasterProcess = new Thread(new MasterProcess(process_ids)); // Creating a master process thread
            MasterProcess.start();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

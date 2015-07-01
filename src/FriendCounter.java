import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sushantt on 28/06/15.
 */
public class FriendCounter {

    public static void main(String args[]) throws IOException, InterruptedException {

       // Thread.sleep(30000);

        if(args.length < 3)
        {
            System.out.println("Please enter three file names along with path in following order :");
            System.out.println("1. orkut ungraph file");
            System.out.println("2. query file");
            System.out.println("3. output file ");
        }

        Long startTime = System.currentTimeMillis();

        Map<Integer, Integer> edgeFriends = new HashMap<Integer, Integer>(100000);


        FileInputStream fin1 = new FileInputStream(new File(args[1]));
        BufferedReader queryReader = new BufferedReader(new InputStreamReader(fin1));
        String line1 = queryReader.readLine();

        while (line1 != null) {

            String queryNode = line1.trim();
            edgeFriends.put(Integer.parseInt(queryNode), 0);
            line1 = queryReader.readLine();
        }

        fin1.close();
        queryReader.close();


        FileChannel inChannel = new RandomAccessFile(new File(args[0]), "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);

        int number = 0;
        int keynumber =0;

        while(inChannel.read(buffer) > 0) {
            buffer.flip();

            for(int i = 0; i < buffer.limit(); i++) {
                int ch = (int) buffer.get(i);
                if(ch == 12 || ch == 10 || ch == 13) {
                    Integer num = edgeFriends.get(number);
                    if( num != null) {
                            edgeFriends.put(number, num + 1);
                    }
                    num = edgeFriends.get(keynumber);
                    if( num != null) {
                        edgeFriends.put(keynumber, num +1);
                    }
                    number = 0;
                } else if(ch == 9 || ch == 32){

                    if(number != 0) {
                        keynumber = number;
                    }
                    number = 0;
                }
                else {
                    number = number * 10 + (ch - '0');
                }

            }

            buffer.clear();
        }


        FileOutputStream fout = new FileOutputStream(new File(args[2]));
        BufferedWriter queryWriter = new BufferedWriter(new OutputStreamWriter(fout));

        int totalCount = 0;
        for(Integer query : edgeFriends.keySet()) {
            totalCount++;
            queryWriter.write(query + " : " + edgeFriends.get(query) + "\n");
        }

        System.out.println(totalCount);
        queryWriter.flush();
        queryWriter.close();
        fout.flush();
        fout.close();

        System.out.println("Time : " + ((System.currentTimeMillis() - startTime) / 1000));

    }
}

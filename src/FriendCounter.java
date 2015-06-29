import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sushantt on 28/06/15.
 */
public class FriendCounter {

    public static void main(String args[]) throws IOException, InterruptedException {

        Thread.sleep(30000);

        if(args.length < 3)
        {
            System.out.println("Please enter three file names along with path in following order :");
            System.out.println("1. orkut ungraph file");
            System.out.println("2. query file");
            System.out.println("3. output file ");
        }

        Map<String, AtomicInteger> edgeFriends = new HashMap<String, AtomicInteger>();
        Map<String, Integer> queryFriends = new HashMap<String, Integer>();

        Long startTime = System.currentTimeMillis();

        FileChannel inChannel = new RandomAccessFile(new File(args[0]), "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1204);

       Charset charset = Charset.forName("ASCII");
       CharsetDecoder decoder = charset.newDecoder();

        char[] line = new char[2048];
        //byte[] line = new byte[1024];
        int lineIndex = 0;
        String node;
        while(inChannel.read(buffer) > 0) {
         //   buffer.flip();
           CharBuffer charBuffer = decoder.decode(buffer);


           for(int i = 0; i < charBuffer.limit(); i++) {
                char ch = charBuffer.get(i);
          //  for(int i =0; i < buffer.limit(); i++) {
            //    byte ch = buffer.get(i);
              //  String hex = Integer.(ch);
               // System.out.println(hex);
                if(ch == '\n' || ch == '\r') {

                    lineIndex=0;
                    Arrays.fill(line, ' ');

                } else if(ch == '\t' || ch == ' '){
                    node = String.valueOf(line).trim();

                    AtomicInteger autoint = edgeFriends.get(node);
                    if(autoint == null)
                        autoint = new AtomicInteger(0);
                    edgeFriends.put(node.trim(), new AtomicInteger(autoint.incrementAndGet()));
                   // System.out.println(node + " : " + autoint.get());
                    lineIndex=0;
                    Arrays.fill(line, ' ');
                }
                else {
                    //System.out.print(ch);
                   line[lineIndex] = ch;
                    lineIndex = lineIndex + 1;
                   // System.out.print(lineIndex);
                }

            }

            buffer.clear();
        }



        /* Slower one.

        FileInputStream fin = new FileInputStream(new File("/Users/sushantt/Documents/Talentica/RandD/com-orkut.ungraph.txt"));
        //BufferedReader edgeReader = new BufferedReader(new InputStreamReader(fin));
        Scanner edgeReader = new Scanner(fin, "UTF-8");


        while(edgeReader.hasNextLine()) {

            String[] nodes = edgeReader.nextLine().split("\\s+");
            if(nodes!=null && nodes.length > 1) {
                String node = nodes[0];
                AtomicInteger autoint = edgeFriends.get(node);
                if(autoint == null)
                    autoint = new AtomicInteger(0);
                edgeFriends.put(node.trim(), new AtomicInteger(autoint.incrementAndGet()));
            }
        }

        System.out.println("Time : " + ((System.currentTimeMillis() - startTime) / 1000));
        edgeReader.close();
        fin.close();*/

        FileInputStream fin1 = new FileInputStream(new File(args[1]));
        BufferedReader queryReader = new BufferedReader(new InputStreamReader(fin1));

        FileOutputStream fout = new FileOutputStream(new File(args[2]));
        BufferedWriter queryWriter = new BufferedWriter(new OutputStreamWriter(fout));

        String line1 = queryReader.readLine();

        while (line1 != null) {

            String queryNode = line1.trim();
            if(edgeFriends.get(queryNode) != null) {
                line1 = queryReader.readLine();
                queryFriends.put(queryNode, edgeFriends.get(queryNode).get());
                continue;
            }

            queryFriends.put(queryNode, 0);

            line1 = queryReader.readLine();
        }

        int totalCount = 0;
        for(String query : queryFriends.keySet()) {

          //  System.out.println(query + " : " + edgeFriends.get(query).get());
            totalCount++;
            queryWriter.write(query + " : " + queryFriends.get(query) + "\n");
        }

        System.out.println(totalCount);
        queryWriter.flush();
        queryWriter.close();
        fout.flush();
        fout.close();

        fin1.close();
        queryReader.close();




        System.out.println("Time : " + ((System.currentTimeMillis() - startTime) / 1000));

    }
}

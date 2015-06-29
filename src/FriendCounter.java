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

        Map<Integer, Integer> edgeFriends = new HashMap<Integer, Integer>();
        Map<Integer, Integer> queryFriends = new HashMap<Integer, Integer>();

        Long startTime = System.currentTimeMillis();

        FileChannel inChannel = new RandomAccessFile(new File(args[0]), "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1204);

       //Charset charset = Charset.forName("US-ASCII");
       //CharsetDecoder decoder = charset.newDecoder();

       // char[] line = new char[1048];
      //  byte[] line = new byte[1024];
        int number = 0;
        int key=0;
        int lineIndex = 1;
        Integer node;
        while(inChannel.read(buffer) > 0) {
            buffer.flip();
         //  CharBuffer charBuffer = decoder.decode(buffer);


           for(int i = 0; i < buffer.limit(); i++) {
                Byte by = buffer.get(i);
               int ch = by.intValue();
          //  for(int i =0; i < buffer.limit(); i++) {
            //    byte ch = buffer.get(i);
              //  String hex = Integer.(ch);
               // System.out.println(hex);
                if(ch == 12 || ch == 10 || ch == 13) {

                    number = 0;
                    lineIndex=1;
                   // Arrays.fill(line, (byte) 32);

                } else if(ch == 9 || ch == 32){

                    if(number != 0)
                        key = number;
                 //  System.out.println(key);
                    Integer autoint = edgeFriends.get(key);
                    if(autoint == null)
                        autoint = 0;
                    edgeFriends.put(key, autoint + 1);
                   // System.out.println(node + " : " + autoint.get());
                    lineIndex=1;
                    number = 0;
                   // Arrays.fill(line, (byte) 32);
                }
                else {
                    //System.out.print(ch);
                    //line[lineIndex] = by;

                  //  if(ch == 48 || ch == 49 || ch == 50 || ch == 51 || ch == 52 || ch == 53 || ch == 54 || ch == 55 || ch == 56 || ch == 57  )
                    {
                       number = number * 10 + (ch - '0');
                      //  System.out.println(number);
                    }
                    //else {
                      //  System.out.println(ch);
                    //}
                    lineIndex = lineIndex + 1;
                   // System.out.print(lineIndex);
                }

            }

            buffer.clear();
            //buffer.flip();
        }



        /* Slower one. *

        Long startTime = System.currentTimeMillis();

        FileInputStream fin = new FileInputStream(new File("/Users/sushantt/Documents/Talentica/RandD/com-orkut.ungraph.txt"));
        //BufferedReader edgeReader = new BufferedReader(new InputStreamReader(fin));
        Scanner edgeReader = new Scanner(fin, "UTF-8");
        Map<String, AtomicInteger> edgeFriends = new HashMap<String, AtomicInteger>();
        Map<String, Integer> queryFriends = new HashMap<String, Integer>();

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
        fin.close(); */

        FileInputStream fin1 = new FileInputStream(new File(args[1]));
        BufferedReader queryReader = new BufferedReader(new InputStreamReader(fin1));

        FileOutputStream fout = new FileOutputStream(new File(args[2]));
        BufferedWriter queryWriter = new BufferedWriter(new OutputStreamWriter(fout));

        String line1 = queryReader.readLine();

        while (line1 != null) {

            String queryNode = line1.trim();
            Integer parsedNode = Integer.parseInt(queryNode);
            if(edgeFriends.get(parsedNode) != null) {
                line1 = queryReader.readLine();
                queryFriends.put(parsedNode, edgeFriends.get(parsedNode));
                continue;
            }

            queryFriends.put(parsedNode, 0);

            line1 = queryReader.readLine();
        }

        int totalCount = 0;
        for(Integer query : queryFriends.keySet()) {

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

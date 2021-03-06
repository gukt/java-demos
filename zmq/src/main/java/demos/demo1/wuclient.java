package demos.demo1;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZThread;

import java.util.StringTokenizer;

/**
 * Created by ktgu on 15/10/4.
 */
public class wuclient {

    public static void main (String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting updates from weather server");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5556");

        //  Subscribe to zipcode, default is NYC, 10001
        String filter = (args.length > 0) ? args[0] : "19";
        subscriber.subscribe(filter.getBytes());

        //  Process 100 updates
        int update_nbr;
        long total_temp = 0;
        for (update_nbr = 0; update_nbr < 100; update_nbr++) {
            //  Use trim to remove the tailing '0' character
            String string = subscriber.recvStr(0).trim();

            StringTokenizer sscanf = new StringTokenizer(string, " ");
            int zipcode = Integer.valueOf(sscanf.nextToken());
            int temperature = Integer.valueOf(sscanf.nextToken());
            int relhumidity = Integer.valueOf(sscanf.nextToken());

            System.out.println("processing the content:" + string);


            total_temp += temperature;

        }
        System.out.println("Average temperature for zipcode '"
                + filter + "' was " + (int) (total_temp / update_nbr));

        subscriber.close();
        context.term();
    }
}
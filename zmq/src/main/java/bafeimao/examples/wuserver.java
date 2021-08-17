package bafeimao.examples;

import org.zeromq.ZMQ;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by ktgu on 15/10/4.
 */
public class wuserver {

    public static void main (String[] args) throws Exception {
        //  Prepare our context and publisher
        ZMQ.Context context = ZMQ.context(1);

        int port = args.length == 0? 5556: Integer.parseInt(args[0]);

        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:" + port);
        publisher.bind("ipc://weather");

        //  Initialize random number generator
        Random srandom = new Random(System.currentTimeMillis());
        while (!Thread.currentThread ().isInterrupted ()) {
            //  Get values that will fool the boss
            int zipcode, temperature, relhumidity;
            zipcode = 10000 + srandom.nextInt(10000) ;
            temperature = srandom.nextInt(215) - 80 + 1;
            relhumidity = srandom.nextInt(50) + 10 + 1;

            //  Send message to all subscribers
            String update = String.format("%05d %d %d", zipcode, temperature, relhumidity);
            System.out.println("Update:" + update);

            publisher.send(update, 0);

            TimeUnit.MILLISECONDS.sleep(200);
        }

        publisher.close ();
        context.term ();
    }
}
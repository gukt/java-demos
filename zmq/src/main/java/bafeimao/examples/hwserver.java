package bafeimao.examples;

import org.zeromq.ZMQ;

/**
 * Created by ktgu on 15/10/2.
 */
public class hwserver {
    public static void main(String[] args) throws Exception {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        System.out.println("Server is ready for requests ...");

        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Wait for next request from the client
                byte[] request = responder.recv(0);
                 System.out.println("Received :Hello");

                // Do some 'work'
                Thread.sleep(1000);

                // Send reply back to client
                String reply = "World";
                responder.send(reply.getBytes(), 0);
            }

        }catch (Exception e) {
            System.out.println("Error:" + e);
        }


        responder.close();
        context.term();
    }
}

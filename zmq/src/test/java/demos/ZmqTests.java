package demos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.concurrent.TimeUnit;

public class ZmqTests {
	final ZMQ.Context context = ZMQ.context(2);

	@Test
	public void testBenchmark() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ZMQ.Socket responder = context.socket(ZMQ.REP);
				responder.bind("tcp://*:9999");

				long starTime = System.currentTimeMillis();

				while (!Thread.currentThread().isInterrupted()) {
					try {
						String content = responder.recvStr();
						System.out.println("Received:" + content);

						responder.send("OK".getBytes(), 1);
						System.out.println("here");
					} catch (ZMQException e) {
						throw e;
					}
				}

				System.out.println("total:" + (System.currentTimeMillis() - starTime));
			}
		}).run();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ZMQ.Socket requester = context.socket(ZMQ.REQ);

				System.out.println("Connecting to echo server...");
				requester.connect("tcp://*:9999");

				while (true) {
					requester.send("hello".getBytes(), 0);
				}

			}
		}).run();

	}

	@Test
	public void testStartClient() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REQ);

		System.out.println("Connecting to echo server...");
		socket.connect("tcp://localhost:9999");

		while (true) {
			byte[] reply = socket.recv(0);
			System.out.println("Received reply   [" + new String(reply) + "]");
		}
	}

	@AfterEach
	public void tearDown() throws InterruptedException {
		TimeUnit.SECONDS.sleep(50);
	}
}

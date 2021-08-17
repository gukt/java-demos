package net.bafeimao.examples.network.mina.timesServer;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MinaTimeServer {

	public static void main(String[] args) throws IOException {
		// IoAcceptor acceptor = new NioSocketAcceptor();
		// acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		// acceptor.getFilterChain().addLast("codec",
		// new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		// acceptor.setHandler(new TimeServerHandler());
		// acceptor.getSessionConfig().setReadBufferSize(2048);
		// acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		// acceptor.bind(new InetSocketAddress(PORT));
		// System.out.println("server is started!");
		ApplicationContext context = new ClassPathXmlApplicationContext("mina-time-server.xml");

		System.out.println(context);

	}
}

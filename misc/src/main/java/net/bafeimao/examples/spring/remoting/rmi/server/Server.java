package net.bafeimao.examples.spring.remoting.rmi.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Server {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("spring-examples-remoting-server.xml");
		System.out.println("The remote server was successfully started!");
	}

}

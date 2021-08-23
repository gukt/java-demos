package demos.spring.remoting.rmi.client;

import java.util.List;

import demos.spring.remoting.rmi.common.Account;
import demos.spring.remoting.rmi.common.AccountService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
	public static void main(String[] args) {
		try {
			System.out.println("Starting the client...");

			ApplicationContext context = new ClassPathXmlApplicationContext("spring-examples-remoting-client.xml");

			System.out.println("The client was successfully started!");

			AccountService accountService = context.getBean(AccountService.class);
			List<Account> accounts = accountService.find();
			System.out.println(accounts);

			SimpleObject simpleObject = context.getBean(SimpleObject.class);
			simpleObject.getAccountService().create(new Account("test0"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

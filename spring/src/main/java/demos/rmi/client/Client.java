package demos.rmi.client;

import demos.rmi.common.Account;
import demos.rmi.common.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

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

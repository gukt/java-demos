package net.bafeimao.examples.spring.remoting.rmi.server;

import java.util.Arrays;
import java.util.List;

import net.bafeimao.examples.spring.remoting.rmi.common.Account;
import net.bafeimao.examples.spring.remoting.rmi.common.AccountService;

import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	public void create(Account account) {
		System.out.println("inserting an acount...");
	}

	public List<Account> find() {
		System.out.println("Retrieving all accounts...");
		return Arrays.asList(new Account("test0"), new Account("test1"), new Account("test2"));
	}

	@Override
	public Account findOne(String name) {
		return new Account(name);
	}
}
package net.bafeimao.examples.spring.remoting.rmi.common;

import java.util.List;

public interface AccountService {

	public void create(Account account);

	public List<Account> find();

	public Account findOne(String name);
}
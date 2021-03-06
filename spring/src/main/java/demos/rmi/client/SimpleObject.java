package demos.rmi.client;

import demos.rmi.common.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleObject {

	@Autowired
	private AccountService accountService;

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public AccountService getAccountService() {
		return accountService;
	}
	// additional methods using the accountService

}
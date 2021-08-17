package net.bafeimao.examples.spring.remoting.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import net.bafeimao.examples.spring.remoting.rmi.common.Account;

public interface RemoteAccountService extends Remote {

	public void insertAccount(Account account) throws RemoteException;

	public List<Account> getAccounts(String name) throws RemoteException;

}
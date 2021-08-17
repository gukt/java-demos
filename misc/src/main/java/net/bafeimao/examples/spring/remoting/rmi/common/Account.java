package net.bafeimao.examples.spring.remoting.rmi.common;

import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = -3967864796991779286L;

	public Account(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Account [name=" + name + "]";
	}
}
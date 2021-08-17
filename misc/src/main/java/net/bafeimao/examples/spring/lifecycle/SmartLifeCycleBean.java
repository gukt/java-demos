package net.bafeimao.examples.spring.lifecycle;

import org.springframework.context.SmartLifecycle;

public class SmartLifeCycleBean implements SmartLifecycle {
	public String getName() {
		return "I am a SmartLifeCycleBean";
	}

	public SmartLifeCycleBean() {
		System.out.println("new SmartLifeCycleBean instance....");
	}

	@Override
	public boolean isAutoStartup() {
		return false;
	}

	@Override
	public void stop(Runnable callback) {
		System.out.println("Stoping with callback");
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public void start() {
		System.out.println("The SmartLifeCycleBean is starting...");
	}

	@Override
	public void stop() {
		System.out.println("stop");
	}

	@Override
	public int getPhase() {
		return 10;
	}

}

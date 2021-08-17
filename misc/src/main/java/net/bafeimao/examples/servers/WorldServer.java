package net.bafeimao.examples.servers;

import java.util.concurrent.TimeUnit;

public class WorldServer {

	public static void main(String[] args) {
		System.out.println("World Server is starting...");

		System.out.println("Registering shutdown hook");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("[Hook] Terminated, stopping world server!");

				System.out.println("[Hook] World server stopped!");
			}
		});

		// System.out.println("Add console command supporting");
		//
		// new Thread() {
		// @Override
		// public void run() {
		// try {
		// Scanner scanner = new Scanner(System.in);
		// while (scanner != null && scanner.hasNextLine()) {
		// System.out.println("Press q to quit !!!");
		// String input = scanner.nextLine();
		// if ("q".equals(input)) {
		// scanner.close();
		// System.exit(0);
		// break;
		// }
		// }
		// } catch (Exception e) {
		// System.out.println("#$@@!$#@" + e);
		// }
		// };
		// }.start();

		System.out.println("World Server started!");

		try {
			System.out.println("Sleep 24 hours");
			TimeUnit.HOURS.sleep(24);
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		System.out.println("Week up!");
	}
}

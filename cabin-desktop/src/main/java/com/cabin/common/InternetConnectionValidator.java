package com.cabin.common;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cabin.desktop.PWLauncher;

public class InternetConnectionValidator {

	public static void checkConnection() {
		Long maximunAttempsInMinutes = PWLauncher.connectionData.get(Parameter.MAXIMUM_DISCONNECTION_TIME);
		System.out.println(Thread.currentThread().getId() + " in check connection: " + maximunAttempsInMinutes);

		AtomicInteger attemptsInMinutes = new AtomicInteger(0);
		ScheduledExecutorService schExService = Executors.newScheduledThreadPool(1);
		schExService.scheduleAtFixedRate(() -> {
			System.out.println(Thread.currentThread().getId() + " in attemp: " + attemptsInMinutes.get());
			if (isConnected()) {
				attemptsInMinutes.set(0);
			} else {
				attemptsInMinutes.incrementAndGet();
			}
			if (attemptsInMinutes.get() == maximunAttempsInMinutes) {
				System.out.println(Thread.currentThread().getId() + " shutting down");
				schExService.shutdown();
			}
		}, 0, 1, TimeUnit.MINUTES);

	}

	private static boolean isConnected() {
		try {
			URL url = new URL("http://www.instanceofjava.com");
			URLConnection connection = url.openConnection();
			connection.connect();
			System.out.println(Thread.currentThread().getId() + " is connected");
			return true;
		} catch (Exception e) {
			System.out.println(Thread.currentThread().getId() + " is not connected");
			return false;
		}
	}

}

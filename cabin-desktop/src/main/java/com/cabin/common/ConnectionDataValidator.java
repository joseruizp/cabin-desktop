package com.cabin.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cabin.desktop.PWLauncher;

public class ConnectionDataValidator {

	public static void checkDataConnection() {
		Long maximumBlockTimeToClosePrograms = PWLauncher.connectionData.get(Parameter.MAXIMUM_BLOCKED_TIME_TO_CLOSE_PROGRAMS);
		Long maximumBlockTimeToShutDown = PWLauncher.connectionData.get(Parameter.MAXIMUM_BLOCKED_TIME_TO_SHUT_DOWN);
		System.out.println(Thread.currentThread().getId() + " in check connection, maximumBlockTimeToClosePrograms: " + maximumBlockTimeToClosePrograms);
		System.out.println(Thread.currentThread().getId() + " in check connection, maximumBlockTimeToShutDown: " + maximumBlockTimeToShutDown);

		AtomicInteger attemptsInMinutes = new AtomicInteger(0);
		ScheduledExecutorService schExService = Executors.newScheduledThreadPool(1);
		schExService.scheduleAtFixedRate(() -> {
			System.out.println(Thread.currentThread().getId() + " in attemp: " + attemptsInMinutes.get());
			attemptsInMinutes.incrementAndGet();
			if (attemptsInMinutes.get() == maximumBlockTimeToClosePrograms) {
				System.out.println(Thread.currentThread().getId() + " closing programs");
				closePrograms();
			}
			if (attemptsInMinutes.get() == maximumBlockTimeToShutDown) {
				System.out.println(Thread.currentThread().getId() + " shutting down");
				schExService.shutdown();
				shutdownComputer();
			}
		}, 0, 1, TimeUnit.MINUTES);

	}
	
	private static void closePrograms() {
		
	}
	
	private static void shutdownComputer() {
		
	}

}

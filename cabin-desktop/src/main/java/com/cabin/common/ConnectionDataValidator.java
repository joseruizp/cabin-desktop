package com.cabin.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cabin.desktop.PWLauncher;

public class ConnectionDataValidator {
	
	private static final List<String> PROGRAMS_TO_KILL = Arrays.asList("chrome.exe", "iexplorer.exe", "dota.exe", "excel.exe", 
			"winword.exe", "powerpnt.exe");

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
		PROGRAMS_TO_KILL.forEach(p -> {
			try {
				Runtime.getRuntime().exec("TASKKILL /F /IM " + p);
			} catch (IOException e) {
				System.out.println("couldn't kill " + p);
				e.printStackTrace();
			}
		});
	}
	
	private static void shutdownComputer() {
		try {
			Runtime.getRuntime().exec("shutdown -s");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

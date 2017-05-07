package com.cabin.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.cabin.desktop.NotificationDialog;
import com.cabin.desktop.PWLauncher;
import com.cabin.rest.RentRest;

public class InternetConnectionValidator {

	final static Logger logger = Logger.getLogger(InternetConnectionValidator.class);
	private static final String FILE_SEPARATOR = "|";
	private static final String NO_CONNECTION_FILE = "noConnection.txt";
	
	public static void checkConnection(Long clientId, TimerUtil timerUtil, double tariff) {
		Long maximunAttempsInMinutes = PWLauncher.connectionData.get(Parameter.MAXIMUM_DISCONNECTION_TIME);
		logger.debug(Thread.currentThread().getId() + " in check connection: " + maximunAttempsInMinutes);

		AtomicInteger attemptsInMinutes = new AtomicInteger(0);
		ScheduledExecutorService schExService = Executors.newScheduledThreadPool(1);
		schExService.scheduleAtFixedRate(() -> {
			logger.debug(Thread.currentThread().getId() + " in attemp: " + attemptsInMinutes.get());
			if (isConnected()) {
				if (attemptsInMinutes.get() > 0 && attemptsInMinutes.get() < maximunAttempsInMinutes) {
					logger.debug(Thread.currentThread().getId() + " extending minutes: " + attemptsInMinutes.get());
					double hours = TimerUtil.getMinutesAsHours((long) attemptsInMinutes.get());
					double balanceToExtend = TimerUtil.getBalance(TimerUtil.getHoursAsString(hours), tariff);
					logger.debug(Thread.currentThread().getId() + " balance to extend: " + balanceToExtend);
					NotificationDialog.extendTime(balanceToExtend);
			        PWLauncher.updateBalance(balanceToExtend);
				}
				endRentIfFileExists();
				attemptsInMinutes.set(0);
				deleteFile();
			} else {
				attemptsInMinutes.incrementAndGet();
			}
			if (attemptsInMinutes.get() == maximunAttempsInMinutes) {
				logger.info(Thread.currentThread().getId() + " shutting down");
				writeFile(clientId, timerUtil.getTotalHoursUsed(), tariff);
			}
		}, 0, 1, TimeUnit.MINUTES);

	}
	
	private static void endRentIfFileExists() {
		try {
			Path path = Paths.get(PropertiesLoader.DEFAULT_PATH + NO_CONNECTION_FILE);
			if (Files.exists(path)) {
				List<String> lines = Files.readAllLines(path);
				lines.stream().forEach(l ->  {
					String[] split = l.split(FILE_SEPARATOR);
					new RentRest().endRentComputer(new Long(split[0]), split[1], split[2]);	
				});
			}
		} catch (IOException e) {
			logger.debug(Thread.currentThread().getId() + " file can not be read");
		}
	}

	private static void deleteFile() {
		Path path = Paths.get(PropertiesLoader.DEFAULT_PATH + NO_CONNECTION_FILE);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.debug(Thread.currentThread().getId() + " file can not be deleted");
		}
	}

	private static void writeFile(Long clientId, double balanceUsed, double tariff) {
		Path path = Paths.get(PropertiesLoader.DEFAULT_PATH + NO_CONNECTION_FILE);
		try (BufferedWriter writer = Files.newBufferedWriter(path)) 
		{
		    writer.write(clientId + FILE_SEPARATOR + balanceUsed + FILE_SEPARATOR + tariff);
		} catch (IOException e) {
			logger.debug(Thread.currentThread().getId() + " file can not be written");
		}
	}

	private static boolean isConnected() {
		try {
			URL url = new URL("http://www.instanceofjava.com");
			URLConnection connection = url.openConnection();
			connection.connect();
			logger.debug(Thread.currentThread().getId() + " is connected");
			return true;
		} catch (Exception e) {
			logger.error(Thread.currentThread().getId() + " is not connected");
			return false;
		}
	}

}

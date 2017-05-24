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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cabin.desktop.NotificationDialog;
import com.cabin.desktop.PWLauncher;
import com.cabin.rest.RentRest;

public class InternetConnectionValidator {

	final static Logger logger = Logger.getLogger(InternetConnectionValidator.class);
	private static final String FILE_SEPARATOR = "|";
	private static final String NO_CONNECTION_FILE = "noConnection.txt";
	
	public static void checkConnection(final Long rentId, final double tariff) {
		Long maximunAttempsInMinutes = PWLauncher.connectionData.get(Parameter.MAXIMUM_DISCONNECTION_TIME);
		logger.debug(Thread.currentThread().getId() + " in check connection: " + maximunAttempsInMinutes);

		AtomicInteger minutesDisconnected = new AtomicInteger(0);
		AtomicInteger minutesConsumed = new AtomicInteger(0);
		ScheduledExecutorService schExService = Executors.newScheduledThreadPool(1);
		schExService.scheduleAtFixedRate(() -> {
			logger.debug(Thread.currentThread().getId() + " in attemp: " + minutesDisconnected.get());
			if (isConnected()) {
				if (minutesDisconnected.get() > 0 && minutesDisconnected.get() < maximunAttempsInMinutes) {
					logger.debug(Thread.currentThread().getId() + " minutes consumed before recharging: " + minutesConsumed.get());
					logger.debug(Thread.currentThread().getId() + " extending minutes: " + minutesDisconnected.get());
					double hours = TimerUtil.getMinutesAsHours((long) minutesDisconnected.get());
					double balanceToExtend = TimerUtil.getBalance(TimerUtil.getHoursAsString(hours), tariff);
					logger.debug(Thread.currentThread().getId() + " balance to extend: " + balanceToExtend);
					NotificationDialog.extendTime(balanceToExtend);
			        PWLauncher.updateBalance(balanceToExtend);
			        minutesConsumed.set(minutesConsumed.get() - minutesDisconnected.get());
			        logger.debug(Thread.currentThread().getId() + " minutes consumed after recharging: " + minutesConsumed.get());
				}
				endRentIfFileExists();
				minutesDisconnected.set(0);
				deleteFile();
			} else {
				int attempt = minutesDisconnected.incrementAndGet();
				logger.debug("no connection, attempt: " + attempt);
			}
			minutesConsumed.incrementAndGet();
			
			if (minutesDisconnected.get() == maximunAttempsInMinutes) {
				logger.info(Thread.currentThread().getId() + " maximum number of disconection reached");
				double hoursConsumed = TimerUtil.getMinutesAsHours(new Long(minutesConsumed.get()));
				logger.debug("before writing file, hours consumed: " + hoursConsumed + ", based on minutes consumed: " + minutesConsumed.get());
				writeFile(rentId, hoursConsumed, tariff);
				PWLauncher.stopComputer();
			}
		}, 0, 1, TimeUnit.MINUTES);

	}
	
	public static void main(String[] args) {
		String a = "73|00:04|2.0";
		String b[] = StringUtils.split(a, FILE_SEPARATOR);
		System.out.println(b[0] + " - " + b[1] + " - " + b[2]);
	}
	
	private static void endRentIfFileExists() {
		try {
			Path path = Paths.get(PropertiesLoader.DEFAULT_PATH + NO_CONNECTION_FILE);
			if (Files.exists(path)) {
				List<String> lines = Files.readAllLines(path);
				lines.stream().forEach(l ->  {
					logger.debug("line: " + l);
					String[] split = StringUtils.split(l, FILE_SEPARATOR);
					Long rentId = new Long(split[0]);
					String hoursConsumed = split[1];
					String tariff = split[2];
					logger.debug("data to end rent, rentId: " + rentId + ", hours consumed: " + hoursConsumed + ", tariff: " + tariff);
					new RentRest().endRentComputer(rentId, hoursConsumed, tariff);	
				});
			}
		} catch (final Exception e) {
			logger.error(Thread.currentThread().getId() + " file can not be read. " + e.getMessage(), e);
		}
	}

	private static void deleteFile() {
		Path path = Paths.get(PropertiesLoader.DEFAULT_PATH + NO_CONNECTION_FILE);
		try {
			Files.deleteIfExists(path);
		} catch (final Exception e) {
			logger.error(Thread.currentThread().getId() + " file can not be deleted. " + e.getMessage(), e);
		}
	}

	private static void writeFile(Long rentId, double hoursConsumed, double tariff) {
		logger.info("writing in file, rentId: " + rentId + ", hours consumed: " + hoursConsumed + ", tariff: " + tariff);
		Path path = Paths.get(PropertiesLoader.DEFAULT_PATH + NO_CONNECTION_FILE);
		logger.info("writing file in path: " + path.toUri().getPath());
		try (BufferedWriter writer = Files.newBufferedWriter(path)) 
		{
		    writer.write(rentId + FILE_SEPARATOR + hoursConsumed + FILE_SEPARATOR + tariff);
		    logger.info("file written");
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

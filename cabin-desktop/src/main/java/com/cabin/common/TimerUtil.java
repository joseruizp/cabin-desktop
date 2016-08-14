package com.cabin.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.Timer;

import org.apache.commons.lang.time.DurationFormatUtils;

public class TimerUtil {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Long totalSeconds;
    private Long tempTotalSeconds;
    private Long secondsUsed;
    private Timer timer;

    public TimerUtil(String totalTime, Long secondsUsed, final ActionListener actionListener) {
        super();
        this.totalSeconds = getTotalSeconds(totalTime);
        this.tempTotalSeconds = totalSeconds;
        this.secondsUsed = secondsUsed == null ? 0L : secondsUsed;

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decreaseTime();
                actionListener.actionPerformed(e);
            }
        });
        timer.start();
    }

    private Long getTotalSeconds(String totalTime) {
        try {
            String[] split = totalTime.split(":");
            if (split.length == 2) {
                totalTime = totalTime.concat(":00");
            }
            Date startDate = TIME_FORMAT.parse(totalTime);
            return startDate.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private void decreaseTime() {
        totalSeconds--;
        secondsUsed++;
    }

    public String getRemainingTime() {
        Long miliseconds = (totalSeconds * 1000) - 1000;
        return DurationFormatUtils.formatDuration(miliseconds, "HH:mm:ss", true);
    }

    public Long getSecondsUsed() {
        return secondsUsed;
    }

    public void stop() {
        timer.stop();
    }

    public boolean isOver() {
        return (totalSeconds == 1 || totalSeconds == 0);
    }
    
    public void extendTime(double newTotalHours) {
        Long newTotalSeconds = (long) (newTotalHours * 60.0 * 60.0);
        Long extraTime = newTotalSeconds - this.tempTotalSeconds;
        this.totalSeconds += extraTime;
        this.tempTotalSeconds = this.totalSeconds;
    }

    public static double getTimeAsHours(double balance, double tariff) {
        double rentTime = getTimeToRent(balance, tariff);
        return rentTime;
    }

    private static double getTimeToRent(double balance, double tariff) {
        return round(balance / tariff);
    }

    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        double factorValue = value * factor;
        long tmp = Math.round(factorValue);
        return (double) tmp / factor;
    }

    public static String getHoursAsString(double hours) {
        long hour = (long) hours;
        double fraction = hours - hour;
        String hourString = hour < 10 ? ("0" + hour) : (Long.toString(hour));
        long minutes = Math.round(60 * fraction);
        String minutesString = minutes < 10 ? ("0" + minutes) : (Long.toString(minutes));
        return hourString + ":" + minutesString + ":00";
    }

}

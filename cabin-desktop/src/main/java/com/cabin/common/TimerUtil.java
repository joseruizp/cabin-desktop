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

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    private static final int ONE_MINUTE = 1000 * 60;

    private Long totalMinutes;
    private Long tempTotalMinutes;
    private Long minutesUsed;
    private Timer timer;
    private ActionListener actionListener;

    public TimerUtil(String totalTime, Long minutesUsed, final ActionListener actionListener) {
        super();
        this.totalMinutes = getTotalMinutes(totalTime);
        this.tempTotalMinutes = totalMinutes;
        this.minutesUsed = minutesUsed == null ? 0L : minutesUsed;
        
        this.actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decreaseTime();
                actionListener.actionPerformed(e);
            }
        };

        timer = new Timer(ONE_MINUTE, this.actionListener);
        timer.start();
    }
    
    public void replaceActionListener(final ActionListener actionListener) {
        this.timer.removeActionListener(this.actionListener);
        this.actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decreaseTime();
                actionListener.actionPerformed(e);
            }
        };
        this.timer.addActionListener(this.actionListener);
    }

    private Long getTotalMinutes(String totalTime) {
        try {
            Date startDate = TIME_FORMAT.parse(totalTime);
            return startDate.getTime() / ONE_MINUTE;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private void decreaseTime() {
        totalMinutes--;
        minutesUsed++;
    }

    public String getRemainingTime() {
        Long miliseconds = (totalMinutes * 60 * 1000);
        return DurationFormatUtils.formatDuration(miliseconds, "HH:mm", true);
    }

    public Long getMinutesUsed() {
        return minutesUsed;
    }

    public void stop() {
        timer.stop();
    }
    
    public boolean isShowNotification() {
        return (totalMinutes == 10 || totalMinutes == 5 || totalMinutes == 3);
    }

    public boolean isOver() {
        return (totalMinutes == 0);
    }
    
    public void extendTime(double newTotalHours) {
        Long newTotalMinutes = (long) (newTotalHours * 60.0);
        Long extraTime = newTotalMinutes - this.tempTotalMinutes;
        this.totalMinutes += extraTime;
        this.tempTotalMinutes = this.totalMinutes;
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
        return hourString + ":" + minutesString;
    }

}

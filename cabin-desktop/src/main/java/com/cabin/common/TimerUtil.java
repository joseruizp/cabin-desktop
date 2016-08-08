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
    private Long secondsUsed;
    private Timer timer;

    public TimerUtil(String totalTime, Long secondsUsed, final ActionListener actionListener) {
        super();
        try {
            String[] split = totalTime.split(":");
            if (split.length == 2) {
                totalTime = totalTime.concat(":00");
            }
            Date startDate = TIME_FORMAT.parse(totalTime);
            this.totalSeconds = startDate.getTime() / 1000L;
            this.secondsUsed = secondsUsed == null ? 0L : secondsUsed;

            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    decreaseTime();
                    actionListener.actionPerformed(e);
                }
            });
            timer.start();

        } catch (ParseException e) {
            e.printStackTrace();
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

}

package com.cabin.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.DurationFormatUtils;

public class TimerUtil {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private Long totalSeconds;

    public TimerUtil(String totalTime) {
        super();
        try {
            Date startDate = TIME_FORMAT.parse(totalTime);
            this.totalSeconds = startDate.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getRemainingTime() {
        totalSeconds--;
        Long miliseconds = (totalSeconds * 1000) - 1000;
        return DurationFormatUtils.formatDuration(miliseconds, "HH:mm:ss", true);
    }

}

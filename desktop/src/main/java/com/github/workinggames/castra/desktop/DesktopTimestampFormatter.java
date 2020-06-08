package com.github.workinggames.castra.desktop;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.workinggames.castra.core.statistics.TimestampFormatter;

public class DesktopTimestampFormatter implements TimestampFormatter
{
    private final SimpleDateFormat isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public String getTimestamp(Date date)
    {
        return isoDateFormatter.format(date);
    }
}
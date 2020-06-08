package com.github.workinggames.castra.html;

import java.util.Date;

import com.github.workinggames.castra.core.statistics.TimestampFormatter;
import com.google.gwt.i18n.shared.DateTimeFormat;

public class GwtTimestampFormatter implements TimestampFormatter
{
    private final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public String getTimestamp(Date date)
    {
        return dateTimeFormat.format(date);
    }
}
/*
* Copyright 2015 herd contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.finra.dm.core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Provides additional utility methods for dates and times.
 */
public class DmDateUtils extends DateUtils
{
    public static final long MILLIS_IN_ONE_SECOND = 1000; // 1000 milliseconds
    public static final long MILLIS_IN_ONE_MINUTE = MILLIS_IN_ONE_SECOND * 60; // 1000 milliseconds * 60 seconds
    public static final long MILLIS_IN_ONE_HOUR = MILLIS_IN_ONE_MINUTE * 60; // 1000 milliseconds * 60 seconds * 60 minutes
    public static final long MILLIS_IN_ONE_DAY = MILLIS_IN_ONE_HOUR * 24; // 1000 milliseconds * 60 seconds * 60 minutes * 24 hours

    /**
     * Returns a calendar for the current day that does not have the time set.
     *
     * @return the current date.
     */
    public static Calendar getCurrentCalendarNoTime()
    {
        // Get the current year, month, and day before we clear out the fields.
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        // Clear out ALL the fields of the calendar.  If any are not cleared, we run the risk of something being set that we don't want.
        calendar.clear();

        // Update the calendar with just the year, month, and day.
        calendar.set(year, month, day);

        // Return the updated calendar.
        return calendar;
    }

    /**
     * Formats a duration in milliseconds.
     *
     * @param duration The duration in milliseconds to format.
     * @param displayMilliseconds Whether to display milliseconds or not.
     *
     * @return The formatted duration.
     */
    public static String formatDuration(long duration, boolean displayMilliseconds)
    {
        long remainingDuration = duration;

        // If the duration is 0, then just return 0 with the correct granularity.
        if (remainingDuration == 0)
        {
            return displayMilliseconds ? "0 Milliseconds" : "0 Seconds";
        }

        // Initialize the result string.
        StringBuilder result = new StringBuilder();

        // Compute each duration separately.
        remainingDuration = processDuration(remainingDuration, MILLIS_IN_ONE_DAY, "Day", result, false);
        remainingDuration = processDuration(remainingDuration, MILLIS_IN_ONE_HOUR, "Hour", result, false);
        remainingDuration = processDuration(remainingDuration, MILLIS_IN_ONE_MINUTE, "Minute", result, false);
        remainingDuration = processDuration(remainingDuration, MILLIS_IN_ONE_SECOND, "Second", result, false);

        // Compute the milliseconds.
        long milliSeconds = remainingDuration;
        if (milliSeconds > 0)
        {
            if (displayMilliseconds)
            {
                // Just display the final millisecond portion no matter what (i.e. the duration is 1).
                processDuration(remainingDuration, 1, "Millisecond", result, true);
            }
            else
            {
                // If no milliseconds are being displayed and the time is less than 1 second, return 0 seconds.
                if (result.length() == 0)
                {
                    result.append("0 Seconds");
                }
            }
        }

        // Return the result.
        return result.toString();
    }

    /**
     * Process a single duration.
     *
     * @param remainingDuration the remaining duration in milliseconds.
     * @param millisPerDuration the number of milliseconds per one duration (e.g. 1000 milliseconds in a single second).
     * @param durationName the duration name (e.g. "Day").
     * @param result the result string.
     * @param displayZeroDuration Flag that indicates whether a "0" duration should be displayed or not.
     *
     * @return the new remaining duration in milliseconds after the current duration is substracted from the original remaining duration.
     */
    private static long processDuration(long remainingDuration, long millisPerDuration, String durationName, StringBuilder result, boolean displayZeroDuration)
    {
        // Compute how many durations (e.g. "5" days).
        long duration = remainingDuration / millisPerDuration;

        // Compute the new remaining duration which is the previous remaining duration - the new duration in milliseconds.
        long newRemainingDuration = remainingDuration - (duration * millisPerDuration);

        // Only append the duration to the result if some time exists in the duration (e.g. we don't add "0 Days").
        if (duration > 0 || displayZeroDuration)
        {
            // If the result previously had a value, add a comma to separate this duration from the previous durations (e.g. 5 days"," ...).
            if (result.length() > 0)
            {
                result.append(", ");
            }

            // Append the duration along with the duration name (e.g. "5 day").
            result.append(String.valueOf(duration)).append(' ').append(durationName);

            // If the duration is > 1, then make it plural (e.g. 5 day"s").
            if (duration > 1)
            {
                result.append('s');
            }
        }

        // Return the new remaining duration so the calculation can continue.
        return newRemainingDuration;
    }

    /**
     * Gets the current date/time as an XMLGregorianCalendar with the default time zone in the default locale.
     *
     * @return the current date/time.
     */
    public static XMLGregorianCalendar now()
    {
        return getXMLGregorianCalendarValue(null);
    }

    /**
     * Gets an instance of XMLGregorianCalendar class initialized per the specified java.util.Date value. Returns the current date/time if date is null.
     *
     * @param date the java.util.Date value to be converted into XMLGregorianCalendar.
     *
     * @return the XMLGregorianCalendar instance initialized per specified date value
     */
    public static XMLGregorianCalendar getXMLGregorianCalendarValue(Date date)
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        if (date != null)
        {
            gregorianCalendar.setTime(date);
        }

        try
        {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        }
        catch (DatatypeConfigurationException ex)
        {
            throw new IllegalStateException("Failed to create a new instance of DataTypeFactory.", ex);
        }
    }
}

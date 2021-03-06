package seedu.savvytasker.commons.util;

import org.junit.Test;

import seedu.savvytasker.logic.parser.DateParser;
import seedu.savvytasker.logic.parser.DateParser.InferredDate;
import seedu.savvytasker.logic.parser.ParseException;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//@@author A0139915W
public class SmartDefaultDatesTest {

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HHmmss");
    
    @Test
    public void smartDefaultDates_parseStart() {
        DateParser dateParser = new DateParser();
        InferredDate inferredStart = null;
        try {
            //use MM-dd-yyyy
            inferredStart = dateParser.parseSingle("12/31/2016");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        InferredDate inferredEnd = null;
        SmartDefaultDates sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // specifying only start date, assumed to on the given date at 12am
        // and to end on the given date at 2359:59
        assertStartEndEquals(
                getDate("31/12/2016 000000"), getDate("31/12/2016 235959"), 
                sdd.getStartDate(), sdd.getEndDate());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = today(0, 0);
        try {
            //use MM-dd-yyyy
            inferredStart = dateParser.parseSingle("3pm");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // specifying only start time, assumed to start today at the given time
        // and to end today 2359:59
        assertStartEndEquals(
                getDate(sdf.format(today) + " 150000"), getDate(sdf.format(today) + " 235959"), 
                sdd.getStartDate(), sdd.getEndDate());
    }

    @Test
    public void smartDefaultDates_parseEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = today(0, 0);
        DateParser dateParser = new DateParser();
        InferredDate inferredEnd = null;
        try {
            //use MM-dd-yyyy
            inferredEnd = dateParser.parseSingle("12/31/2016");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        InferredDate inferredStart = null;
        SmartDefaultDates sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // specified only the end date, assumed to start today at 12am
        // and to end on the given date at 2359:59
        assertStartEndEquals(
                getDate(sdf.format(today) + " 000000"), getDate("31/12/2016 235959"), 
                sdd.getStartDate(), sdd.getEndDate());
        
        try {
            //use MM-dd-yyyy
            inferredEnd = dateParser.parseSingle("3pm");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // specified only the end time, assumed to start today at 12am
        // and to end at the given time today
        assertStartEndEquals(
                getDate(sdf.format(today) + " 000000"), getDate(sdf.format(today) + " 150000"), 
                sdd.getStartDate(), sdd.getEndDate());

        
        try {
            //use MM-dd-yyyy
            inferredEnd = dateParser.parseSingle("12/31/2000");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // specified only the end date in the past, start date will be null
        // and to end on the given date at 2359:59
        assertStartEndEquals(
                null, getDate("31/12/2000 235959"), 
                sdd.getStartDate(), sdd.getEndDate());
    }

    @Test
    public void smartDefaultDates_parseStartEnd() {
        // START_TIME
        // date not supplied -> today
        // time not supplied -> 0000
        // END_TIME
        // date not supplied -> today
        // time not supplied -> 2359
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = today(0, 0);
        DateParser dateParser = new DateParser();
        InferredDate inferredStart = null;
        InferredDate inferredEnd = null;
        try {
            //use MM-dd-yyyy
            inferredStart = dateParser.parseSingle("12/31/2016");
            inferredEnd = dateParser.parseSingle("12/31/2016");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        SmartDefaultDates sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // no time supplied for start and end
        // start defaults to 0000
        // end defaults to 2359:59
        assertStartEndEquals(
                getDate("31/12/2016 000000"), getDate("31/12/2016 235959"), 
                sdd.getStartDate(), sdd.getEndDate());

        inferredStart = null;
        inferredEnd = null;
        try {
            //use MM-dd-yyyy
            inferredStart = dateParser.parseSingle("12/31/2016");
            inferredEnd = dateParser.parseSingle("12/30/2016");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // no time supplied for start and end, end date earlier than start
        // start defaults to 0000
        // end defaults to 2359:59
        // no restrictions imposed on end time earlier than start time
        assertStartEndEquals(
                getDate("31/12/2016 000000"), getDate("30/12/2016 235959"), 
                sdd.getStartDate(), sdd.getEndDate());

        inferredStart = null;
        inferredEnd = null;
        try {
            //use MM-dd-yyyy
            inferredStart = dateParser.parseSingle("10am");
            inferredEnd = dateParser.parseSingle("10pm");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // no date supplied for start and end
        // start and end defaults to the given time today
        assertStartEndEquals(
                getDate(sdf.format(today) + " 100000"), getDate(sdf.format(today) + " 220000"), 
                sdd.getStartDate(), sdd.getEndDate());

        inferredStart = null;
        inferredEnd = null;
        try {
            //use MM-dd-yyyy
            inferredStart = dateParser.parseSingle("10pm");
            inferredEnd = dateParser.parseSingle("10am");
        } catch (ParseException e) {
            assert false; //won't get here
        }
        sdd = new SmartDefaultDates(inferredStart, inferredEnd);
        // no date supplied for start and end, end time ends before start time
        // start and end defaults to the given time today
        // no restrictions imposed on end time being earlier
        assertStartEndEquals(
                getDate(sdf.format(today) + " 220000"), getDate(sdf.format(today) + " 100000"), 
                sdd.getStartDate(), sdd.getEndDate());
    }

    @Test
    public void smartDefaultDates_defaultParse() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = today(0, 0);
        DateParser dateParser = new DateParser();
        SmartDefaultDates sdd = new SmartDefaultDates(null, null);
        Date actualStart = sdd.getStart(dateParser.new InferredDate(new Date(), true, true));
        Date actualEnd = sdd.getEnd(dateParser.new InferredDate(new Date(), true, true));
        assertStartEndEquals(null, null, 
                actualStart, actualEnd);
        
        try {
            //use MM-dd-yyyy
            actualStart = sdd.getStart(dateParser.parseSingle("10pm"));
            actualEnd = sdd.getEnd(dateParser.parseSingle("10am"));
        } catch (ParseException e) {
            assert false; //won't get here
        }
        assertStartEndEquals(
                getDate(sdf.format(today) + " 220000"), getDate(sdf.format(today) + " 100000"), 
                actualStart, actualEnd);
        
        try {
            //use MM-dd-yyyy
            actualStart = sdd.getStart(dateParser.parseSingle("12/31/2016"));
            actualEnd = sdd.getEnd(dateParser.parseSingle("12/31/2016"));
        } catch (ParseException e) {
            assert false; //won't get here
        }
        assertStartEndEquals(getDate("31/12/2016 000000"), getDate("31/12/2016 235959"), 
                actualStart, actualEnd);
    }
    
    private void assertStartEndEquals(Date expectedStart, Date expectedEnd,
            Date actualStart, Date actualEnd) {
        assertEquals(expectedStart, actualStart);
        assertEquals(expectedEnd, actualEnd);
    }

    private Date getDate(String ddmmyyyy) {
        try {
            return format.parse(ddmmyyyy);
        } catch (Exception e) {
            assert false; //should not get an invalid date....
        }
        return null;
    }
    
    private Date today(int hours_24, int minute_60) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hours_24);
        calendar.set(Calendar.MINUTE, minute_60);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
//@@author

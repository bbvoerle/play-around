import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class PlutoInAquarius {

    private static final String PLUTO_IN_AQUARIUS_DATA_FILE = "pluto_in_aquarius.txt";

    @Test
    public void analyse() throws IOException {
        List<String> lines = readResourceFileAsList(PLUTO_IN_AQUARIUS_DATA_FILE);
        List<PlutoInSignData> plutoInSignDataList = lines.stream()
                        .map(this::parseLine)
                                .collect(Collectors.toList());
        for(int i = 0; i < plutoInSignDataList.size() - 1; i++) {
            long days = ChronoUnit.DAYS.between(plutoInSignDataList.get(i).startDate, plutoInSignDataList.get(i+1).startDate);
            plutoInSignDataList.get(i).setPeriodInDays(days);
        }

        Map<String, List<PlutoInSignData>> map = plutoInSignDataList.stream().collect(groupingBy(d -> d.getSign()));

        // per planet print data
        long shortestPeriod = 200000l;
        long longestPeriod = 0l;
        for(String sign : map.keySet()) {
            System.out.println(sign);
            List<PlutoInSignData> signList =  map.get(sign);
            int numberOfTimesInSign = signList.size();
            long totalNumberDaysInSign = signList.stream()
                            .map(PlutoInSignData::getPeriodInDays)
                            .reduce(0l, Long::sum);

            long shortest = signList.stream()
                    .map(PlutoInSignData::getPeriodInDays)
                            .sorted().findFirst().get();
            if(shortest < shortestPeriod){
                shortestPeriod = shortest;
            }

            long longest = signList.stream()
                    .map(PlutoInSignData::getPeriodInDays)
                    .sorted(Collections.reverseOrder())
                    .findFirst().get();
            if(longest > longestPeriod){
                longestPeriod = longest;
            }



            System.out.println("Number of times in this sign: " + numberOfTimesInSign);
            System.out.println("Total number of days in this sign: " + totalNumberDaysInSign);

            System.out.println();
            System.out.println();
        }
        System.out.println(shortestPeriod);
        System.out.println(longestPeriod);
    }

    private List<String> readResourceFileAsList(String filename) throws IOException {
        URL url = Resources.getResource(filename);
        return Files.readLines(new File(url.getFile()), Charset.defaultCharset());
    }

    @Test
    public void parseLineTest(){
        PlutoInSignData plutoInSignData = parseLine("|MAR 25, 2269 | 08:52 PM | AQ|");
        Assert.assertNotNull(plutoInSignData);
        Assert.assertEquals(2269, plutoInSignData.startDate.get(ChronoField.YEAR));
        Assert.assertEquals("AQ", plutoInSignData.getSign());
    }

    @Test
    public void splitLineTest(){
        List<String> stringList = splitLine("|MAR 25, 2269 | 08:52 PM | AQ|");
        Assert.assertNotNull(stringList);
        Assert.assertEquals(3, stringList.size());
    }

    @Test
    public void test(){
        DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient()
                .append(DateTimeFormatter.ofPattern("MMM dd, yyyy")).toFormatter(Locale.ENGLISH);
        LocalDate datetime = LocalDate.parse("DEC 08, 1", f);
        System.out.println(datetime);
    }

    private List<String> splitLine(String line) {
        String[] array = line.split("\\|");
        return Arrays.stream(array)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    // string like "|MAR 25, 2269 | 08:52 PM | AQ|"
    private PlutoInSignData parseLine(String line){
        List<String> stringList = splitLine(line);
        String dateTimeString = stringList.get(0) + ", " + stringList.get(1);
        dateTimeString = dateTimeString.replaceAll("   ", " ");
        dateTimeString = dateTimeString.replaceAll("  ", " ");
        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient()
                .append(DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a")).toFormatter(Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, dtf);
        return new PlutoInSignData(dateTime, stringList.get(2));
    }



    private class PlutoInSignData {
        private LocalDateTime startDate;
        private String sign;
        private long periodInDays;

        public PlutoInSignData(LocalDateTime startDate, String sign) {
            this.startDate = startDate;
            this.sign = sign;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public long getPeriodInDays() {
            return periodInDays;
        }

        public void setPeriodInDays(long periodInDays) {
            this.periodInDays = periodInDays;
        }
    }
}

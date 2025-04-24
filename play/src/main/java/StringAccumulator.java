import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class StringAccumulator {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_DELIMITER = "\n";
    private static final List<String> DEFAULT_DELIMITERS = Arrays.asList(COMMA_DELIMITER, NEW_LINE_DELIMITER);

    public int add(String numbers){
        if(numbers == null || numbers.trim().length() == 0){
            return 0;
        }

        List<String> delimiterList = extractDelimiters(numbers);
        List<Integer> numberList = extractNumbersWithNewLineDelimiter(numbers);
        return numberList.stream()
                .collect(Collectors.summingInt(i -> i));
    }

    private List<String> extractDelimiters(String numbers) {
        if(!numbers.startsWith("//")){
            return DEFAULT_DELIMITERS;
        }
        String delimitersString = numbers.substring(2, numbers.indexOf("\n"));

        return null;
    }

    private List<Integer> extractNumbers(String numbers) {
        return Arrays.stream(numbers.split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
    }

    private List<Integer> extractNumbersWithNewLineDelimiter(String numbers) {
        return Arrays.stream(numbers.split(","))
                .map(s -> s.split("\n"))
                .flatMap(arrays -> Arrays.stream(arrays))
                .map(Integer::parseInt).collect(Collectors.toList());
    }

    List<Integer> extractNumbersUsingMultipleDelimiters(String numbers, List<String> delimiterList) {
        List<String> numbersList = new ArrayList<>();
        numbersList.add(numbers);
        for(String delimiter : delimiterList){
            List<String> splittedStringsList = new ArrayList();
            for(String number : numbersList){
                splittedStringsList.addAll(splitByDelimiter(number, delimiter));
            }
            numbersList = splittedStringsList;
        }
        return numbersList.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    List<Integer> extractNumbersUsingMultipleDelimitersWithStream(String numbers, List<String> delimiterList) {
        Stream<String> stringStream = singletonList(numbers).stream();
        for(String delimiter : delimiterList){
            stringStream = stringStream
                    .map(s -> s.split(delimiter))
                    .flatMap(arrays -> Arrays.stream(arrays));
        }
        return stringStream.map(Integer::parseInt).collect(Collectors.toList());
    }

    List<String> splitByDelimiter(String numbers, String delimiter) {
        return Arrays.stream(numbers.split(delimiter))
                .collect(Collectors.toList());
    }
}

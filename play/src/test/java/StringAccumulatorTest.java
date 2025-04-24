import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringAccumulatorTest {

    @Test
    public void addTest(){
        StringAccumulator stringAccumulator = new StringAccumulator();
        int result = stringAccumulator.add(null);
        Assert.assertEquals(0, result);

        result = stringAccumulator.add("   ");
        Assert.assertEquals(0, result);

        result = stringAccumulator.add("1,2,3");
        Assert.assertEquals(6, result);

        result = stringAccumulator.add("1\n2,3");
        Assert.assertEquals(6, result);

        result = stringAccumulator.add("1\n2,3\n4,5,6");
        Assert.assertEquals(21, result);
    }

    @Test
    public void extractNumbersUsingMultipleDelimitersTest(){
        StringAccumulator stringAccumulator = new StringAccumulator();
        List<Integer> result = stringAccumulator.extractNumbersUsingMultipleDelimiters("1\n2,3", Arrays.asList(",", "\n"));
        Assert.assertEquals(3, result.size());

        result = stringAccumulator.extractNumbersUsingMultipleDelimiters("1\n2,3\n4;5;6", Arrays.asList(",", "\n", ";"));
        Assert.assertEquals(6, result.size());
    }

    @Test
    public void extractNumbersUsingMultipleDelimitersWithStreamTest(){
        StringAccumulator stringAccumulator = new StringAccumulator();
        List<Integer> result = stringAccumulator.extractNumbersUsingMultipleDelimitersWithStream("1\n2,3", Arrays.asList(",", "\n"));
        Assert.assertEquals(3, result.size());

        result = stringAccumulator.extractNumbersUsingMultipleDelimitersWithStream("1\n2,3\n4;5;6", Arrays.asList(",", "\n", ";"));
        Assert.assertEquals(6, result.size());
    }

    @Test
    public void test(){
        String numbers = "//;\n1,2";
        String delimitersString = numbers.substring(2, numbers.indexOf("\n"));
        System.out.println(delimitersString);
    }
}

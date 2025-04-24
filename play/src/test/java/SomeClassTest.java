import org.junit.Assert;
import org.junit.Test;

public class SomeClassTest {

    @Test
    public void testSumUp(){
        int x = 3;
        int y = 5;
        SomeClass someClass = new SomeClass();
        int result = someClass.sumUp(x, y);
        Assert.assertEquals(8, result);
    }
}

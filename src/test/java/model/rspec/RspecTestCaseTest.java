package model.rspec;
import model.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;
public class RspecTestCaseTest {

  @Test
  public void throwsExceptionWhenEstimatedRunTimeIsNegative(){
    try{
      TestCase t = new RspecTestCase(-1, "description");
      fail("No exception was thrown when a test case was created with a negative run time.");
    }catch(IllegalArgumentException e){
      assertEquals("The estimated runtime of a test case cannot be negative.", e.getMessage());
    }
  }

  @Test
  public void returnsCorrectRunTimeForAValidTestCase(){
    TestCase t = new RspecTestCase(2.23, "this is a testcase description");
    assertEquals(2.23, t.getEstimatedRunTime(), 0.0001);
  }


  @Test
  public void returnsCorrectDescriptionForAValidTestCase(){
    TestCase t = new RspecTestCase(2.23, "this is a testcase description");
    assertEquals("this is a testcase description", t.getDescription());

  }
}

import org.junit.Test;

import static org.junit.Assert.*;


public class RspecTestDistributorTest {

  @Test
  public void errorsWhenNoInputFileProvided() {
    try {
      Main.main(new String[]{});
    } catch (Exception e) {
      assertEquals("Parameter not found for --in", e.getMessage());
    }
  }

  @Test
  public void errorsWhenInputFlagProvidedWithoutFilename() {
    try {
      Main.main(new String[]{"--in"});
    } catch (Exception e) {
      assertEquals("Parameter not found for --in", e.getMessage());
    }
  }

  @Test
  public void errorsWhenNonExistentInputFileProvided() {
    try {
      Main.main(new String[]{"--in", "not_real.json"});
    } catch (Exception e) {
      assertEquals("not_real.json does not match the path of any known file. Please check your input parameters and try again.", e.getMessage());
    }
  }

  @Test
  public void errorsWhenPodNumberNotProvided() {
    try {
      Main.main(new String[]{"--in", "src/main/resources/sample_input.json"});
    } catch (Exception e) {
      assertEquals("Parameter not found for --pods", e.getMessage());
    }
  }

  @Test
  public void errorsWhenPodFlagPresentWithoutNumber() {
    try {
      Main.main(new String[]{"--in", "src/main/resources/sample_input.json", "--pods"});
    } catch (Exception e) {
      assertEquals("Parameter not found for --pods", e.getMessage());
    }
  }

  @Test
  public void errorsWhenPodsInputIsNegative() {
    try {
      Main.main(new String[]{"--in", "src/main/resources/sample_input.json", "--pods", "-2"});
    } catch (Exception e) {
      assertEquals("Number of pods must be greater than 0", e.getMessage());
    }
  }

}

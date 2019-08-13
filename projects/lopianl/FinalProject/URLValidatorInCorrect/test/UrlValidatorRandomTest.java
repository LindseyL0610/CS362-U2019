import junit.framework.TestCase;

public class UrlValidatorRandomTest extends TestCase {

	   private final boolean printFailureInfo = true; //print additional info to console. Set false to remove

	   public UrlValidatorRandomTest(String testName) {
	      super(testName);
	   }

	   @Override
	protected void setUp() {
		   if (printFailureInfo) {
		   }		   
	   }

	   /**
	    * Create set of tests by taking the testUrlXXX arrays and
	    * running through all possible permutations of their combinations.
	    *
	    * @param testObjects Used to create a url.
	    */
	   public void testIsValid(Object[] testObjects, long options) {
	      UrlValidator urlVal = new UrlValidator(null, null, options);
	      assertTrue(urlVal.isValid("http://www.google.com"));
	      assertTrue(urlVal.isValid("http://www.google.com/"));
	   }
}
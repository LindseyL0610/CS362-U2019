import junit.framework.TestCase;
import java.util.Random;

public class UrlValidatorRandomTest extends TestCase {
	private final boolean printFailureInfo = true; // Print additional info to console. Set false to remove
	private final int numComponents = 5; // Number of "building blocks" used to make up URL (scheme, authority, port, path, query)
	
	// Number of items in provided test arrays
	private final int numSchemes = 8;
	private final int numAuthorities = 20;
	private final int numPorts = 9;
	private final int numPaths = 10;
	private final int numOptionPaths = 15;
	private final int numQueries = 3;	

	public void testIsValidRandom() {
		Random rand = new Random();
		boolean errorFound = false;
		
		for (int numCase = 1; numCase <= 1000; numCase++) {
		
			// Randomize options and initialize UrlValidator
			long initOptions = UrlValidator.ALLOW_ALL_SCHEMES;
			boolean addOptions = rand.nextBoolean();
			if (addOptions) {
				initOptions += UrlValidator.ALLOW_2_SLASHES + UrlValidator.NO_FRAGMENTS;
			}		
			UrlValidator urlVal = new UrlValidator(null, null, initOptions);
			
			// Build array of URL components
			ResultPair[] urlComps = new ResultPair[numComponents];
			urlComps[0] = testUrlScheme[rand.nextInt(numSchemes)];
			urlComps[1] = testUrlAuthority[rand.nextInt(numAuthorities)];
			urlComps[2] = testUrlPort[rand.nextInt(numPorts)];
			if (addOptions) {
				urlComps[3] = testUrlPathOptions[rand.nextInt(numOptionPaths)];
			} else {
				urlComps[3] = testPath[rand.nextInt(numPaths)];
			}
			urlComps[4] = testUrlQuery[rand.nextInt(numQueries)];
			
			// Concatenate URL string and track expected validity based on known validity of component ResultPairs
			StringBuilder urlBuilder = new StringBuilder();
			boolean expected = true;
			for (int i = 0; i < numComponents; i++) {
				urlBuilder.append(urlComps[i].item);
				expected &= urlComps[i].valid;
			}
			String url = urlBuilder.toString();
			
			boolean result = urlVal.isValid(url); // Test isValid with composite url
	
			// Print URL components if error encountered and additional info is desired
			if (printFailureInfo && !errorFound && result != expected) {
				errorFound = true;
				System.out.println("Failure Encountered. Additional Trace Info:");
				System.out.printf("expected: %b, result: %b\n", expected, result);
				if (addOptions) {
					System.out.println("Options: ALLOW_ALL_SCHEMES, ALLOW_2_SLASHES, NO_FRAGMENTS\n");
				} else {
					System.out.println("Options: ALLOW_ALL_SCHEMES\n");
				}
				System.out.println("URL Component Pairs:");
				for (int j = 0; j < numComponents; j++) {
					System.out.printf("(\"%s\" %b), ", urlComps[j].item, urlComps[j].valid);
				}
				System.out.println();				
			}
			
			assertEquals(url, expected, result); // Use junit assert function for regular failure trace		
		}		
	}
	
	
   //---- Test data for creating a composite URL
   //---- Taken from UrlValidatorTest.java as advised in David Mednikov's resolved question on Piazza @48
   /**
    * The data given below approximates the 4 parts of a URL
    * <scheme>://<authority><path>?<query> except that the port number
    * is broken out of authority to increase the number of permutations.
    * A complete URL is composed of a scheme+authority+port+path+query,
    * all of which must be individually valid for the entire URL to be considered
    * valid.
    */
   ResultPair[] testUrlScheme = {new ResultPair("http://", true),
                               new ResultPair("ftp://", true),
                               new ResultPair("h3t://", true),
                               new ResultPair("3ht://", false),
                               new ResultPair("http:/", false),
                               new ResultPair("http:", false),
                               new ResultPair("http/", false),
                               new ResultPair("://", false)};

   ResultPair[] testUrlAuthority = {new ResultPair("www.google.com", true),
                                  new ResultPair("www.google.com.", true),
                                  new ResultPair("go.com", true),
                                  new ResultPair("go.au", true),
                                  new ResultPair("0.0.0.0", true),
                                  new ResultPair("255.255.255.255", true),
                                  new ResultPair("256.256.256.256", false),
                                  new ResultPair("255.com", true),
                                  new ResultPair("1.2.3.4.5", false),
                                  new ResultPair("1.2.3.4.", false),
                                  new ResultPair("1.2.3", false),
                                  new ResultPair(".1.2.3.4", false),
                                  new ResultPair("go.a", false),
                                  new ResultPair("go.a1a", false),
                                  new ResultPair("go.cc", true),
                                  new ResultPair("go.1aa", false),
                                  new ResultPair("aaa.", false),
                                  new ResultPair(".aaa", false),
                                  new ResultPair("aaa", false),
                                  new ResultPair("", false)
   };
   ResultPair[] testUrlPort = {new ResultPair(":80", true),
                             new ResultPair(":65535", true), // max possible
                             new ResultPair(":65536", false), // max possible +1
                             new ResultPair(":0", true),
                             new ResultPair("", true),
                             new ResultPair(":-1", false),
                             new ResultPair(":65636", false),
                             new ResultPair(":999999999999999999", false),
                             new ResultPair(":65a", false)
   };
   ResultPair[] testPath = {new ResultPair("/test1", true),
                          new ResultPair("/t123", true),
                          new ResultPair("/$23", true),
                          new ResultPair("/..", false),
                          new ResultPair("/../", false),
                          new ResultPair("/test1/", true),
                          new ResultPair("", true),
                          new ResultPair("/test1/file", true),
                          new ResultPair("/..//file", false),
                          new ResultPair("/test1//file", false)
   };
   //Test allow2slash, noFragment
   ResultPair[] testUrlPathOptions = {new ResultPair("/test1", true),
                                    new ResultPair("/t123", true),
                                    new ResultPair("/$23", true),
                                    new ResultPair("/..", false),
                                    new ResultPair("/../", false),
                                    new ResultPair("/test1/", true),
                                    new ResultPair("/#", false),
                                    new ResultPair("", true),
                                    new ResultPair("/test1/file", true),
                                    new ResultPair("/t123/file", true),
                                    new ResultPair("/$23/file", true),
                                    new ResultPair("/../file", false),
                                    new ResultPair("/..//file", false),
                                    new ResultPair("/test1//file", true),
                                    new ResultPair("/#/file", false)
   };

   ResultPair[] testUrlQuery = {new ResultPair("?action=view", true),
                              new ResultPair("?action=edit&mode=up", true),
                              new ResultPair("", true)
   };
}
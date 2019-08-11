import org.junit.*;
import java.util.*;
import org.junit.rules.ErrorCollector;

public class UrlValidatorUnitTests {	
	boolean printFailureInfo = true; // set to true to print details about failures to the console
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@Test
	public void testFunc() {	

		for(int a = 0; a < 2; a++) {
			UrlValidator v;
			Stack<String> stack = new Stack<String>();
			Stack<String> errors = new Stack<String>();
			String valSchemes[];
			String testSchemes[];
			ResultPair rp[];
			int numValSchemes;
			
			if(a == 0) rp = p1;
			else rp = p2;
			
			for(int i = 0; i < rp.length; i++) {
				if(rp[i].valid) stack.push(rp[i].item);
			}
			
			numValSchemes = stack.size();
			
			valSchemes = new String[numValSchemes];
			
			for(int i = numValSchemes-1; i >= 0; i--) {
				valSchemes[i] = stack.pop();
			}
			
			v = new UrlValidator(valSchemes);
			
			testSchemes = new String[rp.length*3];
			
			for(int i = 0; i < testSchemes.length; i++) {
				if(i < rp.length) {
					testSchemes[i] = rp[i].item;
				}
				else if(i < rp.length*2) {
					String s = rp[i - rp.length].item;
					
					testSchemes[i] = s.substring(0, 2) + s.substring(2, 3).toUpperCase() + s.substring(3, s.length());
				}
				else {
					String s = rp[i-(2*rp.length)].item;
					
					testSchemes[i] = s.toUpperCase();
				}
			}
			
			for(int b = 0; b < testSchemes.length; b++) {
				for(int c = 0; c < p3.length; c++) {
					for(int d = 0; d < p4.length; d++) {
						for(int e = 0; e < p5.length; e++) {
							for(int f = 0; f < p6.length; f++) {
								Boolean expected = true;
								Boolean schemeVal;
								
								if(b < rp.length) schemeVal = rp[b].valid;
								else if(b < rp.length*2) schemeVal = rp[b - rp.length].valid;
								else schemeVal = rp[b - (2 * rp.length)].valid;
								
								String url = testSchemes[b] + p3[c].item + p4[d].item + p5[e].item + p6[f].item;
								
								if(!schemeVal) expected = false;
								else if(!p3[c].valid) expected = false;
								else if(!p4[d].valid) expected = false;
								else if(!p5[e].valid) expected = false;
								else if(!p6[f].valid) expected = false;
								
								try {
									if(expected) Assert.assertTrue(v.isValid(url));
									else Assert.assertFalse(v.isValid(url));
								}
								catch(Throwable T) {
									collector.addError(T);
									errors.push(url);
								}
							}
						}
					}
				}
			}
			
			if(printFailureInfo && !errors.empty()) {
				System.out.print("Valid schemes passed to UrlValidator(), in this order: ");
				for(int i = 0; i < numValSchemes; i++) {
					System.out.print(valSchemes[i] + "   ");
				}
				System.out.println("\nUrlValidator.isValid() failed with the following URLs:");
				while(!errors.empty()) System.out.println(errors.pop());
				System.out.println("\n\n");
			}
		}
	}
	
	ResultPair[] p1 = {
			new ResultPair("httpd", false),
			new ResultPair("https", true),
			new ResultPair("ftp", true),
			new ResultPair("not_valid", false),
			new ResultPair("telnet", false),
			new ResultPair("http", true)
		},
	p2 = {
			new ResultPair("http", true),
			new ResultPair("https", true),
			new ResultPair("httpd", false),
			new ResultPair("not_valid", false),
			new ResultPair("telnet", false),
			new ResultPair("ftp", true)
		},
	p3 = {
			new ResultPair("://", true),
			new ResultPair(":/", false),
	},
	p4 = {
			new ResultPair("www.", true),
			new ResultPair(".", false),
	},
	p5 = {
			new ResultPair("google", true),
			new ResultPair("zzz", true),
			new ResultPair("123", true),
			new ResultPair("", false)
	},
	p6 = {
			new ResultPair(".com", true),
			new ResultPair(".net", true),
			new ResultPair(".c", false)
	};
}

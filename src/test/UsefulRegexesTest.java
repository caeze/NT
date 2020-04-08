package test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import util.UsefulRegexes;

/**
 * Test class for {@link util.UsefulRegexes}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class UsefulRegexesTest implements Testable {

	private List<Pattern> PATTERNS_TO_TEST = Arrays.asList(UsefulRegexes.SIGNED_INTEGER, UsefulRegexes.ALPHANUMERICAL, UsefulRegexes.NUMBERS_ONLY, UsefulRegexes.LETTERS_ONLY, UsefulRegexes.FLOATING_POINT_NUMBER, UsefulRegexes.GERMAN_PHONE_NUMBER);

	public boolean testMatching() {
		// prepare data
		List<String> signedIntegers = Arrays.asList("-12333", "-0", "0", "-2", "3737834234");
		List<String> alphanumerical = Arrays.asList("a12AAZ333", "0000ZZ000aaöüÄmß", "aA776Ahfnakssmshterf0", "2", "3737834234");
		List<String> numbersOnly = Arrays.asList("12333", "0", "20", "1234567890", "3737834234");
		List<String> lettersOnly = Arrays.asList("asdfghjklöäüpoiuztrewyxcvbnm", "ASDFGHJKLÖIUZTREWQYXßÄÜÖöüäöCVBMNBVC");
		List<String> floatingPointNumbers = Arrays.asList("-1", "4.567", "-2.345", ".05");
		List<String> germanPhoneNumbers = Arrays.asList("(06442) 3933023", "(02852) 5996-0", "(042) 1818 87 9919", "06442 / 3893023", "06442 / 38 93 02 3", "06442/3839023", "042/ 88 17 890 0", "+49 221 549144 – 79", "+49 221 - 542194 79", "+49 (221) - 542944 79", "0 52 22 - 9 50 93 10", "+49(0)121-79536 - 77",
				"+49(0)2221-39938-113", "+49 (0) 1739 906-44", "+49 (173) 1799 806-44", "0173173990644", "0214154914479", "02141 54 91 44 79", "01517953677", "+491517953677", "015777953677", "02162 - 54 91 44 79", "(02162) 54 91 44 79");

		List<List<String>> stringsToTest = Arrays.asList(signedIntegers, alphanumerical, numbersOnly, lettersOnly, floatingPointNumbers, germanPhoneNumbers);

		// execute tests
		boolean result = true;
		for (int i = 0; i < PATTERNS_TO_TEST.size(); i++) {
			Pattern p = PATTERNS_TO_TEST.get(i);
			List<String> stringsCurrentlyToTest = stringsToTest.get(i);
			for (String s : stringsCurrentlyToTest) {
				result &= p.matcher(s).matches();
			}
		}

		// return result
		return result;
	}

	public boolean testNotMatching() {
		// prepare data
		List<String> signedIntegers = Arrays.asList("", "-123-33", "--0", "0-", "a2", "37.37834234");
		List<String> alphanumerical = Arrays.asList("", "a12-AAZ333", "0000:ZZ000aaöüÄmß", "aA776A+hfnakssmshterf0", "2#", "373783'4234");
		List<String> numbersOnly = Arrays.asList("", "1233a3", "0.1", "20,0", "1234ä567890", "37378%34234");
		List<String> lettersOnly = Arrays.asList("", "asdfghjk0löäüpoiuztrewyxcvbnm", "ASDFGHJ.KLÖIUZTREWQYXßÄÜÖöüäöCVBMNBVC", "a'a");
		List<String> floatingPointNumbers = Arrays.asList("", "-", "4.5a67", "-2.3.45", ",.05", "1.", "1,000.0");
		List<String> germanPhoneNumbers = Arrays.asList("", "06442,3933023", "(028a52) -5996-0", "042) 18a18 87 9919", "06442 /- 389a3023", "064ü42 / 38 93 02 3", "0644a2/3839a023", "-042/ 88 17 89:0 0", ".49 221 549144 – 79", "-49 221 - 542194* 79", "+49 ((221) ~- 542944 79", "-0 52 22 - 9 50 93 1a0",
				"+49(0)121-795a36 - 77", "+49(0)2221-39938-1a13", "+4a9 (0) 1739 906-44", "+49 (173) 1.799       806-44", "0173173990ä644", "02141#54914479", "0.2141 54 91 44 79", "015179a53677", "+49ö1517953677", "015777953677ß", "021as62 - 54 91 44 79", "(02162)) ~54 91 44 79");

		List<List<String>> stringsToTest = Arrays.asList(signedIntegers, alphanumerical, numbersOnly, lettersOnly, floatingPointNumbers, germanPhoneNumbers);

		// execute tests
		boolean result = true;
		for (int i = 0; i < PATTERNS_TO_TEST.size(); i++) {
			Pattern p = PATTERNS_TO_TEST.get(i);
			List<String> stringsCurrentlyToTest = stringsToTest.get(i);
			for (String s : stringsCurrentlyToTest) {
				result &= !p.matcher(s).matches();
			}
		}

		// return result
		return result;
	}
}

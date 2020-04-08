package util;

import java.util.regex.Pattern;

/**
 * Class for collecting useful regular expressions.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class UsefulRegexes {
	private static final String POSSIBLE_CHARACTERS = "A-Za-zÁÀȦÂÄǞǍĂĀÃÅǺǼǢĆĊĈČĎḌḐḒÉÈĖÊËĚĔĒẼE̊ẸǴĠĜǦĞG̃ĢĤḤáàȧâäǟǎăāãåǻǽǣćċĉčďḍḑḓéèėêëěĕēẽe̊ẹǵġĝǧğg̃ģĥḥÍÌİÎÏǏĬĪĨỊĴĶǨĹĻĽĿḼM̂M̄ʼNŃN̂ṄN̈ŇN̄ÑŅṊÓÒȮȰÔÖȪǑŎŌÕȬŐỌǾƠíìiîïǐĭīĩịĵķǩĺļľŀḽm̂m̄ŉńn̂ṅn̈ňn̄ñņṋóòôȯȱöȫǒŏōõȭőọǿơP̄ŔŘŖŚŜṠŠȘṢŤȚṬṰÚÙÛÜǓŬŪŨŰŮỤẂẀŴẄÝỲŶŸȲỸŹŻŽẒǮp̄ŕřŗśŝṡšşṣťțṭṱúùûüǔŭūũűůụẃẁŵẅýỳŷÿȳỹźżžẓǯßœŒçÇ";

	public static final Pattern SIGNED_INTEGER = Pattern.compile("-?\\d+");
	public static final Pattern ALPHANUMERICAL = Pattern.compile("^[" + POSSIBLE_CHARACTERS + "0-9]+$");
	public static final Pattern NUMBERS_ONLY = Pattern.compile("^[0-9]+$");
	public static final Pattern LETTERS_ONLY = Pattern.compile("^[" + POSSIBLE_CHARACTERS + "]+$");
	public static final Pattern FLOATING_POINT_NUMBER = Pattern.compile("^[+-]?([0-9]*[.])?[0-9]+$");
	public static final Pattern GERMAN_PHONE_NUMBER = Pattern.compile("(\\(?([\\d \\-\\)\\–\\+\\/\\(]+){6,}\\)?([ .\\-–\\/]?)([\\d]+))");
}

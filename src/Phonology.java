import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Phonology {
    static final ArrayList<String> vowels = new ArrayList<>(Arrays.asList("a", "e", "i", "o", "u", "ä", "ə"));
    static final ArrayList<String> consonants = new ArrayList<>(Arrays.asList("m", "n", "p", "t", "k", "kʷ", "ʾ", "b", "d", "g", "gʷ", "ṗ", "ṭ", "ḳ", "ḳʷ", "f", "s", "ś", "x", "ḥ", "h", "z", "ʿ", "ṣ", "ḍ", "w", "l", "r", "y"));
    static final ArrayList<String> gutturals = new ArrayList<>(Arrays.asList("h", "ḥ", "x", "ʾ", "ʿ"));

    static final ArrayList<String> velars = new ArrayList<>(Arrays.asList("k", "g", "ḳ"));

    public static boolean isSpace(String character) {
        return character.equals(" ");
    }

    public static boolean isVowel(String character) {
        return vowels.contains(character.toLowerCase());
    }

    public static boolean isLowVowel(String character) {
        return Objects.equals(character, "a") || Objects.equals(character, "ä");
    }

    public static boolean isConsonant(String character) {
        return consonants.contains(character.toLowerCase());
    }

    public static boolean isGuttural(String character) {
        return gutturals.contains(character.toLowerCase());
    }

    public static boolean isVelar(String character) {
        return velars.contains(character.toLowerCase());
    }

    public static boolean isLabiovelar(String sequence) {
        return sequence.equalsIgnoreCase("kʷ") || sequence.equalsIgnoreCase("gʷ") || sequence.equalsIgnoreCase("ḳʷ");
    }

    public static boolean isGeminateLabiovelarSequence(String sequence) {
        return sequence.equals("kkʷ") || sequence.equals("ggʷ") || sequence.equals("ḳḳʷ");
    }

    public static boolean isVowelInitial(String seq) {
        return isVowel(String.valueOf(seq.charAt(0)));
    }

    // Set every /ä/ followed by a syllable-closing guttural to /a/ unless the /ä/ is preceded by a word-initial ʾ
    public static String historicLengthening(String seq) {
        if(seq.length() < 2) return seq;
        StringBuilder mod = new StringBuilder(seq);

        // Deal with ʾäH sequences first (do nothing)
        if(mod.length() == 3 && mod.charAt(0) == 'ʾ') return mod.toString();

        // Word-internal guttural condition
        for (int i = 0; i < mod.length() - 2; i++) {
            // No historic lengthening of an ä which is preceded by a word-initial ʾ
            if (i == 1 && mod.charAt(0) == 'ʾ') {
                continue;
            }
            if (mod.charAt(i) == 'ä' && Phonology.isGuttural(String.valueOf(mod.charAt(i + 1))) && !Phonology.isVowel(String.valueOf(mod.charAt(i + 2)))) {
                mod.setCharAt(i, 'a');
            }
        }

        // Word-final guttural condition
        if (mod.charAt(mod.length() - 2) == 'ä' && Phonology.isGuttural(String.valueOf(mod.charAt(mod.length() - 1)))) {
            mod.setCharAt(mod.length() - 2, 'a');
        }
        return mod.toString();
    }

    public static String highHarmony(String seq) {
        StringBuilder mod = new StringBuilder(seq);
        for (int i = 0; i < mod.length() - 2; i++) {
            if (mod.charAt(i) == 'ä' && Phonology.isGuttural(String.valueOf(mod.charAt(i + 1))) && !Phonology.isLowVowel(String.valueOf(mod.charAt(i + 2)))) {
                mod.setCharAt(i, 'ə');
            }
        }
        return mod.toString();
    }

    public static String lowHarmony(String seq) {
        StringBuilder mod = new StringBuilder(seq);
        for (int i = 0; i < mod.length() - 2; i++) {
            if (mod.charAt(i) == 'ə' && Phonology.isGuttural(String.valueOf(mod.charAt(i + 1))) && Phonology.isLowVowel(String.valueOf(mod.charAt(i + 2)))) {
                mod.setCharAt(i, 'ä');
            }
        }
        return mod.toString();
    }

    public static String velarAssimilation(String seq) {
        StringBuilder mod = new StringBuilder(seq);
        for (int i = 0; i < mod.length() - 1; i++) {
            if (isVelar(String.valueOf(mod.charAt(i))) && isVelar(String.valueOf(mod.charAt(i + 1)))) {
                mod.setCharAt(i + 1, mod.charAt(i));
            }
        }
        return mod.toString();
    }
}
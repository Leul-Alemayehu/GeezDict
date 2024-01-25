import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GeezVerbConj {
    public static void main(String[] args) {
        String test = "ḳäḥ";
        String test2 = "mähirä";
        String test3 = "ʾəxäz";
        String test4 = "wadaqku";
        System.out.println(test4.charAt(4));
        System.out.println(Phonology.historicLengthening(test));
        System.out.println(Phonology.highHarmony(test2));
        System.out.println(Phonology.lowHarmony(test3));
        System.out.println(Phonology.velarAssimilation(test4));
    }
}

class Affixes {
    private static ArrayList<String> perfectiveSuffixes = new ArrayList<>(Arrays.asList("ä", "ät", "u", "a", "kä", "ki", "kəmu", "kən", "ku", "nä"));

    public static ArrayList<String> getPerfectiveSuffixes() {
        return perfectiveSuffixes;
    }
}

class NagaraVerb {
    private String root;
    private final String stem;
    private ArrayList<String> conjugatedForms;

    public NagaraVerb(String root) {
        this.root = root;
        StringBuilder stemMaker = new StringBuilder(root);
        stemMaker.insert(1, "ä");
        stemMaker.insert(3, "ä");
        this.stem = stemMaker.toString();
        conjugatedForms = new ArrayList<>();
        for (String i : Affixes.getPerfectiveSuffixes()) {
            conjugatedForms.add(stem + i);
        }
    }

    public ArrayList<String> getConjugatedForms() {
        return conjugatedForms;
    }
}

class GabraVerb {
    private String root;
    private final String stem;
    private final String shortStem;
    private final ArrayList<String> conjugatedForms;

    public GabraVerb(String root) {
        this.root = root;
        StringBuilder stemMaker = new StringBuilder(root);
        stemMaker.insert(1, "ä");
        shortStem = stemMaker.toString();
        stemMaker.insert(3, "ä");
        stem = stemMaker.toString();
        conjugatedForms = new ArrayList<>();
        for (String i : Affixes.getPerfectiveSuffixes()) {
            if (Phonology.isVowelInitial(i)) {
                conjugatedForms.add(shortStem + i);
            } else {
                conjugatedForms.add(stem + i);
            }
        }
    }

    public ArrayList<String> getConjugatedForms() {
        return conjugatedForms;
    }

}

class Phonology {
    static final ArrayList<String> vowels = new ArrayList<>(Arrays.asList("a", "e", "i", "o", "u", "ä", "ə"));
    static final ArrayList<String> gutturals = new ArrayList<>(Arrays.asList("h", "ḥ", "x", "ʾ", "ʿ"));

    static final ArrayList<String> velars = new ArrayList<>(Arrays.asList("k", "g", "q"));

    public static boolean isVowel(String character) {
        return vowels.contains(character);
    }

    public static boolean isLowVowel(String character) {
        return Objects.equals(character, "a") || Objects.equals(character, "ä");
    }

    public static boolean isConsonant(String character) {
        return !isVowel(character) && Objects.nonNull(character);
    }

    public static boolean isGuttural(String character) {
        return gutturals.contains(character);
    }

    public static boolean isVelar(String character) {
        return velars.contains(character);
    }

    public static boolean isVowelInitial(String seq) {
        return isVowel(String.valueOf(seq.charAt(0)));
    }

    public static String historicLengthening(String seq) {
        StringBuilder mod = new StringBuilder(seq);
        for (int i = 0; i < mod.length() - 2; i++) {
            if (mod.charAt(i) == 'ä' && Phonology.isGuttural(String.valueOf(mod.charAt(i + 1))) && !Phonology.isVowel(String.valueOf(mod.charAt(i + 2)))) {
                mod.setCharAt(i, 'a');
            }
        }
        if (mod.charAt(mod.length() - 2) == 'ä' && Phonology.isGuttural(String.valueOf(mod.charAt(mod.length() - 1))))
            mod.setCharAt(mod.length() - 2, 'a');
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

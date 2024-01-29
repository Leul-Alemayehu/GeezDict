import java.util.ArrayList;
import java.util.Arrays;

public class GeezVerbConj {
    public static void main(String[] args) {
        String test = "ʾählähläh";
        String test2 = "mähirä";
        String test3 = "ʾəxäz";
        String test4 = "läḥäkʷku";
        System.out.println(test4.charAt(4));
        System.out.println(Phonology.historicLengthening(test));
        System.out.println(Phonology.highHarmony(test2));
        System.out.println(Phonology.lowHarmony(test3));
        System.out.println(Phonology.velarAssimilation(test4));
        GabraVerb shut = new GabraVerb("zgḥ");
        for(String form : shut.getConjugatedForms()){
            System.out.println(Phonology.historicLengthening(form));
        }
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
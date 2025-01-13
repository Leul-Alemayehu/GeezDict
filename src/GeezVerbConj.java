import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GeezVerbConj {
    public static void main(String[] args) throws IOException {
        GabraVerb shut = new GabraVerb("zgḥ");
        NagaraVerb tell = new NagaraVerb("ngr");
        for(String form : shut.getConjugatedForms()){
            System.out.println(Orthography.stringify(Orthography.toGeezScript(Orthography.simplify(Orthography.tokenize(Phonology.historicLengthening(form))))));
        }
        for(String form : tell.getConjugatedForms()){
            System.out.println(Orthography.stringify(Orthography.toGeezScript(Orthography.simplify(Orthography.tokenize(Phonology.historicLengthening(form))))));
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
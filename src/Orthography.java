import java.util.ArrayDeque;
import java.util.Deque;
public class Orthography {
    public static void main(String[] args) {
        Deque<String> showDeque = simplify(tokenize("snaraggʷalla"));
        while(!showDeque.isEmpty()){
            System.out.println(showDeque.remove());
        }
    }

    public static Deque<String> tokenize(String latinAlphabetString) {
        Deque<String> tokenized = new ArrayDeque<>();
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = latinAlphabetString.length() - 1; i >= 0; i--) {

            // Check for spaces first; hopefully not necessary often because normally spaced tokenized input is fed
            if (Phonology.isSpace(String.valueOf(latinAlphabetString.charAt(i)))) {
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
            }
            // Check for sequences like "kkʷ" and "ggʷ" (geminate labiovelar)
            else if (i > 1 && Phonology.isGeminateLabiovelarSequence(latinAlphabetString.substring(i - 2, i + 1))) {
                tokenBuilder.insert(0, latinAlphabetString.substring(i - 2, i + 1));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
                i -= 2; // skip the second character in the sequence
            }
            // Check for sequences like "kʷ" and "gʷ" (non-geminate labiovelar)
            else if (i > 0 && Phonology.isLabiovelar(latinAlphabetString.substring(i - 1, i + 1))) {
                tokenBuilder.insert(0, latinAlphabetString.substring(i - 1, i + 1));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
                i--; // skip the second character in the sequence
            }
            // Check for other vowels
            else if (Phonology.isVowel(String.valueOf(latinAlphabetString.charAt(i)))) {
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
            }
            // Initial consonant condition
            else if (Phonology.isConsonant(String.valueOf(latinAlphabetString.charAt(i))) && i == 0) {
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
            }
            // Nongeminate consonant cluster condition
            else if (Phonology.isConsonant(String.valueOf(latinAlphabetString.charAt(i))) &&
                    latinAlphabetString.charAt(i) != latinAlphabetString.charAt(i - 1)) {
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
            }

            // other geminates
            else if (Phonology.isConsonant(String.valueOf(latinAlphabetString.charAt(i))) && latinAlphabetString.charAt(i) == latinAlphabetString.charAt(i - 1)) {
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
                i--; // skip geminate consonant
            }
        }
        return tokenized;
    }

    // Turn transcription into transliteration with 1-to-1 correspondence with Geʿez script
    public static Deque<String> simplify(Deque<String> tokenized) {
        Deque<String> simplified = new ArrayDeque<>();
        StringBuilder simpleBuilder = new StringBuilder();
        while (!tokenized.isEmpty()) {
            simpleBuilder = new StringBuilder(tokenized.remove());
            if(simpleBuilder.length() > 1 && simpleBuilder.charAt(0) == simpleBuilder.charAt(1)){
                simpleBuilder.deleteCharAt(0);
                simplified.add(simpleBuilder.toString());
            }
            else{
                simplified.add(simpleBuilder.toString());
            }
        }
        return simplified;
    }
}

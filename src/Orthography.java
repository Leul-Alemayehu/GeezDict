import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Orthography {
    public static void main(String[] args) {
        Deque<String> showDeque = toGeezScript(simplify(tokenize("namällək")));
        System.out.println(stringify(showDeque));
    }

    public static String stringify(Deque<String> s) {
        StringBuilder sb = new StringBuilder();
        for(String str : s) {
            sb.append(str);
        }
        return sb.toString();
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

            // Other geminates
            else if (Phonology.isConsonant(String.valueOf(latinAlphabetString.charAt(i))) && latinAlphabetString.charAt(i) == latinAlphabetString.charAt(i - 1)) {
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenBuilder.insert(0, latinAlphabetString.charAt(i));
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
                i--; // skip geminate consonant
            }

            // Deal with the exceptional case of word-initial vowels, which aren't even supposed to exist
            if (i == 0 && Phonology.isVowel(String.valueOf(latinAlphabetString.charAt(i)))) {
                tokenBuilder.insert(0, "ʾ");
                tokenized.addFirst(tokenBuilder.toString());
                tokenBuilder.setLength(0);
            }
        }
        return tokenized;
    }

    // Turn transcription into transliteration with 1-to-1 correspondence with Geʿez script
    public static Deque<String> simplify(Deque<String> tokenized) {
        Deque<String> simplified = new ArrayDeque<>();
        StringBuilder simpleBuilder;
        while (!tokenized.isEmpty()) {
            simpleBuilder = new StringBuilder(tokenized.remove());
            if (simpleBuilder.length() > 1 && simpleBuilder.charAt(0) == simpleBuilder.charAt(1)) {
                simpleBuilder.deleteCharAt(0);
                simplified.add(simpleBuilder.toString().toLowerCase());
            } else {
                // No distinction between syllables with sixth-order vowels and simple consonants
                if (!Phonology.isVowel(String.valueOf(simpleBuilder.charAt(simpleBuilder.length() - 1))))
                    simpleBuilder.append("ə");
                simplified.add(simpleBuilder.toString().toLowerCase());
            }
        }
        return simplified;
    }

    // Turn token stream to Geʿez script token stream
    public static Deque<String> toGeezScript(Deque<String> simplified) {
        String filePath = "./workfiles/Latin to Geez map - CSV_Friendly(1).csv";
        Deque<String> geezTokens = new ArrayDeque<>();
        HashMap<String, String> transliterationsToGeez = new HashMap<>();

        // Read and parse the CSV
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                // Split the line into Transliteration and Grapheme
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    transliterationsToGeez.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String transliteration : simplified){
            String geezToken = transliterationsToGeez.get(transliteration);
            if (geezToken != null) {
                geezTokens.addLast(geezToken);
            } else {
                System.err.println("Unmapped token: " + transliteration);
            }
        }
        return geezTokens;
    }
}
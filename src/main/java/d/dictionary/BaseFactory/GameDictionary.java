package d.dictionary.BaseFactory;

import java.util.ArrayList;
import java.util.Random;

public class GameDictionary extends DictionaryManagement {
    protected String word;
    protected String suggestions;

    public String getRandomWord() {
        Word wordRandom = getRandomWordinDatabase(DictionaryManagement.getWordList());
        if (wordRandom != null) {
            suggestions = wordRandom.getWord_explain();
            setSuggestions(suggestions);
            word = wordRandom.getWord_target();
            return word;
        }

        return null;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        String result = "";
        String detail = suggestions;
        detail = detail.replaceAll("<I><Q>", "");
        detail = detail.replaceAll("</Q></I>", "");
        String[] parts = detail.split("<br />");
        for (String part : parts) {
            if (part.contains("*")) {
                result += part.substring(1).trim();
                result += "\n";
                break;
            }
        }
        for (String part : parts) {
            if (part.contains("-")) {
                result += part.substring(1).trim();
                break;
            }
        }
        this.suggestions = result;
    }

    public static Word getRandomWordinDatabase(ArrayList<Word> wordList) {
        if (wordList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(wordList.size());
        return wordList.get(randomIndex);
    }
}

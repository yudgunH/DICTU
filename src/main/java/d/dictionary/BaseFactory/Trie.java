package d.dictionary.BaseFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trie {

    public static class TrieNode {
        Map<Character, TrieNode> children = new TreeMap<>();
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
        }
    }

    private static final TrieNode root = new TrieNode();
    protected static ArrayList<String> searchedWords = new ArrayList<>();

    public static ArrayList<String> getSearchedWords() {
        return searchedWords;
    }

    public static void insert(String target) {
        if (target == null || target.isEmpty()) {
            return; // Skip inserting null or empty strings
        }

        TrieNode parent = root;
        int n = target.length();

        for (int i = 0; i < n; i++) {
            char current = target.charAt(i);
            parent.children.putIfAbsent(current, new TrieNode());
            parent = parent.children.get(current);
        }

        parent.isEndOfWord = true;
    }

    private static void dfsGetWords(TrieNode p, String current) {
        if (p.isEndOfWord) {
            searchedWords.add(current);
        }

        for (char c : p.children.keySet()) {
            dfsGetWords(p.children.get(c), current + c);
        }
    }

    public static ArrayList<String> search(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return new ArrayList<>();
        }

        searchedWords.clear();
        TrieNode p = root;

        int n = prefix.length();
        for (int i = 0; i < n; ++i) {
            char current = prefix.charAt(i);
            p.children.putIfAbsent(current, new TrieNode());
            p = p.children.get(current);
        }

        dfsGetWords(p, prefix);
        return new ArrayList<>(searchedWords);
    }

    public static void delete(String target) {
        TrieNode p = root;

        if (target == null || target.isEmpty()) {
            return; // Skip deleting null or empty strings
        }

        int n = target.length();
        for (int i = 0; i < n; ++i) {
            char current = target.charAt(i);
            if (p.children.get(current) == null) {
                System.out.println("Không tìm thấy từ");
                return;
            }
            p = p.children.get(current);
        }

        if (!p.isEndOfWord) {
            System.out.println("Không tìm thấy từ");
            return;
        }
        p.isEndOfWord = false;
    }
}

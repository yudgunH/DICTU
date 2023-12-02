package d.dictionary.BaseFactory;

import d.dictionary.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static d.dictionary.App.scene;
import static d.dictionary.App.stage;

public class Dictionary {
    protected static Map<String, String> words;

    public Dictionary() {
        words = new HashMap<>();
    }

    public void addWord(Word word) {
        words.put(word.getWord_target(), word.getWord_explain());
        Trie.insert(word.getWord_target());
    }

    public void removeWord(Word word) {
        words.remove(word.getWord_target());
    }

    public String lookupWord(Word word) {
        String definition = words.get(word.getWord_target());
        if (definition != null) {
            return definition;
        } else {
            return "Không tìm thấy từ trong từ điển.";
        }
    }

    protected static Dictionary dictionary = new Dictionary();

    protected static ArrayList<Word> wordList;

    protected static ArrayList<Word> wordsGame;


    public static ArrayList<Word> getWordList() {
        return wordList;
    }

    public static ArrayList<Word> getWordsGame() {
        return wordsGame;
    }

    public static void switchScene(ActionEvent event, Parent root) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    public static void showWindow(String filename, String title) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource(filename)));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}

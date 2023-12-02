package d.dictionary.Controller;

import d.dictionary.BaseFactory.GameDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class HangManController extends GameController {
    @FXML
    private Pane base1;

    @FXML
    private Pane base2;

    @FXML
    private Pane base3;

    @FXML
    private FlowPane buttons;

    @FXML
    private Pane man;

    @FXML
    private Pane pole;

    @FXML
    private Text realWord;

    @FXML
    private Pane rod;

    @FXML
    private Pane rope1;

    @FXML
    private Pane rope2;

    @FXML
    private Text text;

    @FXML
    private Text suggestion;

    @FXML
    private Button btnSuggesttion;

    @FXML
    private Text winStatus;

    @FXML
    void newGame() {
        for (int i = 0; i < 26; i++) {
            buttons.getChildren().get(i).setDisable(false);
        }
        suggestion.setText("");
        initialize();
    }

    @FXML
    public void onClick(ActionEvent event) {
        String letter = ((Button) event.getSource()).getText().toLowerCase();
        ((Button) event.getSource()).setDisable(true);

        if (myWord.contains(letter)) {
            handleCorrectGuess(letter);
        } else {
            handleWrongGuess();
        }
    }
    private final GameDictionary word = new GameDictionary();
    private StringBuilder displayWord;
    private StringBuilder answer;

    @Override
    public void initialize() {
        base1.setVisible(false);
        base2.setVisible(false);
        base3.setVisible(false);
        pole.setVisible(false);
        rod.setVisible(false);
        rope1.setVisible(false);
        rope2.setVisible(false);
        man.setVisible(false);
        score = 0;
        tries = 0;
        myWord = word.getRandomWord();
        hints = word.getSuggestions();
        btnSuggesttion.setOnAction(event -> {
            suggestion.setText(hints);
        });
        displayWord = new StringBuilder();
        answer = new StringBuilder();
        for (int i = 0; i < myWord.length(); i++) {
            if (Character.isWhitespace(myWord.charAt(i))) {
                displayWord.append(" ");
            } else if (Character.isAlphabetic(myWord.charAt(i))) {
                displayWord.append("_");
            } else {
                displayWord.append("-");
            }
        }
        text.setText(displayWord.toString());
        winStatus.setText("");
        realWord.setText("");
        buttons.setDisable(false);
    }

    @Override
    public void handleCorrectGuess(String letter) {
        answer.append(letter);
        for (int i = 0; i < myWord.length(); i++) {
            if (Character.toString(myWord.charAt(i)).equals(letter)) {
                displayWord.setCharAt(i, letter.charAt(0));
                score++;
            }
        }

        text.setText(displayWord.toString());
        endGame();
    }

    @Override
    public void handleWrongGuess() {
        tries++;
        showHangman();
        endGame();
    }

    @Override
    public void endGame() {
        if (score == myWord.length()) {
            winStatus.setText("You win!");
            realWord.setText("The word was: " + myWord);
            buttons.setDisable(true);
        }

        if (tries == 8) {
            winStatus.setText("You lose!");
            realWord.setText("The word was: " + myWord);
            buttons.setDisable(true);
        }
    }

    private void showHangman() {
        switch (tries) {
            case 1:
                base1.setVisible(true);
                break;
            case 2:
                base2.setVisible(true);
                break;
            case 3:
                base3.setVisible(true);
                break;
            case 4:
                pole.setVisible(true);
                break;
            case 5:
                rod.setVisible(true);
                break;
            case 6:
                rope1.setVisible(true);
                break;
            case 7:
                rope2.setVisible(true);
                break;
            case 8:
                rope2.setVisible(false);
                man.setVisible(true);
                break;
        }
    }
}

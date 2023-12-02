package d.dictionary.Controller;

import d.dictionary.BaseFactory.GameDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


public class QuizController extends GameController {
    @FXML
    private Button hintBtn;

    @FXML
    private Text hintText;

    @FXML
    private Button newBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField wordinput;

    @FXML
    private Text wordscramble, scoreText, timerText;

    private final GameDictionary word = new GameDictionary();

    private Timer timer;
    private int time;

    @FXML
    public void onClick(ActionEvent event) {
        if (event.getSource() == newBtn) {
            initialize();
            score = 0;
        } else if (event.getSource() == submitBtn || event.getSource() == wordinput) {
            String input = wordinput.getText();
            if (input.equals(myWord)) {
                handleCorrectGuess(myWord);
            } else {
                handleWrongGuess();
            }
        } else if (event.getSource() == hintBtn) {
            hintText.setVisible(true);
        }
    }

    @Override
    public void handleCorrectGuess(String word) {
        score++;
        scoreText.setText("Score: " + score + "/10");
        if (score == 10) {
            System.out.println("Win");
        } else {
            initialize();
        }
    }

    @Override
    public void handleWrongGuess() {
        tries--;
        if (tries == 0) {
            hintBtn.setDisable(false);
        }
        wordinput.setText("");
    }

    @Override
    public void endGame() {
        System.out.println("End game");
    }

    public void initialize() {
        wordinput.setText("");
        myWord = word.getRandomWord();
        hints = word.getSuggestions();
        tries = 3;
        hintText.setVisible(false);
        hintBtn.setDisable(true);
        wordscramble.setText(myWord);
        hintText.setText(hints);
        scoreText.setText("Score: " + score + "/10");
        setTimer();
    }

    private void setTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        time = 30;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerText.setText("Time left:" + time + "s");
                time--;
                if (time == -1) {
                    timer.cancel();
                    timer = null;
                    endGame();
                }
            }
        }, 1000, 1000);
    }

}

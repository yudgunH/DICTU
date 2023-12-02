package d.dictionary.Controller;

public abstract class GameController {
    protected int score;
    protected int tries;
    protected String myWord;
    protected String hints;
    public abstract void initialize();
    public abstract void handleCorrectGuess(String word);
    public abstract void handleWrongGuess();

    public abstract void endGame();

}

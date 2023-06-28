import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;
import javafx.animation.FillTransition;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*
 *  @author Muhammad Danish
 *
 * A class of the Typing Game that deals with the words and associated functions.
 */
public class Words {
    // Pane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/Pane.html)
    // which represents the floating words part of the game
    private final Pane wordsPane;
    // List of all available words
    private final List<String> words;
    // List of all JavaFX floating words currently on the screen
    private final List<WordBox> activeWords;
    // List of all keys that have been pressed since the last correct word
    private final List<KeyCode> typed;
    // JavaFX Label which shows the score on the screen
    private final Label scoreLabel;
    // Keeps track of the number of correct words
    private int score = 0;
    // JavaFX Label which shows what the user has typed since the last correct word
    private final Label typedLabel;
    // Width/height of the screen
    private final double width;
    private final double height;
    private Label spawnSpeedLabel = new Label("Words Spawn / Min: ");
    private Label stayDurationLabel = new Label("Duration of Words (Sec): ");
    private Slider spawnSpeedSlider = new Slider(10, 60, 20);
    private Slider stayDurationSlider = new Slider(5, 30, 10);

    /*
     * Contructor for Words
     * @param path
     * @param width
     * @param height
     * @param scoreLabel
     * @param typedLabel
     * @throws FileNotFoundException
     */
    public Words(String path, double width, double height,
                 Label scoreLabel, Label typedLabel) throws FileNotFoundException {
        wordsPane = new Pane();
        wordsPane.setPrefWidth(width);
        wordsPane.setPrefHeight(height);

        this.words = Utils.readWords(path);

        activeWords = new ArrayList<>();
        typed = new ArrayList<>();

        this.scoreLabel = scoreLabel;
        this.typedLabel = typedLabel;

        this.width = width;
        this.height = height;

    }

    /*
     * Getter for the Pane of wordsPane
     * @return wordsPane Pane
     */
    public Pane getWordsPane() {

        return wordsPane;

    }

    /*
     * Getter for the score in game
     * @return score int
     */
    public int getScore() {

        return score;

    }

    /*
     * Getter for the spawn speed of words
     * @return spawn speed int
     */
    public int getSpawnSpeed() {

        // Value in seconds for delays between word spawns
        return 60 / (int) spawnSpeedSlider.getValue();

    }

    /*
     * Getter for the spawn speed Label
     * @return spawnSpeedLabel Label
     */
    public Label getSpawnSpeedLabel() {

        return spawnSpeedLabel;

    }

    /*
     * Getter for the spawn speed Slider
     * @return spawnSpeedSlider Slider
     */
    public Slider getSpawnSpeedSlider() {

        return spawnSpeedSlider;

    }

    /*
     * Getter for the stay duration of words (seconds)
     * @return stay duration int
     */
    public int getStayDuration() {

        return (int) stayDurationSlider.getValue();

    }

    /*
     * Getter for the stay duration of words Label
     * @return stayDurationLabel Label
     */
    public Label getStayDurationLabel() {

        return stayDurationLabel;

    }

    /*
     * Getter for the stay duration of words Slider
     * @return stayDurationSlider Slider
     */
    public Slider getStayDurationSlider() {

        return stayDurationSlider;

    }

    /*
     * Removes the wordBox from the wordsPane as well as
     * removing it from activeWords.
     * @param wordBox WordBox to remove
     * @param correctWord boolean to remove
     */
    private void removeWord(WordBox wordBox, boolean correctWord) {

        // Use a special effect for 0.25 seconds before removing a correct word
        if (correctWord) {

            Rectangle rect = wordBox.getRect();

            wordBox.shrinkWordBox();

            FillTransition wbFill = new FillTransition(Duration.millis(250), rect, Color.TRANSPARENT, Color.GREEN);
            wbFill.play();
            wbFill.setOnFinished(event -> wordsPane.getChildren().remove(wordBox.getWordBox()));

        }
        else {
        // Normal removal for timeout of words
            wordsPane.getChildren().remove(wordBox.getWordBox());

        }

        activeWords.remove(wordBox);

    }

    /*
     * Creates a random floating word.
     * Choses a random word from the list of words.
     * Then chooses a starting point on any edge of the screen.
     * Then creates a Timeline (https://openjfx.io/javadoc/18/javafx.graphics/javafx/animation/Timeline.html)
     * that moves the WordBox from its starting point to a random ending
     * point over 10 seconds.
     */
    public void createWord() {

        // Get a random word from the list of words
        String word = words.get(ThreadLocalRandom.current().nextInt(words.size()));

        // Create a WordBoX for the word
        WordBox wordBox = new WordBox(40, word, Color.TRANSPARENT);

        StackPane spWordBox = wordBox.getWordBox();

        double startX = 0;
        double startY = ThreadLocalRandom.current().nextDouble(height) * 2.0 / 5.0;

        double endX = ThreadLocalRandom.current().nextDouble(width) * 4.0 / 5.0;
        double endY = ThreadLocalRandom.current().nextDouble(height) * 2.0 / 5.0;

        // To account for shorter horizontant floating paths
        if (endX < width * 0.2) {

            endX *= 2;

        }

        wordsPane.getChildren().add(spWordBox);
        activeWords.add(wordBox);

        // Use TimeLine transiiton to make the active words flooat
        // staring from the right edge and ending at random points
        Timeline wordFloating = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(wordBox.getWordBox().translateXProperty(), startX),
                        new KeyValue(wordBox.getWordBox().translateYProperty(), startY)),
                new KeyFrame(Duration.seconds(getStayDuration()),
                        new KeyValue(wordBox.getWordBox().translateXProperty(), endX),
                        new KeyValue(wordBox.getWordBox().translateYProperty(), endY))
        );

        // Remove the word after the timeout
        wordFloating.setOnFinished(event -> removeWord(wordBox, false));

        wordFloating.play();

    }

    /*
     * Adds the keyCode to typed if it is a letter key.
     * Removes the first element of typed if it is the backspace key.
     * Either way it checks for a correct word and updates the typedLabel.
     * @param keyCode KeyCode to add to the state
     */
    public void addTypedLetter(KeyCode keyCode) {

        // If it is a letter, add to the typed label
        if (keyCode.isLetterKey()) {

            typed.add(keyCode);

        }
        // If it is a back space, remove to the last character from typed label
        else if (keyCode == KeyCode.BACK_SPACE && !(typedLabel.getText().equals(""))) {

            typed.remove(typed.size() - 1);

        }

        // Check if the word in typed the label matches any active words
        checkForCorrectWord(Utils.combineList(typed));

    }

    /*
     * Checks if the given String is equal to any of the currently
     * active words. If it is then it updates the score and scoreLabel.
     * It also removes the wordBox and clears the typed list.
     * @param s Word to check
     */
    private void checkForCorrectWord(String s) {

        boolean correctWordFound = false;

        // Check if the String s matched any word of the active WordBoxes
        for (WordBox wb : activeWords) {

            if (wb.getWord().equals(s)) {

                // Remove the word as correct, update score, and clear screen & typed label
                removeWord(wb, true);
                typed.clear();
                typedLabel.setText("");
                score++;
                scoreLabel.setText(Integer.toString(score));
                correctWordFound = true;
                break;

            }
        }

        // If correct word not found, do nothing with the typed label
        if (!correctWordFound) {

            typedLabel.setText(s);

        }
    }
}

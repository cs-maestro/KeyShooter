import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/*
 *  @author Muhammad Danish
 *
 * A class of the Typing Game that deals with the WordBox and associated functions.
 */
public class WordBox {
    // StackPane for WordBox
    private final StackPane wordBox;
    // Rectangle Shape for WordBox
    private final Rectangle rect;
    // String word inside the WordBox
    private final String word;
    // Label for the word to be placed inside the WordBox
    private Label text;
    // Double size of the font, width, and length of WordBox
    private double size;

    /*
     * Constructor for WordBox
     * @param size as double
     * @param word as String
     * @param color as Color
     */
    public WordBox(double size, String word, Color color) {
        wordBox = new StackPane();
        this.size = size;
        rect = new Rectangle(size, size, color);
        this.word = word.toUpperCase();
        text = new Label(this.word);
        text.setFont(new Font(size - 2));

        wordBox.getChildren().addAll(rect, text);
    }

    /*
     * Getter for the StackPane of WordBox
     * @return wordBox Stackpane
     */
    public StackPane getWordBox() {
        return wordBox;
    }

    /*
     * Getter for the Rectangle of WordBox
     * @return rect Rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /*
     * Getter for the word of WordBox
     * @return word String
     */
    public String getWord() {
        return word;
    }

    /*
     * This function does the following:
     * - Reduces the WordBox font size by half
     * - Reduces the WordBox width by half
     */
    public void shrinkWordBox() {

        text.setFont(new Font(size * 0.5));
        rect.setWidth(size * (word.length()) * 0.5);

    }
}

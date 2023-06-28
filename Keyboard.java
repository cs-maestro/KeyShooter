import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.*;

/*
 *  @author Muhammad Danish
 *
 * A class of the Typing Game that initializes and creates a KeyBoard as VBox.
 */
public class Keyboard {
    // 2 Dimensional list representing the rows of keys on the keyboard
    // Letter keys only
    private final List<List<KeyCode>> keyCodes;
    // Map that is used to access the keys JavaFX representation
    private final Map<KeyCode, WordBox> keyCodeToWordBox;
    // JavaFX control that represents the keyboard on the screen
    private final VBox keyboard;
    // Color that the keys are by default
    private static final Color from = Color.color(0.9, 0.9, 0.9);
    // Color that the keys become when pressed
    private static final Color to = Color.color(0.3, 0.3, 0.8);

    /*
     * Constructor for the keyboard
     * @param width as double
     * @param height as double
     * @param spacing as double
     */
    public Keyboard(double width, double height, double spacing) {
        keyCodes = initializeKeys();
        keyCodeToWordBox = new HashMap<>();

        keyboard = initializeKeyboard(width, height, keyCodes, spacing);
    }

    /*
     * Getter for the VBox
     * @return Keyboard as VBox
     */
    public VBox getKeyboard() {
        return keyboard;
    }

    /*
     * First checks if the given keyCode exists in the keyCodeToWordBox.
     * If it does then it starts a FillTransition
     * to go from the from color to the to color.
     * If the keyCode does not exist then it does nothing.
     * @param keyCode KeyCode to lookup in the map and flash
     */
    public void startFillTransition(KeyCode keyCode) {

        // Check if valid KeyCode
        if (keyCodeToWordBox.containsKey(keyCode)) {

            Rectangle rect = keyCodeToWordBox.get(keyCode).getRect();

            // Fading tansition to blue color in one second
            FillTransition keyboardFill = new FillTransition(Duration.millis(1000), rect, from, to);
            keyboardFill.play();

            // Fading tansition to normal color in one second
            keyboardFill = new FillTransition(Duration.millis(1000), rect, to, from);
            keyboardFill.play();

        }

    }

    /*
     * Simply creates the 2D list that represents the keyboard.
     * Each row is an element of the outer list and each inner list
     * contains all the letter keys in that row. Only contains
     * 3 rows. All letters are uppercase.
     * @return 2D list representing the letters on the keyboard
     */
    private List<List<KeyCode>> initializeKeys() {

        // List to hold threee lists of KeyCodes representing each row of keyboard
        List<List<KeyCode>> keyCodes = new ArrayList<>();

        List<KeyCode> rowOne = new ArrayList<>();
        List<KeyCode> rowTwo = new ArrayList<>();
        List<KeyCode> rowThree = new ArrayList<>();

        Collections.addAll(rowOne,
                KeyCode.Q,
                KeyCode.W,
                KeyCode.E,
                KeyCode.R,
                KeyCode.T,
                KeyCode.Y,
                KeyCode.U,
                KeyCode.I,
                KeyCode.O,
                KeyCode.P );

        Collections.addAll(rowTwo,
                KeyCode.A,
                KeyCode.S,
                KeyCode.D,
                KeyCode.F,
                KeyCode.G,
                KeyCode.H,
                KeyCode.J,
                KeyCode.K,
                KeyCode.L );

        Collections.addAll(rowThree,
                KeyCode.Z,
                KeyCode.X,
                KeyCode.C,
                KeyCode.V,
                KeyCode.B,
                KeyCode.N,
                KeyCode.M );

        // Add the three rows of KeyCodes to one List and return it
        keyCodes.add(rowOne);
        keyCodes.add(rowTwo);
        keyCodes.add(rowThree);

        return keyCodes;
    }

    /*
     * Creates the JavaFX control that visualized the keyboard on the screen
     * Also initializes the keyCodeToWordBox map as it goes.
     * It deduces the size of each key using the 2D list and the
     * width parameter. Then creates a VBox and sets its width/height
     * and centers it. Then loops over the 2D list and creates JavaFX
     * controls, WordBox, to represent each key and adds them to HBoxes.
     * The adds the row HBox to the VBox. It also adds the WordBox to the
     * map. Then it moves on to the next row.
     * @param width Width of the screen
     * @param height Height of the screen
     * @param keyCodes 2D list that holds all the letters on the keyboard
     * @param spacing Space between each key
     * @return JavaFX control that visualizes the keyboard on the screen
     */
    private VBox initializeKeyboard(double width, double height, List<List<KeyCode>> keyCodes, double spacing) {

        // Create a VBox to hold KeyBoard keys
        VBox vBox = new VBox();
        vBox.setMinHeight(height);
        vBox.setMinWidth(width);
        vBox.setAlignment(Pos.CENTER);

        double size = width / keyCodes.get(0).size();

        // Add the three lists of KeyCodes into three HBoxes
        // Add each HBox to the VBox as a row of the keyboard
        for (int i = 0; i < keyCodes.size(); i++) {

            HBox hBox = new HBox(spacing);

            for (KeyCode keyCode : keyCodes.get(i)) {

                WordBox wordBox = new WordBox(size - spacing, keyCode.getName(), from);

                keyCodeToWordBox.put(keyCode, wordBox);
                hBox.getChildren().add(wordBox.getWordBox());

            }

            hBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hBox);
        }

        return vBox;
    }
}

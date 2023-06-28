import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/*
 *  @author Muhammad Danish
 *
 * Msin class of the Typing Game that used other classes and add some
 * additional things to run the gane.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * Setups up all the JavaFX GUI controls and creates instances of
     * all the helper classes.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Always make sure to set the title of the window
        primaryStage.setTitle("Key Shooter");

        // Width/height variables so that we can mess with the size of the window
        double width = 700;
        double height = 700;

        // BorderPane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/BorderPane.html)
        // Provides the basis which we basis the rest of the GUI on
        BorderPane window = new BorderPane();

        // VBox for the top part of the GUI
        VBox topVBox = new VBox(5);
        topVBox.setAlignment(Pos.CENTER);

        // Label which displays the score
        Label scoreLabel = new Label("0");
        scoreLabel.setFont(new Font(30));

        // Label which displays the currently typed letters
        Label typedLabel = new Label();
        typedLabel.setFont(new Font(40));

        // Button to stop the game
        Button stopButton = new Button("STOP");
        stopButton.setTextFill(Color.RED);
        stopButton.setFont(new Font(20));

        // Add them all to the VBox
        topVBox.getChildren().addAll(scoreLabel, typedLabel, stopButton);

        // Put them in the top of the BorderPane
        window.setTop(topVBox);

        // Create an instance of our helper Words class
        Words words = new Words("./words.txt", width, (height * 3) / 4,
                                scoreLabel, typedLabel);


        // Put the pagne in the center of the BorderPane
        window.setCenter(words.getWordsPane());

        // Create a VBox for the keyboard and controls
        VBox keyBoardWindow = new VBox(10);

        // Create an instance of our helper class Keyboard
        Keyboard keyboard = new Keyboard(width, height / 4, 10);

        // Create an HBox to hold game controls
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(
                words.getSpawnSpeedLabel(), words.getSpawnSpeedSlider(),
                words.getStayDurationLabel(), words.getStayDurationSlider()
        );
        hBox.setAlignment(Pos.CENTER);

        // Add a horizontal line above the keyboard to create clear seperation
        keyBoardWindow.getChildren().addAll(new Separator(Orientation.HORIZONTAL), hBox, keyboard.getKeyboard());

        // Put it in the bottom of the BorderPane
        window.setBottom(keyBoardWindow);

        // Create the scene
        Scene scene = new Scene(window, width, height);

        // Get the KeyCode of the event
        // Start the fill transition, which blinks the key
        // Then add it to the typed letters
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            keyboard.startFillTransition(keyCode);
            words.addTypedLetter(keyCode);
        });

        // Set the scene
        primaryStage.setScene(scene);

        // Run the game
        primaryStage.show();

        // AnimationTimer to create words on th screen every 3 seconds using createWord from the Words class.
        AnimationTimer timer = new AnimationTimer() {

            private long lastToggle;

            @Override
            public void handle(long now) {

                if (lastToggle == 0L) {

                    lastToggle = now;

                }
                else {

                    long diff = now - lastToggle;

                    // Assign spawn time for words based on input from the controls
                    if (diff >= words.getSpawnSpeed() * 1_000_000_000L) { // 1,000,000,000ns == 1s
                        words.createWord();
                        lastToggle = now;

                    }
                }
            }
        };
        timer.start();

        double timeStart = System.nanoTime();

        // Stop the game and show WPM when STOP button clicked
        stopButton.setOnAction(new EventHandler<ActionEvent>() {

            // Method is automatically called when  button is pressed)
            @Override
            public void handle(ActionEvent event) {

                timer.stop();

                double timeEnd = System.nanoTime();
                double WPM = words.getScore() / ((timeEnd - (timeStart + 3e9)) / 6e10);

                words.getWordsPane().getChildren().clear();

                // Finish the game and show a notification with WPM
                Alert gameOver = new Alert(AlertType.CONFIRMATION,
                        "WPM: " + Math.round(WPM));
                gameOver.setTitle("Game Over");
                gameOver.showAndWait();

            }
        });
    }
}

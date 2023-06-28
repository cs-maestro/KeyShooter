import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 *  @author Muhammad Danish
 *
 * A class of the Typing Game that provide two utility functions.
 */
public class Utils {

    /*
     * Converts a list of KeyCode into a String
     * @param keyCodes List<KeyCode> to convert
     * @return String form of the keyCodes List
     */
    public static String combineList(List<KeyCode> keyCodes) {

        StringBuilder sb = new StringBuilder();

        for (KeyCode keyCode : keyCodes) {
            sb.append(keyCode.getChar());
        }

        return sb.toString();
    }

    /*
     * Collect Strings from the file
     * and add them to a List
     * @param path String to the file
     * @return List<String> words from the file
     */
    public static List<String> readWords(String path) throws FileNotFoundException {

        Scanner sc = new Scanner(new File(path));
        List<String> words = new ArrayList<>();

        while (sc.hasNextLine()) {
            words.add(sc.nextLine());
        }

        return words;
    }
}

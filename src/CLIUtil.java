import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A java library offering various functions for use in CLI-based applications
 * Functions include input/output, formatting, String manipulation and parsing,
 * and trim functions.
 */
public class CLIUtil {

    /**
     * Reads in all user-inputted text and returns it as a single String.
     * Text can be longer than a single word or line.
     * @return user-inputted text, or empty String if user did not enter anything
     */
    public static String getTextInput() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        /* Concatenate next token with input while more tokens exist */
        while(scanner.hasNext())
            input += scanner.next();
        return input;
    }

    /**
     * Uses System to print specified String.
     * @param s String to print
     */
    public static void print(String s) {
        System.out.print(s);
    }

    /**
     * Uses System to print a newline followed by the specified String.
     * @param s String to print after newline
     */
    public static void printLn(String s) {
        System.out.println(s);
    }

    /**
     * Uses System to print specified String, but limits the length
     * of the line to be printed. If the String is longer than the specified
     * columnWidth it will be printed on more than one line.
     * This function assumes it is printing text and will not split a word
     * over a line unless the length of the word is longer than the column
     * width.
     * @param s String to be printed
     * @param columnWidth max width of a line of text
     */
    public static void printFrmt(String s, int columnWidth) {
        /* Pattern to capture and group word characters and whitespace
         * characters seperately */
        Pattern tokenize_words = Pattern.compile("\\s+");
        Matcher m = tokenize_words.matcher(s);
        

    }

    public static int printMenu(String[] menuItems) {
        return 1;
    }

    public static void printList(String[] listItems) {

    }

    public static String trimL(String s) {
        return "";
    }

    public static String trimR(String s) {
        return "";
    }

    public static String normalizeText(String s) {
        return "";
    }

    public static String[] parseWords(String s) {
        return new String[1];
    }

    public static String[] parseLines(String s) {
        return new String[4];
    }
}

import java.util.ArrayList;
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
            System.out.println("Next: " + scanner.next());
        input += scanner.next(); // todo: runs forever
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
        /* Remove linebreaks from s (it will be reformatted with linebreaks) */
        s = s.replace("\\n|\\r", "");

        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> white_space = new ArrayList<>();
        ArrayList<String> word_tokens = new ArrayList<>();
        splitWords(s, white_space, word_tokens);

        /* Add first whitespace token to the first line (splitWords makes each String
         * start with "whitespace", even if it is the whitespace is a String of length
         * zero). If it is longer than columnWidth it must be split onto separate lines. */
        do {
            if(white_space.get(0).length() > columnWidth) { // todo: will this result in a column one longer than wanted? Does it matter?
                lines.add(white_space.get(0).substring(0, columnWidth) + "\n");
                white_space.set(0, white_space.get(0).substring(columnWidth));
            } else {
                lines.add(white_space.get(0));
            }
        } while(white_space.get(0).length() > columnWidth);


        /* Remove first index of white_space, allowing us to look at the rest of the
         * String as pairs of words followed by whitespace, not the other way around */
        white_space.remove(0);

        int current_line = lines.size() - 1;

        /* Loop through word_tokens and white_space. For each pair see if the word */
        for(int i = 0; i < word_tokens.size(); i++) {
            if(word_tokens.get(i).length() > columnWidth) {
                /* Substring word token to the length of space left in the column
                 * and add it to the current line */
                do {
                    lines.set(current_line, lines.get(current_line) +
                            word_tokens.get(i).substring(0, columnWidth - lines.get(current_line).length()) + "\n");
                    word_tokens.set(i, word_tokens.get(i).substring(columnWidth - lines.get(current_line).length()));
                } while(word_tokens.get(i).length() > columnWidth);
            }

            /* If word fits on this line, add it. If not, start a new line and add it */
            if(lines.get(current_line).length() + word_tokens.get(i).length() <= columnWidth) {
                lines.set(current_line, lines.get(current_line) + word_tokens.get(i));
            } else {
                lines.set(current_line, lines.get(current_line) + "\n");
                lines.add(word_tokens.get(i));
            }

            /* If whitespace fits on this line, add it. If not, ignore it and create a new line */
            if(lines.get(current_line).length() + white_space.get(i).length() <= columnWidth) {
                lines.set(current_line, lines.get(current_line) + white_space.get(i));
            } else {
                lines.set(current_line, lines.get(current_line) + "\n");
            }
        }

        String result = "";
        for(int i = 0; i < lines.size(); i++)
            result += lines.get(i);

        print(result);
    }

    /**
     * Uses regex patterns to split a String into whitespace tokens and word
     * tokens. Note: the String will always start and end with "whitespace",
     * even if the whitespace is a String of length zero. This means that, with
     * the exception of the first element of whitespace, each word is followed
     * by whitespace.
     * @param s String to split
     * @param whiteSpace ArrayList containing whiteSpace tokens
     * @param wordTokens ArrayList containing word tokens
     */
    private static void splitWords(String s, ArrayList<String> whiteSpace, ArrayList<String> wordTokens) {
        int last_index = 0;

        Pattern find_words = Pattern.compile("\\S+");
        Matcher matcher = find_words.matcher(s);

        /* Grab whitespace tokens in one list and put everything else in another list */
        while(matcher.find()) {
            wordTokens.add(matcher.group());
            whiteSpace.add(s.substring(last_index, matcher.start()));
            last_index = matcher.end() + 1;
        }

        /* Grab any remaining non-whitespace text. If there is none, add a String
         * of length zero to ensure each word is followed by whitespace. */
        if(last_index <= s.length())
            whiteSpace.add(s.substring(last_index));
        else
            whiteSpace.add("");
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

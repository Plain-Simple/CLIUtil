import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A java library offering various functions for use in CLI-based applications
 * Functions include input/output, formatting, String manipulation and parsing,
 * and trim functions.
 *
 * Contributor(s): Stefan Kussmaul, Plain+Simple 2015
 */
public class CLIUtil {

    /**
     * Reads in all user-inputted text and returns it as a single String.
     * Text can be longer than a single word, but cannot be longer than a line.
     * Note: returned String does not end in an endline character
     * @return user-inputted text, or empty String if user did not enter anything
     */
    public final static String getTextInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Uses System to print specified String.
     * @param s String to print
     */
    public final static void print(String s) {
        System.out.print(s);
    }

    /**
     * Uses System to print a newline followed by the specified String.
     * @param s String to print after newline
     */
    public final static void printLn(String s) {
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
    public final static void printFrmt(String s, int columnWidth) {
        /* Remove linebreaks from s (it will be reformatted with linebreaks) */
        s = s.replace("\\n|\\r", "");
        s = s.replace("\t", "     ");

        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> white_space = new ArrayList<>();
        ArrayList<String> word_tokens = new ArrayList<>();
        splitWords(s, white_space, word_tokens);

        /* Add first whitespace token to the first line (splitWords makes each String
         * start with "whitespace", even if the whitespace is a String of length
         * zero). If it is longer than columnWidth it must be split onto separate lines. */
        do {
            if(white_space.get(0).length() > columnWidth) {
                /* Add a full line of whitespace. Remaining whitespace is put on next line */
                lines.add(cloneString(" ", columnWidth) + "\n");
                white_space.set(0, white_space.get(0).substring(columnWidth));
            }
        } while(white_space.get(0).length() > columnWidth);

        lines.add(white_space.get(0));

        /* Remove first index of white_space, allowing us to look at the rest of the
         * String as pairs of words followed by whitespace, not the other way around */
        white_space.remove(0);

        int current_line = lines.size() - 1;
        System.out.println("current_line = " + current_line);

        /* Loop through word_tokens and white_space. For each pair see if the word */
        for(int i = 0; i < word_tokens.size(); i++) {
            /* Word token is longer than allowed column width - split it across lines */
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
                current_line++;
            }

            /* If whitespace fits on this line, add it. If not, ignore it and create a new line */
            if(lines.get(current_line).length() + white_space.get(i).length() <= columnWidth) {
                lines.set(current_line, lines.get(current_line) + white_space.get(i));
            } else {
                lines.set(current_line, lines.get(current_line) + "\n");
                lines.add("");
                current_line++;
            }

        }

        String result = "";
        for(int i = 0; i < lines.size(); i++)
            result += lines.get(i);

        printLn("\n" + result);
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
    public final static void splitWords(String s, ArrayList<String> whiteSpace, ArrayList<String> wordTokens) {
        int last_index = 0;

        Pattern find_words = Pattern.compile("\\S+");
        Matcher matcher = find_words.matcher(s);

        /* Grab whitespace tokens in one list and put everything else in another list */
        while(matcher.find()) {
            wordTokens.add(matcher.group());
            whiteSpace.add(s.substring(last_index, matcher.start()));
            last_index = matcher.end();
        }

        /* Grab any remaining non-whitespace text. If there is none, add a String
         * of length zero to ensure each word is followed by whitespace. */
        if(last_index <= s.length())
            whiteSpace.add(s.substring(last_index));
        else
            whiteSpace.add("");
    }

    /**
     * Displays a menu with each element of @menuItems on a seperate,
     * numbered line. Asks the user to choose one of the items,
     * and loops until the user enters a valid choice.
     * @param menuItems Strings to number and display as menu items
     * @return User-chosen menu item as index of @menuItems
     */
    public final static int printMenu(String[] menuItems) {
        int choice;
        for(int i = 0; i < menuItems.length; i++)
            print((i + 1) + ". " + menuItems[i] + "\n");
        do {
            printLn("Enter choice: ");
            String str_choice = getTextInput();
            try {
                choice = Integer.parseInt(str_choice);

                /* Validate choice to make sure it is in the correct range */
                if(choice > 0 && choice < menuItems.length)
                    return choice - 1;
                else
                    print("Error: Choice must be in the range of 1 to " + menuItems.length + "\n");
            } catch(NumberFormatException e) {
                print("Error: Choice must be an integer in the range of 1 to " + menuItems.length + "\n");
            }
        } while(true);
    }

    /**
     * Prints each element of listItems on a separate, numbered line.
     * @param listItems Strings to be listed and numbered
     */
    public final static void printList(String[] listItems) {
        for(int i = 0; i < listItems.length; i++)
            print((i + 1) + ". " + listItems[i] + "\n");
    }

    /**
     * Generates a String by concatenating toClone with itself
     * n times. If either parameter is null, the function returns
     * null. If toClone equals "" or n is equal to or less than 0,
     * an empty String is returned.
     * @param toClone String to be "cloned"
     * @param n number of times for @toClone to be repeated
     * @return String composed of @toClone repeated @n times
     */
    public final static String cloneString(String toClone, int n) {
        if(toClone == null || (Integer) n == null)
            return null;
        else if(toClone.equals("") || n <= 0)
            return "";

        String result = "";
        for(int i = 0; i < n; i++)
            result += toClone;
        return result;
    }

    /**
     * Removes all leading whitespace from a String.
     * @param s String to be trimmed
     * @return String with leading whitespace removed
     */
    public final static String trimL(String s) {
        Pattern non_whiteSpace = Pattern.compile("\\S+");
        Matcher m = non_whiteSpace.matcher(s);

        /* Find the first instance of non-whitespace and substring
         * from this point to the end of the String */
        if(m.find()) {
            return s.substring(m.start());
        } else
            return "";
    }

    /** Removes all trailing whitespace from a String.
     * @param s String to be trimmed
     * @return String with trailing whitespace removed
     */
    public final static String trimR(String s) {
        Pattern non_whiteSpace = Pattern.compile("\\S+");
        Matcher m = non_whiteSpace.matcher(s);

        /* Find the last location of non-whitespace */
        int last_index = 0;
        while(m.find()) {
            last_index = m.end();
        }

        /* No trailing whitespace */
        if(last_index == s.length())
            return s;
        else /* Substring only until last index of non-whitespace */
            return s.substring(0, last_index);
    }

    /**
     * Trims all leading and trailing whitespace and ensures that
     * each non-whitespace token is followed by only one space
     * @param s String to be trimmed and normalized
     * @return trimmed and processed String
     */
    public final static String normalizeText(String s) {
        return s.trim().replaceAll(" +", " ");
    }

    /**
     * Splits String by whitespace and returns an array containing
     * non-whitespace tokens. Anything surounded by single- or
     * double-quotes will be preserved, minus the quotes
     * (i.e. "file name" will be in the array as "file name",
     * not as "file" and "name".
     * @param s String to be split and parsed
     * @return an array of non-whitespace tokens with all whitespace removed
     */
    public final static String[] parseWords(String s) {
        ArrayList<String> tokens = new ArrayList<>();
        Pattern tokenize = Pattern.compile("([^\\s\"\']+)|\"([^\"]*)\"|\'([^\']*)\'");
        Matcher m = tokenize.matcher(s);
        while(m.find()) {
            if(m.group(1) != null) {
                tokens.add(m.group(1));
            } else if(m.group(2) != null) {
                tokens.add(m.group(2));
            } else if(m.group(3) != null) {
                tokens.add(m.group(3));
            }
        }

        return tokens.toArray(new String[tokens.size()]);
    }
}

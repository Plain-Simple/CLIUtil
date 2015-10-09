import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A java library for text output.
 * Copyright(C) Plain Simple Apps 2015
 * Licensed under GPL GNU Version 3 (see license.txt)
 * See plain-simple.github.io for more information.
 */
public class TextFormat {

    /**
     * Formats the String, limiting the length of the text body to the
     * specified column width. If the String is longer than the specified
     * column width it will be printed on more than one line.
     * This function assumes it is printing text and will not split a word
     * over a line unless the length of the word is longer than the column
     * width.
     *
     * @param text String to be printed
     * @param columnWidth max width of a line of text
     */
    public final static String formatWidth(String text, int columnWidth) { // todo: is this just leftJustify()?
        /* Remove linebreaks from s (it will be reformatted with linebreaks) */
        text = text.replace("\\n|\\r", "");
        text = text.replace("\t", "     ");

        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> white_space = new ArrayList<>();
        ArrayList<String> word_tokens = new ArrayList<>();
        splitWords(text, white_space, word_tokens);

        /* Loop breaks first whitespace token into lines of whitespace
         * if it is longer than columnWidth */
        while(white_space.get(0).length() > columnWidth) {
            /* Add a full line of whitespace. Remaining whitespace is put on next line */
            lines.add(cloneString(" ", columnWidth) + "\n");
            white_space.set(0, white_space.get(0).substring(columnWidth));
        }

        /* Add whitespace of length less than columnWidth */
        lines.add(white_space.get(0));

        /* Remove first index of white_space, allowing us to look at the rest of the
         * String as pairs of a word followed by whitespace, not the other way around */
        white_space.remove(0);

        int current_line = lines.size() - 1;

        for(int i = 0; i < word_tokens.size(); i++) {
            /* Word token is longer than allowed column width - split it across lines */
            while (word_tokens.get(i).length() > columnWidth) {
                String current_line_copy = lines.get(current_line);
                /* Substring word token to the length of space left in the line
                 * and add it to the current line */
                lines.set(current_line, lines.get(current_line) +
                        word_tokens.get(i).substring(0, columnWidth - lines.get(current_line).length()) + "\n");
                word_tokens.set(i, word_tokens.get(i).substring(columnWidth - current_line_copy.length()));
                lines.add("");
                current_line++;
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

        return result;
    }

    /**
     * Uses regex patterns to split a String into whitespace tokens and word
     * tokens. Note: the String will always start and end with "whitespace",
     * even if the whitespace is a String of length zero. This means that, with
     * the exception of the first element of whitespace, each word is followed
     * by whitespace.
     *
     * @param text String to split
     * @param whiteSpace ArrayList containing whiteSpace tokens
     * @param wordTokens ArrayList containing word tokens
     */
    private final static void splitWords(String text, ArrayList<String> whiteSpace, ArrayList<String> wordTokens) {
        int last_index = 0;

        Pattern find_words = Pattern.compile("\\S+");
        Matcher matcher = find_words.matcher(text);

        /* Grab whitespace tokens in one list and put everything else in another list */
        while(matcher.find()) {
            wordTokens.add(matcher.group());
            whiteSpace.add(text.substring(last_index, matcher.start()));
            last_index = matcher.end();
        }

        /* Grab any remaining non-whitespace text. If there is none, add a String
         * of length zero to ensure each word is followed by whitespace. */
        if(last_index <= text.length())
            whiteSpace.add(text.substring(last_index));
        else
            whiteSpace.add("");
    }



    /**
     * Generates a String by concatenating toClone with itself
     * n times. If either parameter is null, the function returns
     * null. If toClone equals "" or n is equal to or less than 0,
     * an empty String is returned.
     *
     * @param toClone String to be "cloned"
     * @param n number of times for @toClone to be repeated
     * @return String composed of @toClone repeated @n times
     */
    private final static String cloneString(String toClone, int n) {
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
     * Formats String to specified column width, then centers each // todo: better explanation
     * line of text in the column.
     *
     * @param text String to be center-formatted
     * @param columnWidth max number of characters per line
     * @return a String where text of each line is centered in the column
     */
    public final static String center(String text, int columnWidth) {
        String result = "";
        // apply basic formatting
        String width_formatted = formatWidth(text, columnWidth);
        String[] lines = width_formatted.split("\\n");
        for(int i = 0; i < lines.length; i++) {
            int leading_space = (columnWidth - lines[i].length()) / 2;
            result += cloneString(" ", leading_space) + lines[i] + "\n";
        }
        return result;
    }
    // todo: documentation, parameter names

    /**
     * Formats String to specified column width, then right-justifies
     * each line of text in the column.
     *
     * @param text String to be right-justified
     * @param columnWidth max number of characters per line
     * @return a String where the text of each line has been right-justified // todo: better explanation
     */
    public final static String rightJustify(String text, int columnWidth) {
        String result = "";
        // apply basic formatting
        String width_formatted = formatWidth(text, columnWidth);
        String[] lines = width_formatted.split("\\n");
        for(int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
            int leading_space = (columnWidth - lines[i].length());
            result += cloneString(" ", leading_space) + lines[i] + "\n";
        }
        return result;
    }
}

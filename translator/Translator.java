package translator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Translator {
    public static List<AncientChar> chars = initialiseFromFile();

    private static List<AncientChar> initialiseFromFile() {
        List<AncientChar> out = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            out.add(new AncientNum(i));
        }
        try (Scanner data = new Scanner(Path.of("alphabet"))) {
            while (data.hasNextLine()) {
                String id = data.next();
                String pixelData = data.next();
                List<Boolean> pixels = new ArrayList<>();
                for (char c : pixelData.toCharArray()) {
                    pixels.add(c == '1');
                }
                if (id.length() == 1) {
                    char plaintext = id.charAt(0);
                    out.add(new AncientChar(plaintext, pixels));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not open alphabet file: " + e);
            System.exit(1);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Malformed alphabet file: " + e);
        }
        {
            List<Boolean> spcPixels = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                spcPixels.add(false);
            }
            out.add(new AncientChar(' ', spcPixels));
        }

        return out;
    }

    /**
     * translate a given phrase to or from ancient
     *
     * @param toAncient whether to translate to ancient (if false translates from
     *                  ancient)
     * @param in        the phrase to translate
     * @return the translated phrase
     * @see #toAncient(String)
     * @see #fromAncient(String)
     */
    public static String translate(boolean toAncient, String in) {
        return toAncient ? toAncient(in) : fromAncient(in);
    }

    /**
     * translates teh given phrase from ancient to plain text
     *
     * @param in the phrase to translate
     * @return the translated phrase
     */
    private static String fromAncient(String in) {
        // get all teh lines from the input
        List<String> lines = new ArrayList<>();
        Scanner linesGetter = new Scanner(in);
        while (linesGetter.hasNext()) {
            lines.add(linesGetter.nextLine());
        }
        linesGetter.close();
        StringBuilder out = new StringBuilder();
        while (lines.size() >= 4) {
            // translate while there is still content to translate
            int strtInd = 0;
            int endInd = 3;
            while (endInd <= lines.get(0).length()) {
                String currChar;
                // extract the current char
                if (endInd == lines.get(0).length()) {
                    currChar = lines.get(0).substring(strtInd) + '\n'
                            + lines.get(1).substring(strtInd) + '\n'
                            + lines.get(2).substring(strtInd) + '\n'
                            + lines.get(3).substring(strtInd);
                } else {
                    currChar = lines.get(0).substring(strtInd, endInd) + '\n'
                            + lines.get(1).substring(strtInd, endInd) + '\n'
                            + lines.get(2).substring(strtInd, endInd) + '\n'
                            + lines.get(3).substring(strtInd, endInd);
                }
                for (AncientChar ch : chars) {
                    // look for a match and add it to the output
                    if (ch.toString().equals(currChar)) {
                        out.append(ch.plainText);
                        break;
                    }
                }
                strtInd += 4;
                endInd += 4;
            }
            // add new lines as needed
            if (lines.size() >= 9) {
                out.append('\n');
            }
            // remove processed data
            int numToRemove = Math.min(5, lines.size());
            for (int i = 0; i < numToRemove; i++) {
                lines.remove(0);
            }
        }
        return out.toString();
    }

    /**
     * translates the given phrase from plain text to ancient
     *
     * @param in the phrase as plain text
     * @return the translated phrase
     */
    private static String toAncient(String in) {
        List<String> outLines = new ArrayList<>();
        // prep initial sub lines
        for (int i = 0; i < 5; i++) {
            outLines.add("");
        }
        int currTopLine = 0;
        for (char c : in.toCharArray()) {
            if (c == '\n') {
                // create new sub lines as required
                for (int i = 0; i < 5; i++) {
                    outLines.add("");
                }
                currTopLine += 5;
            } else {
                // translate the current char
                List<String> outChar = null;
                for (AncientChar ancChar : chars) {
                    if (ancChar.plainText == c) {
                        outChar = ancChar.toLines();
                        break;
                    }
                }
                if (outChar == null) {
                    // skip if no match found
                    continue;
                }
                // add sub lines of current char to relevant sub lines
                for (int subLine = 0; subLine < outChar.size(); subLine++) {
                    outLines.set(currTopLine + subLine,
                            outLines.get(currTopLine + subLine)
                                    + (outLines.get(currTopLine + subLine).isEmpty() ? "" : ' ')
                                    + outChar.get(subLine));
                }
            }
        }
        StringBuilder out = new StringBuilder();
        // merge sub lines and lines into single string
        for (String line : outLines) {
            out.append(line);
            out.append('\n');
        }
        // remove trailing \n
        out.deleteCharAt(out.length() - 1);
        return out.toString();
    }

    private static String wrap(int wrapLength, String in) {
        if (wrapLength > 0) {
            StringBuilder wrapping = new StringBuilder(in);
            int blockSize = 0;
            for (int i = 0; i < wrapping.length(); i++) {
                if (blockSize > wrapLength) {
                    if (wrapping.charAt(i) == ' ') {
                        wrapping.setCharAt(i, '\n');
                        blockSize = -1;
                    } else {
                        wrapping.insert(i, '\n');
                        blockSize = 0;
                    }
                }
                blockSize++;
            }
            return wrapping.toString();
        }
        return in;
    }

    public static void main(String[] args) {
        boolean toAncient = true;
        int wrapLength = 0;
        Path inPath, outPath = null;
        int lastArg = -1;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--to-ancient" -> {
                toAncient = true;
                lastArg = i;
            }
            case "--from-ancient" -> {
                toAncient = false;
                lastArg = i;
            }
            case "--wrap-length" -> {
                try {
                    i++;
                    wrapLength = Integer.parseInt(args[i]);
                    lastArg = i;
                } catch (NumberFormatException e) {
                    System.out.println("wrap length must be an integer.");
                    System.exit(1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("You must specify a wrap length.");
                    System.exit(1);
                }
            }
            case "--help", "-h" -> {
                System.out.println("Usage:"
                        + "\n\t--to-ancient             translate to ancient (default)"
                        + "\n\t--from-ancient           translate from ancient"
                        + "\n\t--wrap-length <integer>  wrap the output so that a maximum number of characters are used per line"
                        + "\n\t--help, -h               shows this information");
                System.exit(0);
            }
            }
        }
        if (lastArg + 1 >= args.length) {
            System.out.println("You must supply a phrase to be translated.");
            System.exit(1);
        }
        String in = args[lastArg + 1];
        if (toAncient) {
            in = wrap(wrapLength, in);
        }
        String out = translate(toAncient, in);
        if (!toAncient) {
            out = wrap(wrapLength, out);
        }

        System.out.println(out);
    }
}

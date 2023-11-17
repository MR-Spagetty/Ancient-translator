package translator;

import java.util.List;
import java.util.ArrayList;

/**
 * AncientChar
 */
public class AncientChar {

    public final char plainText;
    private boolean[] ancient = new boolean[12];

    public AncientChar(char plainText, List<Boolean> pixels) {
        this.plainText = plainText;
        if (pixels.size() != 12) {
            throw new IllegalArgumentException(
                    "Ancient Char must have exactly 12 pixels specified.");
        }
        for (int i = 0; i < pixels.size(); i++) {
            this.ancient[i] = pixels.get(i);
        }
    }

    public List<String> toLines() {
        String str = toString();
        List<String> out = new ArrayList<>();
        int ind = 0;
        int nextInd = -1;
        do {
            nextInd = str.indexOf("\n", ind);
            if (nextInd != -1) {
                out.add(str.substring(ind, nextInd));
                ind = nextInd + 1;
            }
        } while (nextInd != -1);
        out.add(str.substring(ind));

        return out;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < this.ancient.length; i++) {
            if ((i / 3 > 0) && (i % 3 == 0)) {
                out.append('\n');
            }
            out.append(this.ancient[i] ? '█' : ' ');
        }
        return out.toString();
    }
}
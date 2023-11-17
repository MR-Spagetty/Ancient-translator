package translator;

import java.util.ArrayList;
import java.util.List;

public class AncientNum extends AncientChar {

    public AncientNum(int num) { this(("" + num).charAt(0), generatePixels(num)); }

    private AncientNum(char plainText, List<Boolean> pixels) { super(plainText, pixels); }

    @Override
    public String toString() {
        String out = super.toString();
        char[] outAr = out.toCharArray();
        outAr[outAr.length - 1] = '▀';
        outAr[outAr.length - 3] = '▀';
        out = new String(outAr);
        return out;
    }

    private static List<Boolean> generatePixels(int num) {
        if ((0 > num) || (num > 9)) {
            throw new IllegalArgumentException(
                    "Ancient Number may only be numbers between 0 and 9 (inclusive)");
        }
        List<Boolean> pixels = new ArrayList<>();
        if (num != 0) {
            for (int i = 0; i < 9; i++) {
                pixels.add(i < num);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                pixels.add((i / 3 == 0) || ((i - (i / 3 * 3)) % 2 == 0));
            }
        }
        for (int i = 0; i < 3; i++) {
            pixels.add(true);
        }
        return pixels;
    }
}
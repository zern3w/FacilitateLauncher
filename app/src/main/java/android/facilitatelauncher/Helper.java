package android.facilitatelauncher;

/**
 * Created by puttipongtadang on 1/14/18.
 */

public class Helper {
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
}

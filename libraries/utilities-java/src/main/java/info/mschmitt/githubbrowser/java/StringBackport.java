package info.mschmitt.githubbrowser.java;

/**
 * @author Matthias Schmitt
 */
public class StringBackport {
    public static String join(CharSequence delimiter, CharSequence... elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CharSequence s : elements) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
}

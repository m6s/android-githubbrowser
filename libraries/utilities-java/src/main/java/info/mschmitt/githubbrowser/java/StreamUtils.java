package info.mschmitt.githubbrowser.java;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Matthias Schmitt
 */
public class StreamUtils {
    public static String toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}

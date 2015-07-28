package info.mschmitt.githubapp.android.presentation;

/**
 * @author Matthias Schmitt
 */
public class ObjectsBackport {
    /**
     * Null-safe equivalent of {@code a.equals(b)}.
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}

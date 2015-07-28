package info.mschmitt.githubapp.android.presentation;

/**
 * @author Matthias Schmitt
 */
public interface OnBackPressedListener {
    OnBackPressedListener IGNORE = () -> false;

    boolean onBackPressed();
}

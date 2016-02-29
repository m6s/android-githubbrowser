package info.mschmitt.githubapp.android.presentation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author Matthias Schmitt
 */
public class FragmentUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getParent(@NonNull Fragment fragment, @NonNull Class<T> parentClass) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            try {
                return (T) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString() +
                        " must implement " + parentClass);
            }
        }
        try {
            return (T) fragment.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.getActivity().toString() +
                    " must implement " + parentClass);
        }
    }

    public static int getContainerViewId(Fragment fragment) {
        @SuppressWarnings("ConstantConditions") View parent = (View) fragment.getView().getParent();
        return parent.getId();
    }
}

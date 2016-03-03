package info.mschmitt.githubbrowser.android.databinding;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author Matthias Schmitt
 */
public class FragmentBindingUtils {
    public static int getContainerViewId(Fragment fragment) {
        @SuppressWarnings("ConstantConditions") View parent = (View) fragment.getView().getParent();
        return parent.getId();
    }
}

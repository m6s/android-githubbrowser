package info.mschmitt.githubapp.android.presentation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.lang.reflect.Field;

/**
 * https://code.google.com/p/android/issues/detail?id=74222
 */
public class BugFixFragment extends Fragment {
    private FragmentManager mRetainedChildFragmentManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mRetainedChildFragmentManager == null) {
            mRetainedChildFragmentManager = getChildFragmentManager();
        } else if (getRetainInstance()) {
            try {
                Field field = Fragment.class.getDeclaredField("mChildFragmentManager");
                field.setAccessible(true);
                field.set(this, mRetainedChildFragmentManager);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }
}

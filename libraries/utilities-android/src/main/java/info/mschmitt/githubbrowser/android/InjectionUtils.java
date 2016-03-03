package info.mschmitt.githubbrowser.android;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Matthias Schmitt
 */
public class InjectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getInjector(Fragment fragment, Class<T> injectorClass) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            try {
                return (T) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString() +
                        " must implement " + injectorClass);
            }
        }
        FragmentActivity activity = fragment.getActivity();
        try {
            return (T) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement " + injectorClass);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T retainAndGetInjector(Fragment fragment, Class<T> injectorClass) {
        fragment.setRetainInstance(true);
        Application application = fragment.getActivity().getApplication();
        try {
            return (T) application;
        } catch (ClassCastException e) {
            throw new ClassCastException(application.toString() +
                    " must implement " + injectorClass);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInjector(Activity activity, Class<T> injectorClass) {
        Application application = activity.getApplication();
        try {
            return (T) application;
        } catch (ClassCastException e) {
            throw new ClassCastException(application.toString() +
                    " must implement " + injectorClass);
        }
    }
}

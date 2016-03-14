package info.mschmitt.githubbrowser.android;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Matthias Schmitt
 */
public class FragmentUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getHost(Fragment fragment, Class<T> hostInterface) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            try {
                return (T) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString() +
                        " must implement " + hostInterface);
            }
        }
        FragmentActivity activity = fragment.getActivity();
        try {
            return (T) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement " + hostInterface);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T retainAndGetHost(Fragment fragment, Class<T> hostInterface) {
        fragment.setRetainInstance(true);
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null) {
            try {
                return (T) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString() +
                        " must implement " + hostInterface);
            }
        }
        Application application = fragment.getActivity().getApplication();
        try {
            return (T) application;
        } catch (ClassCastException e) {
            throw new ClassCastException(application.toString() +
                    " must implement " + hostInterface);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getHost(Activity activity, Class<T> hostInterface) {
        Application application = activity.getApplication();
        try {
            return (T) application;
        } catch (ClassCastException e) {
            throw new ClassCastException(application.toString() +
                    " must implement " + hostInterface);
        }
    }
}

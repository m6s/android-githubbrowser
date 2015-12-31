package info.mschmitt.githubapp.domain;

import android.text.TextUtils;

/**
 * @author Matthias Schmitt
 */
public class Validator {
    public boolean validateUsername(String username) {
        return !TextUtils.isEmpty(username);
    }
}

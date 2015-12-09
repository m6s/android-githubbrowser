package info.mschmitt.githubapp.domain;

import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Matthias Schmitt
 */
@Singleton
public class Validator {
    @Inject
    public Validator() {
    }

    public boolean validateUsername(String username) {
        return !TextUtils.isEmpty(username);
    }
}

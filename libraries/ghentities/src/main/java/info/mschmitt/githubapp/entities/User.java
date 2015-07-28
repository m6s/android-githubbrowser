package info.mschmitt.githubapp.entities;

import java.io.Serializable;

/**
 * @author Matthias Schmitt
 */
public class User implements Serializable {
    private String mLogin;
    private long mId;
    private String mUrl;
    private String mEmail;

    public User(String login, long id, String url, String email) {
        mLogin = login;
        mId = id;
        mUrl = url;
        mEmail = email;
    }

    public String getLogin() {
        return mLogin;
    }

    public long getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getEmail() {
        return mEmail;
    }
}

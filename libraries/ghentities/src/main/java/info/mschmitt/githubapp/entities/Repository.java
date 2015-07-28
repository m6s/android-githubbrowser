package info.mschmitt.githubapp.entities;

import java.io.Serializable;

/**
 * @author Matthias Schmitt
 */
public class Repository implements Serializable {
    private long mId;
    private String mName;
    private String mUrl;

    public Repository(long id, String name, String url) {
        mId = id;
        mName = name;
        mUrl = url;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }
}

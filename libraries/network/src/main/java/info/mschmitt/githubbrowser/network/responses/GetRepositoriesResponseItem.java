package info.mschmitt.githubbrowser.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * @author Matthias Schmitt
 */
public class GetRepositoriesResponseItem {
    public long id;
    public User owner;
    public String name;
    public String fullName;
    public String description;
    @SerializedName("private")
    public boolean xPrivate;
    public boolean fork;
    public String url;
    public String htmlUrl;
    public String cloneUrl;
    public String gitUrl;
    public String sshUrl;
    public String svnUrl;
    public String mirrorUrl;
    public String homepage;
    public String language;
    public int forksCount;
    public int stargazersCount;
    public int watchersCount;
    public int size;
    public String defaultBranch;
    public int openIssuesCount;
    public boolean hasIssues;
    public boolean hasWiki;
    public boolean hasPages;
    public boolean hasDownloads;
    public String pushedAt; //2011-01-26T19:06:43Z
    public String createdAt; //2011-01-26T19:01:12Z
    public String updatedAt; //2011-01-26T19:14:43Z
    public Permissions permissions;
}

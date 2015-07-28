package info.mschmitt.githubapp.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * @author Matthias Schmitt
 */
public class GetRepositoriesResponseItem {
    public long id;
    public User owner;
    public String name;
    public String full_name;
    public String description;
    @SerializedName("private")
    public boolean xPrivate;
    public boolean fork;
    public String url;
    public String html_url;
    public String clone_url;
    public String git_url;
    public String ssh_url;
    public String svn_url;
    public String mirror_url;
    public String homepage;
    public String language;
    public int forks_count;
    public int stargazers_count;
    public int watchers_count;
    public int size;
    public String default_branch;
    public int open_issues_count;
    public boolean has_issues;
    public boolean has_wiki;
    public boolean has_pages;
    public boolean has_downloads;
    public String pushed_at; //2011-01-26T19:06:43Z
    public String created_at; //2011-01-26T19:01:12Z
    public String updated_at; //2011-01-26T19:14:43Z
    public Permissions permissions;
}

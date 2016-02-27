package info.mschmitt.githubapp.entities;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * @author Matthias Schmitt
 */
@AutoValue
public abstract class User {
    User() {
    }

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }

    public abstract Builder toBuilder();

    public abstract long id();

    public abstract String url();

    @Nullable
    public abstract String login();

    @Nullable
    public abstract String email();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract User build();

        public abstract Builder id(long id);

        public abstract Builder url(String url);

        public abstract Builder login(@Nullable String login);

        public abstract Builder email(@Nullable String email);
    }
}

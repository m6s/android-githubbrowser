package info.mschmitt.githubapp.entities;

import com.google.auto.value.AutoValue;

/**
 * @author Matthias Schmitt
 */
@AutoValue
public abstract class Repository {
    Repository() {
    }

    public static Builder builder() {
        return new AutoValue_Repository.Builder();
    }

    public abstract Builder toBuilder();

    public abstract long id();

    public abstract String name();

    public abstract String url();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Repository build();

        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder url(String url);
    }
}

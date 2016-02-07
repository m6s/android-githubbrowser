package info.mschmitt.githubapp.entities;

import info.mschmitt.githubapp.java.Tuple4;

/**
 * @author Matthias Schmitt
 */
public class User {
    private final Tuple4<Long, String, String, String> mTuple4;

    public User(Builder builder) {
        mTuple4 = builder.mBuilder.build(tuple4 -> new Object[]{tuple4.component1});
    }

    public static Builder builder() {
        return new Builder(Tuple4.builder());
    }

    public long getId() {
        return mTuple4.component1;
    }

    public String getLogin() {
        return mTuple4.component2;
    }

    public String getUrl() {
        return mTuple4.component3;
    }

    public String getEmail() {
        return mTuple4.component4;
    }

    public Builder toBuilder() {
        return new Builder(mTuple4.toBuilder());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User && mTuple4.equals(((User) o).mTuple4);
    }

    @Override
    public int hashCode() {
        return mTuple4.hashCode();
    }

    @Override
    public String toString() {
        return mTuple4.toString();
    }

    public static class Builder {
        private final Tuple4.Builder<Long, String, String, String> mBuilder;

        private Builder(Tuple4.Builder<Long, String, String, String> tupleBuilder) {
            mBuilder = tupleBuilder;
        }

        public User build() {
            return new User(this);
        }

        public Builder id(long id) {
            mBuilder.component1(id);
            return this;
        }

        public Builder login(String login) {
            mBuilder.component2(login);
            return this;
        }

        public Builder url(String url) {
            mBuilder.component3(url);
            return this;
        }

        public Builder email(String email) {
            mBuilder.component4(email);
            return this;
        }
    }
}

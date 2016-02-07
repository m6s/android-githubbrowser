package info.mschmitt.githubapp.entities;

import info.mschmitt.githubapp.java.Tuple3;

/**
 * @author Matthias Schmitt
 */
public class Repository {
    private final Tuple3<Long, String, String> mTuple3;

    private Repository(Builder builder) {
        mTuple3 = builder.mBuilder.build(tuple3 -> new Object[]{tuple3.component1});
    }

    public static Builder builder() {
        return new Builder(Tuple3.builder());
    }

    public long getId() {
        return mTuple3.component1;
    }

    public String getName() {
        return mTuple3.component2;
    }

    public String getUrl() {
        return mTuple3.component3;
    }

    public Builder toBuilder() {
        return new Builder(mTuple3.toBuilder());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Repository && mTuple3.equals(((Repository) o).mTuple3);
    }

    @Override
    public int hashCode() {
        return mTuple3.hashCode();
    }

    @Override
    public String toString() {
        return mTuple3.toString();
    }

    public static class Builder {
        private final Tuple3.Builder<Long, String, String> mBuilder;

        private Builder(Tuple3.Builder<Long, String, String> tupleBuilder) {
            mBuilder = tupleBuilder;
        }

        public Repository build() {
            return new Repository(this);
        }

        public Builder id(long id) {
            mBuilder.component1(id);
            return this;
        }

        public Builder name(String name) {
            mBuilder.component2(name);
            return this;
        }

        public Builder url(String url) {
            mBuilder.component3(url);
            return this;
        }
    }
}

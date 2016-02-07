package info.mschmitt.githubapp.java;

/**
 * Use for value objects. This is for alternative solution #3 from the AutoValue presentation:
 * <p>
 * https://docs.google.com/presentation/d/14u_h-lMn7f1rXE1nDiLX0azS3IkgjGl5uxp5jGJ75RE/edit
 *
 * @author Matthias Schmitt
 */
public abstract class Tuple2<C1, C2> {
    public final C1 component1;
    public final C2 component2;

    public Tuple2(C1 component1, C2 component2) {
        this.component1 = component1;
        this.component2 = component2;
    }

    private Tuple2(Builder<C1, C2> builder) {
        component1 = builder.mComponent1;
        component2 = builder.mComponent2;
        checkNotNull();
    }

    protected void checkNotNull() {
        // Override to check for null values
    }

    public static <C1, C2> Builder<C1, C2> builder() {
        return new Builder<>(null, null);
    }

    @Override
    public int hashCode() {
        return ObjectsBackport.hash(component1, component2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple2)) {
            return false;
        }
        Tuple2 that = (Tuple2) o;
        return ObjectsBackport.equals(component1, that.component1) &&
                ObjectsBackport.equals(component2, that.component2);
    }

    @Override
    public String toString() {
        return "ValueObject3{" + component1 + ", " + component2 + '}';
    }

    public Builder<C1, C2> toBuilder() {
        return new Builder<>(component1, component2);
    }

    public static class Builder<C1, C2> {
        private C1 mComponent1;
        private C2 mComponent2;

        private Builder(C1 component1, C2 component2) {
            mComponent1 = component1;
            mComponent2 = component2;
        }

        public Tuple2<C1, C2> build(PropertySelector<C1, C2> nonNullPropertySelector) {
            return new Tuple2<C1, C2>(this) {
                @Override
                protected void checkNotNull() {
                    for (Object o : nonNullPropertySelector.call(this)) {
                        ObjectsBackport.requireNonNull(o);
                    }
                }
            };
        }

        public Builder<C1, C2> component1(C1 component1) {
            mComponent1 = component1;
            return this;
        }

        public Builder<C1, C2> component2(C2 component2) {
            mComponent2 = component2;
            return this;
        }

        public interface PropertySelector<C1, C2> {
            Object[] call(Tuple2<C1, C2> tuple3);
        }
    }
}

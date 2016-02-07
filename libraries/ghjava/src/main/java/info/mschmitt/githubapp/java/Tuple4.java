package info.mschmitt.githubapp.java;

/**
 * Use for value objects. This is for alternative solution #3 from the AutoValue presentation:
 * <p>
 * https://docs.google.com/presentation/d/14u_h-lMn7f1rXE1nDiLX0azS3IkgjGl5uxp5jGJ75RE/edit
 *
 * @author Matthias Schmitt
 */
public abstract class Tuple4<C1, C2, C3, C4> {
    public final C1 component1;
    public final C2 component2;
    public final C3 component3;
    public final C4 component4;

    public Tuple4(C1 component1, C2 component2, C3 component3, C4 component4) {
        this.component1 = component1;
        this.component2 = component2;
        this.component3 = component3;
        this.component4 = component4;
    }

    private Tuple4(Builder<C1, C2, C3, C4> builder) {
        component1 = builder.mComponent1;
        component2 = builder.mComponent2;
        component3 = builder.mComponent3;
        component4 = builder.mComponent4;
        checkNotNull();
    }

    protected void checkNotNull() {
        // Override to check for null values
    }

    public static <C1, C2, C3, C4> Builder<C1, C2, C3, C4> builder() {
        return new Builder<>(null, null, null, null);
    }

    @Override
    public int hashCode() {
        return ObjectsBackport.hash(component1, component2, component3, component4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple4)) {
            return false;
        }
        Tuple4 that = (Tuple4) o;
        return ObjectsBackport.equals(component1, that.component1) &&
                ObjectsBackport.equals(component2, that.component2) &&
                ObjectsBackport.equals(component3, that.component3) &&
                ObjectsBackport.equals(component4, that.component4);
    }

    @Override
    public String toString() {
        return "ValueObject3{" + component1 + ", " + component2 + ", " + component3 + ", " +
                component4 + '}';
    }

    public Builder<C1, C2, C3, C4> toBuilder() {
        return new Builder<>(component1, component2, component3, component4);
    }

    public static class Builder<C1, C2, C3, C4> {
        private C1 mComponent1;
        private C2 mComponent2;
        private C3 mComponent3;
        private C4 mComponent4;

        private Builder(C1 component1, C2 component2, C3 component3, C4 component4) {
            mComponent1 = component1;
            mComponent2 = component2;
            mComponent3 = component3;
            mComponent4 = component4;
        }

        public Tuple4<C1, C2, C3, C4> build(
                PropertySelector<C1, C2, C3, C4> nonNullPropertySelector) {
            return new Tuple4<C1, C2, C3, C4>(this) {
                @Override
                protected void checkNotNull() {
                    for (Object o : nonNullPropertySelector.call(this)) {
                        ObjectsBackport.requireNonNull(o);
                    }
                }
            };
        }

        public Builder<C1, C2, C3, C4> component1(C1 component1) {
            mComponent1 = component1;
            return this;
        }

        public Builder<C1, C2, C3, C4> component2(C2 component2) {
            mComponent2 = component2;
            return this;
        }

        public Builder<C1, C2, C3, C4> component3(C3 component3) {
            mComponent3 = component3;
            return this;
        }

        public Builder<C1, C2, C3, C4> component4(C4 component4) {
            mComponent4 = component4;
            return this;
        }

        public interface PropertySelector<C1, C2, C3, C4> {
            Object[] call(Tuple4<C1, C2, C3, C4> tuple3);
        }
    }
}

package raystark.eflib.function.recursive;

import org.jetbrains.annotations.NotNull;
import raystark.eflib.function.C1;

@FunctionalInterface
public interface RC1<T1> {
    @NotNull
    TailCall<Void> apply(T1 t1, RC1<T1> self);

    @NotNull
    default TailCall<Void> apply(T1 t1) {
        return apply(t1, this);
    }

    @NotNull
    static <T1> C1<T1> of(@NotNull RC1<T1> rc1) {
        return t1 -> rc1.apply(t1).get();
    }
}

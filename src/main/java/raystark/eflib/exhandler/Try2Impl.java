package raystark.eflib.exhandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import raystark.eflib.exhandler.function.STh2;
import raystark.eflib.function.A;
import raystark.eflib.function.F1;
import raystark.eflib.function.S;

class Try2Impl<T, X1 extends Throwable, X2 extends Throwable> implements Try2<T, X1, X2> {
    private final Class<X1> classX1;
    private final Class<X2> classX2;
    private final STh2<T, X1, X2> s;

    Try2Impl(@NotNull Class<X1> classX1, @NotNull Class<X2> classX2, @NotNull STh2<T, X1, X2> s) {
        this.classX1 = classX1;
        this.classX2 = classX2;
        this.s = s;
    }

    @Override
    @Nullable
    public T rawGet() throws X1, X2 {
        return s.get();
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public Try1<T, X2> recover1(
        @NotNull F1<? super X1, ? extends T> handlerX1,
        @NotNull A handlerFinally
    ) {
        return Try1.builder(classX2).build(() -> {
            try {
                return rawGet();
            } catch (Throwable x) {
                if(classX1.isInstance(x)) return handlerX1.apply((X1)x);
                if(classX2.isInstance(x)) throw (X2)x;
                throw new AssertionError("All throwable must be handled.", x);
            } finally {
                handlerFinally.run();
            }
        });
    }


    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public S<T> recover2(
        @NotNull F1<? super X1, ? extends T> handlerX1,
        @NotNull F1<? super X2, ? extends T> handlerX2,
        @NotNull A handlerFinally)
    {
        return S.of(() -> {
            try {
                return rawGet();
            } catch (Throwable x) {
                if(classX1.isInstance(x)) return handlerX1.apply((X1)x);
                if(classX2.isInstance(x)) return handlerX2.apply((X2)x);
                throw new AssertionError("All throwable must be handled.", x);
            } finally {
                handlerFinally.run();
            }
        });
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public Try2<T, X2, X1> swap2() {
        //例外型の宣言順は順不同なためキャストは安全
        return new Try2Impl<>(classX2, classX1, (STh2<T, X2, X1>)s);
    }
}

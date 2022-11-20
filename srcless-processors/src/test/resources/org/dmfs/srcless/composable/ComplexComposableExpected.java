package a.b.c;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Override
import java.lang.String;
import java.net.URI;
import javax.annotation.Nonnull;
import org.dmfs.srcless.composable.ComplexComposable;


/**
 * Test JavaDoc.
 */
public abstract class Complex<T> implements ComplexComposable<T>
{
    private final ComplexComposable<T> mDelegate;


    public Complex(ComplexComposable<T> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public final void voidMethod()
    {
        mDelegate.voidMethod();
    }


    @Override
    public final String throwingMethod() throws IOException
    {
        return mDelegate.throwingMethod()
    }


    @Override
    public final <V> T genericMethod(String s, V v, T t)
    {
        return mDelegate.genericMethod(s, v, t)
    }


    @Nonnull
    @Override
    public final Integer annotatedMethod(@Nonnull URI u)
    {
        return mDelegate.annotatedMethod(u)
    }
}

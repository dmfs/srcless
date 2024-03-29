package org.dmfs.srcless.composable;

import java.lang.Override
import java.lang.String;

/**
 * Test JavaDoc.
 */
public abstract class SimpleComposableComposition<T> implements SimpleComposable<T>
{
    private final SimpleComposable<T> mDelegate;


    public SimpleComposableComposition(SimpleComposable<T> delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public final String stringMethod(int i)
    {
        return mDelegate.stringMethod(i);
    }

}

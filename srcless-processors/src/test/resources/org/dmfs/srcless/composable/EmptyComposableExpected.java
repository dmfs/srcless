package org.dmfs.srcless.composable;

/**
 * Test JavaDoc.
 */
public abstract class EmptyComposableComposition<T> implements EmptyComposable<T>
{
    private final EmptyComposable<T> mDelegate;


    public EmptyComposableComposition(EmptyComposable<T> delegate)
    {
        mDelegate = delegate;
    }
}

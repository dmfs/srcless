package org.dmfs.srcless.staticfactory;

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    public static <W> GenericCtorFactory genericCtorFactory(W w)
    {
        return new GenericCtorFactory(w);
    }
}

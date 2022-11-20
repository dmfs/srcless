package org.dmfs.srcless.staticfactory;

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    public static <U, V, W> GenericFactory<U, V> genericFactory(U u, V v, W w)
    {
        return new org.dmfs.srcless.staticfactory.GenericFactory<>(u, v, w);
    }


    public static <U, V> GenericFactory<U, V> genericFactory(U u, V v)
    {
        return new org.dmfs.srcless.staticfactory.GenericFactory<>(u, v);
    }

}

package org.dmfs.srcless.staticfactory;

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    public static DefaultFactory defaultFactory()
    {
        return new org.dmfs.srcless.staticfactory.DefaultFactory();
    }
}

package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;

import java.io.IOException;


@StaticFactories("Factory")
public final class ThrowingFactory
{

    public ThrowingFactory(String s) throws IOException
    {

    }
}

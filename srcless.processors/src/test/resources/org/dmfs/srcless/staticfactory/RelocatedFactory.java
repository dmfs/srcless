package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;


@StaticFactories(packageName = "a.b.c", value = "Factory")
public final class RelocatedFactory
{
    public RelocatedFactory(String s)
    {
    }
}

package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;
import org.dmfs.srcless.annotations.staticfactory.StaticFactory;


@StaticFactories("Factory")
public final class SoleFactory
{
    public SoleFactory(Object o)
    {
    }


    @StaticFactory(packageName = "a.b.c", value = "StringFactory")
    public SoleFactory(String s)
    {
    }


    @StaticFactory("IntFactory")
    public SoleFactory(int i)
    {
    }

}

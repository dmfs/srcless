package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactory;


public final class SoleFactory
{
    public SoleFactory(Object o)
    {
    }


    @StaticFactory("Factory")
    public SoleFactory(String s)
    {
    }


    public SoleFactory(int i)
    {
    }

}

package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;


@StaticFactories("Factory")
public final class AnnotatedFactory
{
    @SafeVarargs
    public AnnotatedFactory(Object... o)
    {
    }


    @SuppressWarnings("unchecked")
    public AnnotatedFactory(String s)
    {
    }
}

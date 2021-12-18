package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;


@StaticFactories("Factory")
public final class GenericCtorFactory
{
    public <W> GenericCtorFactory(W w)
    {

    }
}

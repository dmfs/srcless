package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;


@StaticFactories("Factory")
public final class GenericFactory<U, V>
{
    public <W> GenericFactory(U u, V v, W w)
    {

    }


    public GenericFactory(U u, V v)
    {

    }
}

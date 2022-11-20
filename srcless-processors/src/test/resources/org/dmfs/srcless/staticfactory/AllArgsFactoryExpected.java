package org.dmfs.srcless.staticfactory;

import java.lang.String;
import javax.annotation.Nonnull;

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    public static AllArgsFactory allArgsFactory(@Nonnull String s, int i)
    {
        return new org.dmfs.srcless.staticfactory.AllArgsFactory(s, i);
    }

}


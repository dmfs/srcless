package org.dmfs.srcless.staticfactory;

import java.lang.String

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    public static SoleFactory soleFactory(String s)
    {
        return new SoleFactory(s);
    }
}


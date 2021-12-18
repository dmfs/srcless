package org.dmfs.srcless.staticfactory;

import java.io.IOException;
import java.lang.String;

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    public static ThrowingFactory throwingFactory(String s) throws IOException
    {
        return new ThrowingFactory(s);
    }
}


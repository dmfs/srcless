package org.dmfs.srcless.staticfactory;

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


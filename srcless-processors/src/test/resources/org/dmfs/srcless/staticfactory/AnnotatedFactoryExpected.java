package org.dmfs.srcless.staticfactory;

import java.lang.Object;
import java.lang.SafeVarargs;
import java.lang.String;
import java.lang.SuppressWarnings;

public final class Factory
{
    private Factory()
    {
        // no-instances contructor
    }


    @SafeVarargs
    public static AnnotatedFactory annotatedFactory(Object... o)
    {
        return new AnnotatedFactory(o);
    }


    @SuppressWarnings("unchecked")
    public static AnnotatedFactory annotatedFactory(String s)
    {
        return new AnnotatedFactory(s);
    }

}


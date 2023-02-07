package org.dmfs.nullless.sub;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Nonnull;


/**
 * Test JavaDoc.
 */
public interface SomeInterface<T>
{
    void voidMethod();

    String throwingMethod() throws IOException;

    <V> T genericMethod(String s, V v, T t);

    @Nonnull
    Integer annotatedMethod(@Nonnull URI u);
}

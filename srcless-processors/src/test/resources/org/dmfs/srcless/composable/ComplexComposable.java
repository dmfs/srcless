package org.dmfs.srcless.composable;

import org.dmfs.srcless.annotations.composable.Composable;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Nonnull;


/**
 * Test JavaDoc.
 */
@Composable(packageName = "a.b.c", className = "Complex")
public interface ComplexComposable<T>
{
    void voidMethod();

    String throwingMethod() throws IOException;

    <V> T genericMethod(String s, V v, T t);

    @Nonnull
    Integer annotatedMethod(@Nonnull URI u);
}

package org.dmfs.srcless.staticfactory;

import org.dmfs.srcless.annotations.staticfactory.StaticFactories;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;


@StaticFactories("Factory")
@AllArgsConstructor
public final class AllArgsFactory
{
    @Nonnull
    private final String s;
    public int i = 3;
    public final boolean b = true;
}

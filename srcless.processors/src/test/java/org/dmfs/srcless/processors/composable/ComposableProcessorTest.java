package org.dmfs.srcless.processors.composable;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;


class ComposableProcessorTest
{
    Compiler compiler;


    @BeforeEach
    public void setUpCompiler()
    {
        compiler = Compiler.javac().withProcessors(new ComposableProcessor());
    }


    @Test
    public void testEmpty()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/composable/EmptyComposable.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.composable.EmptyComposableComposition")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/composable/EmptyComposableExpected.java"));
    }


    @Test
    public void testSimple()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/composable/SimpleComposable.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.composable.SimpleComposableComposition")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/composable/SimpleComposableExpected.java"));
    }


    @Test
    public void testComplex()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/composable/ComplexComposable.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("a.b.c.Complex")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/composable/ComplexComposableExpected.java"));
    }
}
package org.dmfs.srcless.processors.staticfactory;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;


class StaticFactoryProcessorTest
{
    Compiler compiler;


    @BeforeEach
    public void setUpCompiler()
    {
        compiler = Compiler.javac().withProcessors(new StaticFactoryProcessor());
    }


    @Test
    public void testEmpty()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/staticfactory/EmptyFactory.java"));
        assertThat(compilation).succeeded();
    }


    @Test
    public void testDefault()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/staticfactory/DefaultFactory.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.staticfactory.Factory")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/staticfactory/DefaultFactoryExpected.java"));
    }


    @Test
    public void testSole()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/staticfactory/SoleFactory.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.staticfactory.Factory")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/staticfactory/SoleFactoryExpected.java"));
    }


    @Test
    public void testThrowing()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/staticfactory/ThrowingFactory.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.staticfactory.Factory")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/staticfactory/ThrowingFactoryExpected.java"));
    }


    @Test
    public void testGeneric()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/staticfactory/GenericFactory.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.staticfactory.Factory")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/staticfactory/GenericFactoryExpected.java"));
    }


    @Test
    public void testGenericCtor()
    {
        Compilation compilation = compiler.compile(forResource("org/dmfs/srcless/staticfactory/GenericCtorFactory.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.srcless.staticfactory.Factory")
            .hasSourceEquivalentTo(forResource("org/dmfs/srcless/staticfactory/GenericCtorFactoryExpected.java"));
    }
}
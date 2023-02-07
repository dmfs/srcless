package org.dmfs.nullless.processors.nullless;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forResource;


class NullLessProcessorTest
{
    Compiler compiler;


    @BeforeEach
    public void setUpCompiler()
    {
        compiler = Compiler.javac().withProcessors(new NullLessProcessor()).withClasspathFrom(this.getClass().getClassLoader());
    }


    @Test
    public void testComplex()
    {
        Compilation compilation = compiler.compile(
            forResource("org/dmfs/nullless/SomeClass.java"),
            forResource("org/dmfs/nullless/sub/SomeInterface.java"),
            forResource("org/dmfs/nullless/sub2/package-info.java"),
            forResource("org/dmfs/nullless/sub2/SomeEnum.java"));

        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("org.dmfs.nullless.package-info")
            .hasSourceEquivalentTo(forResource("org/dmfs/nullless/package-info.java"));
        assertThat(compilation).generatedSourceFile("org.dmfs.nullless.sub.package-info")
            .hasSourceEquivalentTo(forResource("org/dmfs/nullless/sub/package-info.java"));
    }
}
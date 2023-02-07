package org.dmfs.nullless.processors.nullless;

import org.dmfs.jems2.iterable.Distinct;
import org.dmfs.jems2.iterable.Mapped;
import org.dmfs.jems2.iterable.Sieved;
import org.dmfs.jems2.procedure.ForEach;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


@SupportedAnnotationTypes("*")
public final class NullLessProcessor extends AbstractProcessor
{
    private static final Set<ElementKind> TOP_LEVEL_OBJECTS = EnumSet.of(
        ElementKind.CLASS,
        ElementKind.INTERFACE,
        ElementKind.ENUM,
        ElementKind.ANNOTATION_TYPE
    );


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        new ForEach<>(pkgs(new Sieved<>(e -> TOP_LEVEL_OBJECTS.contains(e.getKind()), roundEnv.getRootElements())))
            .process(packageElement -> {
                try
                {
                    writePackageInfo(processingEnv.getFiler(), packageElement.getQualifiedName().toString());
                }
                catch (Exception e)
                {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Can't write package-info of package", packageElement);
                }
            });
        return false;
    }


    private Iterable<PackageElement> pkgs(Iterable<? extends Element> elements)
    {
        return new Distinct<>(new Mapped<>(this::pkg, elements));
    }


    private PackageElement pkg(Element element)
    {
        if (element instanceof PackageElement)
        {
            return (PackageElement) element;
        }
        return pkg(element.getEnclosingElement());
    }


    private void writePackageInfo(Filer filer, String packageName)
    {
        try
        {
            JavaFileObject builderFile = filer.createSourceFile(packageName + ".package-info");
            try (PrintWriter out = new PrintWriter(builderFile.openWriter()))
            {
                out.println("@NonNullByDefault\npackage " + packageName + ";\n\nimport org.eclipse.jdt.annotation.NonNullByDefault;");
            }
        }
        catch (IOException r)
        {
            // currently I don't know how to test for the existence of a package-info, so for now we just ignore the exception
        }
    }
}

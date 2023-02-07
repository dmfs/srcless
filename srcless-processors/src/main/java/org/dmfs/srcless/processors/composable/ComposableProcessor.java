package org.dmfs.srcless.processors.composable;

import com.squareup.javapoet.*;

import org.dmfs.jems2.iterable.Distinct;
import org.dmfs.jems2.iterable.Joined;
import org.dmfs.jems2.iterable.Mapped;
import org.dmfs.jems2.optional.Conditional;
import org.dmfs.jems2.optional.NullSafe;
import org.dmfs.jems2.optional.Present;
import org.dmfs.jems2.optional.Sieved;
import org.dmfs.jems2.predicate.Not;
import org.dmfs.jems2.procedure.ForEach;
import org.dmfs.jems2.single.Backed;
import org.dmfs.srcless.annotations.composable.Composable;
import org.dmfs.srcless.utils.PackageName;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;


@SupportedAnnotationTypes("org.dmfs.srcless.annotations.composable.Composable")
public final class ComposableProcessor extends AbstractProcessor
{

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        for (Element element : roundEnv.getElementsAnnotatedWith(Composable.class))
        {
            JavaFile file = composableClass(
                new Backed<>(
                    new Sieved<>(
                        new Not<>(String::isEmpty),
                        new Present<>(element.getAnnotation(Composable.class).packageName())),
                    new PackageName((TypeElement) element)).value(),
                new Backed<>(
                    new Sieved<>(
                        new Not<>(String::isEmpty),
                        new Present<>(element.getAnnotation(Composable.class).className())),
                    () -> (element.getSimpleName().toString() + "Composition")).value(), (TypeElement) element,
                new Mapped<>(e -> (ExecutableElement) e,
                    new org.dmfs.jems2.iterable.Sieved<>(e -> e.getKind() == ElementKind.METHOD, element.getEnclosedElements())));
            try
            {
                if (processingEnv.getElementUtils().getTypeElement(file.packageName + "." + file.typeSpec.name) == null)
                {
                    file.writeTo(processingEnv.getFiler());
                }
            }
            catch (IOException e)
            {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "can't write Composable output class.", element);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }
        return true;
    }


    private JavaFile composableClass(String packageName, String className, TypeElement clazz, Iterable<ExecutableElement> methods)
    {
        return JavaFile.builder(
                packageName,
                TypeSpec.classBuilder(className)
                    .addTypeVariables(new Mapped<>(TypeVariableName::get, clazz.getTypeParameters()))
                    .addSuperinterface(TypeName.get(clazz.asType()))
                    .addJavadoc(new Backed<>(new NullSafe<>(processingEnv.getElementUtils().getDocComment(clazz)), "").value())
                    .addJavadoc("Automatically generated class for composition.")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addField(TypeName.get(clazz.asType()), "mDelegate", Modifier.PRIVATE, Modifier.FINAL)
                    .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(TypeName.get(clazz.asType()), "delegate").build())
                        .addModifiers(Modifier.PUBLIC)
                        .addCode("mDelegate = delegate;")
                        .build())
                    .addMethods(new Mapped<>(this::codeOfMethod, methods)).build())
            .build();
    }


    private MethodSpec codeOfMethod(ExecutableElement method)
    {
        MethodSpec.Builder builder =
            MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotations(new Distinct<>(new Joined<>(
                    new Mapped<>(AnnotationSpec::get, method.getAnnotationMirrors()),
                    AnnotationSpec.builder(Override.class).build())))
                .addTypeVariables(new Mapped<>(TypeVariableName::get, method.getTypeParameters()))
                .varargs(method.isVarArgs())
                .addParameters(new Mapped<>(param ->
                    ParameterSpec.builder(TypeName.get(param.asType()), param.getSimpleName().toString())
                        .addModifiers(param.getModifiers())
                        .addAnnotations(new Mapped<>(AnnotationSpec::get, param.getAnnotationMirrors()))
                        .build(),
                    method.getParameters()))
                .addExceptions(new Mapped<>(TypeName::get, method.getThrownTypes()))
                .addStatement("$1LmDelegate.$2L($3L)", method.getReturnType().getKind() == TypeKind.VOID ? "" : "return ", method.getSimpleName(),
                    method.getParameters());

        new ForEach<>(new org.dmfs.jems2.optional.Mapped<>(TypeName::get, new Conditional<>(t -> t.getKind() != TypeKind.VOID, method.getReturnType())))
            .process(builder::returns);
        new ForEach<>(new NullSafe<>(processingEnv.getElementUtils().getDocComment(method))).process(builder::addJavadoc);

        return builder.build();
    }

}

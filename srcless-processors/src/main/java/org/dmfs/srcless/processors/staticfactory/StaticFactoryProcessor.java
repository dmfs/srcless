package org.dmfs.srcless.processors.staticfactory;

import com.squareup.javapoet.*;

import org.dmfs.jems2.comparator.By;
import org.dmfs.jems2.iterable.*;
import org.dmfs.jems2.optional.First;
import org.dmfs.jems2.optional.FirstPresent;
import org.dmfs.jems2.optional.NullSafe;
import org.dmfs.jems2.predicate.Not;
import org.dmfs.jems2.procedure.ForEach;
import org.dmfs.srcless.annotations.staticfactory.StaticFactories;
import org.dmfs.srcless.annotations.staticfactory.StaticFactory;
import org.dmfs.srcless.utils.Constructors;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


/**
 * An annotation processor that generates static factory methods for any non-private constructor that's annotated with @{@link StaticFactory} or
 * all public constructors of a class annotated with @{@link StaticFactories}.
 */
@SupportedAnnotationTypes({ "org.dmfs.srcless.annotations.staticfactory.StaticFactory", "org.dmfs.srcless.annotations.staticfactory.StaticFactories" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public final class StaticFactoryProcessor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        new ForEach<>(
            new org.dmfs.jems2.iterable.Mapped<>(
                ctors -> staticFactoryContainer(ctors.iterator().next().targetPackage(), ctors.iterator().next().targetClass(), ctors),
                new Clustered<>(
                    new By<AnnotatedCtor>(ctor -> ctor.targetPackage() + "." + ctor.targetClass()),
                    new Sorted<>(
                        new By<>(ctor -> ctor.targetPackage() + "." + ctor.targetClass()),
                        new Joined<>(
                            new Sieved<>(
                                ctor -> !ctor.ctor().getModifiers().contains(Modifier.PRIVATE),
                                new org.dmfs.jems2.iterable.Mapped<>(
                                    ctor -> new ClassCtor((TypeElement) ctor.getEnclosingElement(), (ExecutableElement) ctor),
                                    new Sieved<>(
                                        c -> c.getKind() == ElementKind.CONSTRUCTOR,
                                        roundEnv.getElementsAnnotatedWith(StaticFactory.class)))),
                            new Sieved<>(
                                ctor -> ctor.ctor().getAnnotation(StaticFactory.class) == null && ctor.ctor().getModifiers().contains(Modifier.PUBLIC),
                                new Expanded<>(
                                    clazz -> new org.dmfs.jems2.iterable.Mapped<>(ctor -> new ClassCtor((TypeElement) clazz, ctor),
                                        new Constructors((TypeElement) clazz)),
                                    roundEnv.getElementsAnnotatedWith(StaticFactories.class))))))))
            .process(file -> {
                try
                {
                    file.writeTo(processingEnv.getFiler());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });

        return true;
    }


    /**
     * Create a {@link JavaFile} that contains all the static factory methods for the given constructors.
     */
    private JavaFile staticFactoryContainer(String packageName, String className, Iterable<ClassCtor> ctor)
    {
        return JavaFile.builder(
                packageName,
                TypeSpec.classBuilder(className)
                    .addJavadoc("Automatically generated class with static factory methods.")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).addComment("no-instances constructor").build())
                    .addMethods(new Mapped<>(this::staticFactoryMethod, ctor)).build())
            .build();
    }


    /**
     * Creates a {@link MethodSpec} of a static factory method for the given Constructor.
     */
    private MethodSpec staticFactoryMethod(ClassCtor ctor)
    {
        MethodSpec.Builder builder =
            MethodSpec.methodBuilder(factoryMethodName(ctor))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addExceptions(new Mapped<>(TypeName::get, ctor.ctor().getThrownTypes()))
                .addAnnotations(new Mapped<>(AnnotationSpec::get,
                    new Sieved<>(
                        an -> new First<>(ElementType.METHOD::equals,
                            new Expanded<>(
                                Seq::new,
                                new NullSafe<>(an.getAnnotationType().asElement().getAnnotation(Target.class)))).isPresent(),
                        ctor.ctor().getAnnotationMirrors())))
                .addTypeVariables(new Mapped<>(TypeVariableName::get, new Joined<>(ctor.clazz().getTypeParameters(), ctor.ctor().getTypeParameters())))
                .returns(TypeName.get(ctor.clazz().asType()))
                .varargs(ctor.ctor().isVarArgs())
                .addParameters(new Mapped<>(param ->
                    ParameterSpec.builder(TypeName.get(param.asType()), param.getSimpleName().toString())
                        .addModifiers(param.getModifiers())
                        .addAnnotations(new Mapped<>(AnnotationSpec::get, param.getAnnotationMirrors()))
                        .build(),
                    ctor.ctor().getParameters()))
                .addStatement("return new $1L$2L($3L)", ctor.clazz().getSimpleName(),
                    ctor.clazz().getTypeParameters().iterator().hasNext() ? "<>" : "",
                    ctor.ctor().getParameters());
        new ForEach<>(new NullSafe<>(processingEnv.getElementUtils().getDocComment(ctor.ctor()))).process(builder::addJavadoc);

        return builder.build();
    }


    /**
     * Returns the name of the factory method.
     */
    private String factoryMethodName(ClassCtor ctor)
    {
        return new FirstPresent<>(
            new org.dmfs.jems2.optional.Sieved<>(new Not<>(String::isEmpty),
                new org.dmfs.jems2.optional.Mapped<>(StaticFactory::methodName, new NullSafe<>(ctor.ctor().getAnnotation(StaticFactory.class)))),
            new org.dmfs.jems2.optional.Just<>(() -> {
                String constructorName = ctor.ctor().getEnclosingElement().getSimpleName().toString();
                return Character.toLowerCase(constructorName.charAt(0)) + constructorName.substring(1);
            })).value();
    }

}

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
import javax.lang.model.element.*;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;


/**
 * An annotation processor that generates static factory methods for any non-private constructor that's annotated with @{@link StaticFactory} or
 * all public constructors of a class annotated with @{@link StaticFactories}.
 */
@SupportedAnnotationTypes({ "org.dmfs.srcless.annotations.staticfactory.StaticFactory", "org.dmfs.srcless.annotations.staticfactory.StaticFactories" })
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
                            new Expanded<>(
                                clazz -> new LombokConstructors((TypeElement) clazz),
                                roundEnv.getElementsAnnotatedWith(StaticFactories.class)),
                            new Sieved<>(
                                ctor -> !ctor.description().hasModifier(PRIVATE),
                                new org.dmfs.jems2.iterable.Mapped<>(
                                    ctor -> new ClassCtor((TypeElement) ctor.getEnclosingElement(), (ExecutableElement) ctor),
                                    new Sieved<>(
                                        c -> c.getKind() == ElementKind.CONSTRUCTOR,
                                        roundEnv.getElementsAnnotatedWith(StaticFactory.class)))),
                            new Sieved<>(
                                ctor -> ctor.description().annotation(StaticFactory.class) == null && ctor.description().hasModifier(PUBLIC),
                                new Expanded<>(
                                    clazz -> new org.dmfs.jems2.iterable.Mapped<>(ctor -> new ClassCtor((TypeElement) clazz, ctor),
                                        new Sieved<>(
                                            new Not<>(new IsDefaultCtorInLombokClass()), // exclude default ctor of lombok annotated classes
                                            new Constructors((TypeElement) clazz))),
                                    roundEnv.getElementsAnnotatedWith(StaticFactories.class))))))))
            .process(file -> {
                try
                {
                    if (processingEnv.getElementUtils().getTypeElement(file.packageName + "." + file.typeSpec.name) == null)
                    {
                        file.writeTo(processingEnv.getFiler());
                    }
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
    private JavaFile staticFactoryContainer(String packageName, String className, Iterable<? extends AnnotatedCtor> ctor)
    {
        return JavaFile.builder(
                packageName,
                TypeSpec.classBuilder(className)
                    .addJavadoc("Automatically generated class with static factory methods.")
                    .addModifiers(PUBLIC, Modifier.FINAL)
                    .addMethod(MethodSpec.constructorBuilder().addModifiers(PRIVATE).addComment("no-instances constructor").build())
                    .addMethods(new Mapped<>(this::staticFactoryMethod, ctor)).build())
            .build();
    }


    /**
     * Creates a {@link MethodSpec} of a static factory method for the given Constructor.
     */
    private MethodSpec staticFactoryMethod(AnnotatedCtor ctor)
    {
        CtorDescription ctorDescription = ctor.description();

        MethodSpec.Builder builder =
            MethodSpec.methodBuilder(factoryMethodName(ctorDescription))
                .addModifiers(PUBLIC, Modifier.STATIC)
                .addExceptions(new Mapped<>(TypeName::get, ctorDescription.thrownExceptions()))
                .addAnnotations(new Mapped<>(AnnotationSpec::get,
                    new Sieved<>(
                        an -> new First<ElementType>(ElementType.METHOD::equals,
                            new Expanded<>(
                                target -> new Seq<>(target.value()),
                                new NullSafe<>(an.getAnnotationType().asElement().getAnnotation(Target.class)))).isPresent(),
                        ctorDescription.annotationMirrors())))
                .addTypeVariables(new Mapped<>(TypeVariableName::get, new Joined<>(ctor.clazz().getTypeParameters(), ctorDescription.typeParameters())))
                .returns(TypeName.get(ctor.clazz().asType()))
                .varargs(ctorDescription.hasVarArgs())
                .addParameters(new Mapped<>(param ->
                    ParameterSpec.builder(TypeName.get(param.asType()), param.getSimpleName().toString())
                        .addAnnotations(new Mapped<>(AnnotationSpec::get, param.getAnnotationMirrors()))
                        .build(),
                    ctorDescription.parameters()))
                .addStatement("return new $1L$2L($3L)",
                    ctorDescription.fqClass(),
                    ctor.clazz().getTypeParameters().isEmpty() ? "" : "<>",
                    String.join(", ", new Mapped<>(VariableElement::getSimpleName, ctorDescription.parameters())));

        builder.addJavadoc(ctorDescription.javaDoc(processingEnv));

        return builder.build();
    }


    /**
     * Returns the name of the factory method.
     */
    private String factoryMethodName(CtorDescription ctor)
    {
        return new FirstPresent<>(
            new org.dmfs.jems2.optional.Sieved<>(new Not<>(String::isEmpty),
                new org.dmfs.jems2.optional.Mapped<>(StaticFactory::methodName, new NullSafe<>(ctor.annotation(StaticFactory.class)))),
            new org.dmfs.jems2.optional.Just<>(() -> {
                String constructorName = ctor.name();
                return Character.toLowerCase(constructorName.charAt(0)) + constructorName.substring(1);
            })).value();
    }

}

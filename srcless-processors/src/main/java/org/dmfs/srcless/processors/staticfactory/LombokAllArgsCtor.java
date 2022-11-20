package org.dmfs.srcless.processors.staticfactory;

import org.dmfs.jems2.iterable.Expanded;
import org.dmfs.jems2.iterable.Seq;
import org.dmfs.jems2.optional.*;
import org.dmfs.jems2.predicate.Not;
import org.dmfs.jems2.single.Collected;
import org.dmfs.srcless.annotations.staticfactory.StaticFactories;
import org.dmfs.srcless.utils.PackageName;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static org.dmfs.jems2.iterable.EmptyIterable.emptyIterable;


public final class LombokAllArgsCtor implements AnnotatedCtor
{
    private final TypeElement mClassElement;


    public LombokAllArgsCtor(TypeElement classElement)
    {
        mClassElement = classElement;
    }


    @Override
    public String targetPackage()
    {
        return new FirstPresent<>(
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactories::packageName, new NullSafe<>(mClassElement.getAnnotation(StaticFactories.class)))),
            new Just<>(new PackageName(mClassElement))
        ).value();
    }


    @Override
    public String targetClass()
    {
        return new FirstPresent<>(
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactories::value, new NullSafe<>(mClassElement.getAnnotation(StaticFactories.class))))
        ).value();
    }


    @Override
    public TypeElement clazz()
    {
        return mClassElement;
    }


    @Override
    public CtorDescription description()
    {
        return new CtorDescription()
        {
            @Override
            public String name()
            {
                return mClassElement.getSimpleName().toString();
            }


            @Override
            public String fqClass()
            {
                return mClassElement.getQualifiedName().toString();
            }


            @Override
            public boolean hasModifier(Modifier modifier)
            {
                // we only handle public ctors
                return PUBLIC.equals(modifier);
            }


            @Override
            public Iterable<? extends TypeMirror> thrownExceptions()
            {
                return emptyIterable();
            }


            @Override
            public <T extends Annotation> T annotation(Class<T> clazz)
            {
                if (new org.dmfs.jems2.iterable.Sieved<ElementType>(
                    ElementType.CONSTRUCTOR::equals,
                    new Expanded<>(target -> new Seq<>(target.value()), new Seq<>(clazz.getAnnotationsByType(Target.class)))).iterator().hasNext())
                {
                    return mClassElement.getAnnotation(clazz);
                }
                return null;
            }


            @Override
            public Iterable<? extends VariableElement> parameters()
            {
                return new Collected<>(ArrayList::new,
                    new org.dmfs.jems2.iterable.Sieved<>(
                        field -> !(field.getModifiers().contains(Modifier.FINAL) && field.getConstantValue() != null)
                            && !field.getModifiers().contains(STATIC),
                        new org.dmfs.jems2.iterable.Mapped<>(
                            field -> (VariableElement) field,
                            new org.dmfs.jems2.iterable.Sieved<>(
                                element -> element.getKind() == ElementKind.FIELD,
                                mClassElement.getEnclosedElements())))).value();
            }


            @Override
            public String javaDoc(ProcessingEnvironment processingEnv)
            {
                return "";
            }


            @Override
            public Iterable<? extends AnnotationMirror> annotationMirrors()
            {
                return new org.dmfs.jems2.iterable.Sieved<>(
                    testedInstance -> new org.dmfs.jems2.iterable.Sieved<ElementType>(
                        ElementType.CONSTRUCTOR::equals,
                        new Expanded<>(target -> new Seq<>(target.value()),
                            new Seq<>(testedInstance.getAnnotationType().getAnnotationsByType(Target.class)))).iterator().hasNext(),
                    mClassElement.getAnnotationMirrors());
            }


            @Override
            public boolean hasVarArgs()
            {
                // TODO we could return true if the last parameter is an array
                // in that case we might have to add the @SafeVarargs annotation if the array has a generic type
                return false;
            }


            @Override
            public Iterable<? extends TypeParameterElement> typeParameters()
            {
                return emptyIterable();
            }
        };
    }
}

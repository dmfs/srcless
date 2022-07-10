package org.dmfs.srcless.processors.staticfactory;

import org.dmfs.jems2.optional.*;
import org.dmfs.jems2.predicate.Not;
import org.dmfs.jems2.single.Backed;
import org.dmfs.srcless.annotations.staticfactory.StaticFactories;
import org.dmfs.srcless.annotations.staticfactory.StaticFactory;
import org.dmfs.srcless.utils.PackageName;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;


class ClassCtor implements AnnotatedCtor
{
    private final TypeElement mClassElement;
    private final ExecutableElement mCtorElement;


    public ClassCtor(TypeElement classElement, ExecutableElement ctorElement)
    {
        mClassElement = classElement;
        mCtorElement = ctorElement;
    }


    @Override
    public String targetPackage()
    {
        return new FirstPresent<>(
            new Sieved<>(new Not<>(String::isEmpty),
                new Mapped<>(StaticFactory::packageName, new NullSafe<>(mCtorElement.getAnnotation(StaticFactory.class)))),
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
                new Mapped<>(StaticFactory::value, new NullSafe<>(mCtorElement.getAnnotation(StaticFactory.class)))),
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
            public boolean hasModifier(Modifier modifier)
            {
                return mCtorElement.getModifiers().contains(modifier);
            }


            @Override
            public Iterable<? extends TypeMirror> thrownExceptions()
            {
                return mCtorElement.getThrownTypes();
            }


            @Override
            public <T extends Annotation> T annotation(Class<T> clazz)
            {
                return mCtorElement.getAnnotation(clazz);
            }


            @Override
            public Iterable<? extends VariableElement> parameters()
            {
                return mCtorElement.getParameters();
            }


            @Override
            public String javaDoc(ProcessingEnvironment processingEnv)
            {
                return new Backed<>(new NullSafe<>(processingEnv.getElementUtils().getDocComment(mClassElement)), "").value();
            }


            @Override
            public Iterable<? extends AnnotationMirror> annotationMirrors()
            {
                return mCtorElement.getAnnotationMirrors();
            }


            @Override
            public boolean hasVarArgs()
            {
                return mCtorElement.isVarArgs();
            }


            @Override
            public Iterable<? extends TypeParameterElement> typeParameters()
            {
                return mCtorElement.getTypeParameters();
            }
        };
    }
}

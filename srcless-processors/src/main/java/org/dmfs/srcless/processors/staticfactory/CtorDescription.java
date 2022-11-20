package org.dmfs.srcless.processors.staticfactory;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;


public interface CtorDescription
{
    String name();

    String fqClass();

    boolean hasModifier(Modifier modifier);

    Iterable<? extends TypeMirror> thrownExceptions();

    <T extends Annotation> T annotation(Class<T> clazz);

    Iterable<? extends VariableElement> parameters();

    String javaDoc(ProcessingEnvironment processingEnv);

    Iterable<? extends AnnotationMirror> annotationMirrors();

    boolean hasVarArgs();

    Iterable<? extends TypeParameterElement> typeParameters();
}
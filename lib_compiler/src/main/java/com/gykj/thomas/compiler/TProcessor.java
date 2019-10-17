package com.gykj.thomas.compiler;

import com.gykj.thomas.aspectj.AspectjHelper;
import com.gykj.thomas.aspectj.OkAspectj;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class TProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(OkAspectj.class.getCanonicalName());
        return annotationTypes;
    }
/*
*
*   MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        NeedLogin annotation = methodSignature.getMethod().getAnnotation(NeedLogin.class);

* */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> okAspectjElements = roundEnv.getElementsAnnotatedWith(OkAspectj.class);
        for (Element element : okAspectjElements) {
            TypeElement classElement = (TypeElement) element;
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();

            AnnotationSpec pointcut = AnnotationSpec.builder(Pointcut.class)
                    .addMember("value", "\"execution(@" + classElement.getQualifiedName() + " * *(..))\"").build();
            MethodSpec pointcutMethod = MethodSpec.methodBuilder("pointcut" + classElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addAnnotation(pointcut)
                    .build();
            AnnotationSpec around = AnnotationSpec.builder(Around.class)
                    .addMember("value", "\"" + pointcutMethod.name + "()\"").build();
            MethodSpec aroundMethod = MethodSpec.methodBuilder("around" + classElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(ProceedingJoinPoint.class, "joinPoint")
                    .addAnnotation(around)
                    .addStatement("if (!(joinPoint.getSignature() instanceof $T)){\n\tthrow new $T(\"This annotation can only be used on Methods\");\n}"
                            , MethodSignature.class,IllegalStateException.class)
                    .addStatement("MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();")
                    .addStatement(classElement.getSimpleName()+" annotation = methodSignature.getMethod().getAnnotation("+classElement.getSimpleName()+".class);")
                    .addStatement("$T.notifyHandler("+classElement.getSimpleName()+".class,joinPoint)", AspectjHelper.class)
                    .build();

            TypeSpec OkAspectj = TypeSpec.classBuilder(classElement.getSimpleName().toString() + "_Aspectj")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(pointcutMethod)
                    .addMethod(aroundMethod)
                    .addAnnotation(AnnotationSpec.builder(Aspect.class).build())
                    .build();
            JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), OkAspectj).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
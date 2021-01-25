package com.thomas.okaspectj;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.thomas.aspectj.OkAspectj;


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
public class OkAspectjProcessor extends AbstractProcessor {
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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> okAspectjElements = roundEnv.getElementsAnnotatedWith(OkAspectj.class);
        for (Element element : okAspectjElements) {
            TypeElement classElement = (TypeElement) element;
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();
            AnnotationSpec.Builder pointcutBuilder = AnnotationSpec.builder(ClassName.get("org.aspectj.lang.annotation", "Pointcut"));
            OkAspectj annotation = element.getAnnotation(OkAspectj.class);
            String value = annotation.value();
            if (value.length() != 0) {
                pointcutBuilder.addMember("value","\""+ value +"\"");
            } else {
                pointcutBuilder.addMember("value", "\"execution(@" + classElement.getQualifiedName() + " * *(..))\"").build();
            }
            AnnotationSpec pointcut = pointcutBuilder.build();
            MethodSpec pointcutMethod = MethodSpec.methodBuilder("pointcut" + classElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addAnnotation(pointcut)
                    .build();
            AnnotationSpec around = AnnotationSpec.builder(ClassName.get("org.aspectj.lang.annotation","Around"))
                    .addMember("value", "\"" + pointcutMethod.name + "()\"").build();
            MethodSpec aroundMethod = MethodSpec.methodBuilder("around" + classElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(ClassName.get("org.aspectj.lang","ProceedingJoinPoint"), "joinPoint")
                    .addAnnotation(around)
                    .addStatement("if (!(joinPoint.getSignature() instanceof $T)){\n\tthrow new $T(\"This annotation can only be used on Methods\");\n}"
                            ,ClassName.get("org.aspectj.lang.reflect","MethodSignature"),IllegalStateException.class)
                    .addStatement("MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();")
                    .addStatement(classElement.getSimpleName()+" annotation = methodSignature.getMethod().getAnnotation("+classElement.getSimpleName()+".class);")
                    .addStatement("$T.notifyHandler("+classElement.getSimpleName()+".class,joinPoint)",ClassName.get("com.thomas.okaspectj","OkAspectjHelper"))
                    .build();

            TypeSpec OkAspectj = TypeSpec.classBuilder(classElement.getSimpleName().toString() + "_Aspectj")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(pointcutMethod)
                    .addMethod(aroundMethod)
                    .addAnnotation(AnnotationSpec.builder(ClassName.get("org.aspectj.lang.annotation","Aspect")).build())
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
package com.piro84.hibernate.annotationprocessor.processors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.persistence.Entity;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//http://deors.wordpress.com/2011/10/31/annotation-generators/
@SupportedAnnotationTypes("javax.persistence.Entity")
public class JPAEntityProcessor extends AbstractProcessor {

	public static String OSGI_JPA_CONTRIBUTOR_CLASS_NAME="JPAContrib.java";
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		
		List<String> entityList = new ArrayList<String>();
		for (Element elem : roundEnv.getElementsAnnotatedWith(Entity.class)) {
	        String message = "annotation found in " + elem.getSimpleName();
	        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
	        
	        /*I am interested only in classes, since I am looking for entities*/
	        if (elem.getKind() == ElementKind.CLASS) {
                TypeElement classElement = (TypeElement) elem;
//                PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "found annotated class: " + classElement.getQualifiedName(), elem);

                String fqClassName = classElement.getQualifiedName().toString();
//                className = classElement.getSimpleName().toString();
//                packageName = packageElement.getQualifiedName().toString();
                entityList.add(fqClassName);
                
            }
	    }
		
		generateJavaFile(entityList);
		
		return false;
	}
	
	private void generateJavaFile(List<String> entityList){
		//generate the java file
		OSGIExtensionVelocityGenerator generator = new OSGIExtensionVelocityGenerator();
		JavaFileObject jfo;
		try {
			processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "generating JPAContrib.java file.." );
			jfo = processingEnv.getFiler().createSourceFile(
					OSGI_JPA_CONTRIBUTOR_CLASS_NAME);
			
			processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "JPAContrib.java file successfully generated." );
			
			generator.generateFileFromAnnotations(entityList, jfo);
		} catch (IOException e) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,"Unexpected exception while generating the file from the annotations");
		}
		
	}

}

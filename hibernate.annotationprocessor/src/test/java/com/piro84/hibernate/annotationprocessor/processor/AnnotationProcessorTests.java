/**
 * 
 */
package com.piro84.hibernate.annotationprocessor.processor;

import org.junit.Test;

import com.google.testing.compile.JavaFileObjects;
import com.piro84.hibernate.annotationprocessor.processors.JPAEntityProcessor;
import static org.truth0.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Testing the annotation processor with the help of https://github.com/google/compile-testing
 * @author pie
 *
 */
public class AnnotationProcessorTests {

	@Test
	public void generate() {
		  ASSERT.about(javaSource())
		  .that(JavaFileObjects.forResource("TestEntity.java"))
		  .processedWith(new JPAEntityProcessor())
		  .compilesWithoutError()
		  .and().generatesSources(JavaFileObjects.forResource(JPAEntityProcessor.OSGI_JPA_CONTRIBUTOR_CLASS_NAME));
	}

}

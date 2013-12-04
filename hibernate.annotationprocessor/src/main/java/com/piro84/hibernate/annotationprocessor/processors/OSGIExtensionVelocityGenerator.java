package com.piro84.hibernate.annotationprocessor.processors;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.tools.JavaFileObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class OSGIExtensionVelocityGenerator {

	private VelocityEngine engine = null;
	private static String VELOCITY_RESOURCE_FILE = "velocity.properties";
	private static String VELOCITY_TEMPLATE_NAME = "JPAEntityContributor.vm";

	private void initVelocityEngine() throws IOException {
		Properties props = new Properties();
		URL url = this.getClass().getClassLoader()
				.getResource(VELOCITY_RESOURCE_FILE);
		props.load(url.openStream());

		engine = new VelocityEngine(props);
		engine.init();
	}

	private VelocityContext initVelocityContext(List<String> entityList) {
		VelocityContext context = new VelocityContext();

		context.put("classNameList", entityList);
		// context.put("packageName", packageName);
		// context.put("fields", fields);
		// context.put("methods", methods);
		//
		return context;
	}

	private Template getVelocityTemplate(String templatePath) {
		Template template = engine.getTemplate(templatePath);
		return template;
	}

	public void generateFileFromAnnotations(List<String> entityList,
			JavaFileObject jfo) {

		Writer writer = null;
		
		if (engine == null) {
			try {
				initVelocityEngine();
				VelocityContext context = initVelocityContext(entityList);
				writer = jfo.openWriter();
				
				getVelocityTemplate(VELOCITY_TEMPLATE_NAME).merge(context, writer);
				
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
}

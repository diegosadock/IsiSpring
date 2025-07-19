package br.com.sadock.isispring.web;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.sadock.isispring.annotations.IsiGetMethod;
import br.com.sadock.isispring.annotations.IsiPostMethod;
import br.com.sadock.isispring.datastructures.ControllersMap;
import br.com.sadock.isispring.datastructures.RequestControllerData;
import br.com.sadock.isispring.datastructures.ServiceImplementationMap;
import br.com.sadock.isispring.explorer.ClassExplorer;
import br.com.sadock.isispring.util.IsiLogger;

public class IsiSpringWebApplication {
	
	public static final String GREEN = "\u001B[32m";
	public static final String BLUE = "\u001B[34m";
	public static final String WHITE = "\u001B[37m";
	public static final String RESET = "\u001B[0m";

	public static void run(Class<?> sourceClass) {

		IsiLogger.showBanner();
		long ini, fim;
		java.util.logging.Logger.getLogger("org.apache").setLevel(java.util.logging.Level.OFF);

		try {
			ini = System.currentTimeMillis();
			IsiLogger.log("Main Module", "Starting IsiSpringWebApplication");
			Tomcat tomcat = new Tomcat();
			Connector connector = new Connector();
			connector.setPort(8081);
			IsiLogger.log("Embeded Web Container", "Web Container started on port " + connector.getPort());

			extractMetaData(sourceClass);

			tomcat.setConnector(connector);

			Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
			Tomcat.addServlet(ctx, "IsiDispatchServlet", new IsiDispatchServlet());

			ctx.addServletMappingDecoded("/*", "IsiDispatchServlet");
			tomcat.start();

			fim = ini + System.currentTimeMillis();
			IsiLogger.log("Embeded Web Container",
					"IsiSpringWebApplication started in " + ((double) (fim - ini) / 1000) + " seconds");
			tomcat.getServer().await();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void extractMetaData(Class<?> sourceClass) throws Exception {

		List<String> allClasses = ClassExplorer.retrieveAllClasses(sourceClass);

		for (String isiClass : allClasses) {
			// recupero as anotações da classe
			Annotation[] annotations = Class.forName(isiClass).getAnnotations();
			for (Annotation classAnnotation : annotations) {
				if (classAnnotation.annotationType().getName()
						.equals("br.com.sadock.isispring.annotations.IsiController")) {
					IsiLogger.log("Metadata Explorer", "Found a Controller: " + isiClass);
					extractMethods(isiClass);
				}
				else if (classAnnotation.annotationType().getName()
						.equals("br.com.sadock.isispring.annotations.IsiService")) {
					IsiLogger.log("Metadata Explorer", "Found a Service Implementation: " + isiClass);
					for(Class<?> interf : Class.forName(isiClass).getInterfaces()) {
						IsiLogger.log("Metadata Explorer", "	 Class implements: " + interf.getName());
						ServiceImplementationMap.implementations.put(interf.getName(), isiClass);
					}
				}
			}
		}

	}

	private static void extractMethods(String className) throws Exception {
		// recupero todos os métodos da classe
		String httpMethod = "";
		String prefix = "";
		String path = "";
		for (Method method: Class.forName(className).getDeclaredMethods()) {
			// para cada método vou recuperar todas as suas anotações
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation.annotationType().getName().equals("br.com.sadock.isispring.annotations.IsiGetMethod")) {
					path = ((IsiGetMethod) annotation).value();
					//IsiLogger.log("", " + method " + method.getName() + " - URL PATH = " + BLUE + "🟢" + RESET + " [GET] " + path); 
					httpMethod = "GET";	
				}
				else if (annotation.annotationType().getName().equals("br.com.sadock.isispring.annotations.IsiPostMethod")) {
					path = ((IsiPostMethod) annotation).value();
					//IsiLogger.log("", " + method " + method.getName() + " - URL PATH = " + GREEN + "🔵" + RESET + " [POST] " + path);
					httpMethod = "POST";
				}
				RequestControllerData getData = new RequestControllerData(httpMethod, path, className, method.getName());
				ControllersMap.values.put(httpMethod+path, getData);
			}
		}
		
		for (RequestControllerData item : ControllersMap.values.values()) {
			prefix = (item.httpMethod.equals("GET") ? BLUE : GREEN) + "🟢 " + RESET;
			IsiLogger.log("", "     " + prefix + "" + item.httpMethod + " " + item.url + " [" + item.controllerClass + "." + item.controllerMethod + "]");
		}

	}

}

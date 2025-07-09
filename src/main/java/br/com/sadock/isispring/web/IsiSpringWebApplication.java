package br.com.sadock.isispring.web;

import java.io.File;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.sadock.isispring.explorer.ClassExplorer;
import br.com.sadock.isispring.util.IsiLogger;

public class IsiSpringWebApplication {
	
	public static void run(Class<?> sourceClass) {
		
		List<String> allClasses = ClassExplorer.retrieveAllClasses(sourceClass);
		
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
			
			for (String s : allClasses) {
				IsiLogger.log("Class Explorer", "Class Found: " + s);
			}
			
			tomcat.setConnector(connector);
			
			Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
			Tomcat.addServlet(ctx, "IsiDispatchServlet", new IsiDispatchServlet());
			
			ctx.addServletMappingDecoded("/*", "IsiDispatchServlet");
			tomcat.start();
			
			fim = ini + System.currentTimeMillis();
			IsiLogger.log("Embeded Web Container", "IsiSpringWebApplication started in " + ((double) (fim - ini) / 1000) + " seconds");
			tomcat.getServer().await();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

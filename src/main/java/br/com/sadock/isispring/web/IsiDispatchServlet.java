package br.com.sadock.isispring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import br.com.sadock.isispring.datastructures.ControllersInstances;
import br.com.sadock.isispring.datastructures.ControllersMap;
import br.com.sadock.isispring.datastructures.RequestControllerData;
import br.com.sadock.isispring.util.IsiLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IsiDispatchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// preciso ignorar o favicon
		if (request.getRequestURL().toString().endsWith("/favicon.ico")) {
			return;
		}

		String url = request.getRequestURI();
		String httpMethod = request.getMethod().toUpperCase();
		String key = httpMethod + url;
		RequestControllerData data = ControllersMap.values.get(key);
		IsiLogger.log("IsiDispatchServlet", "URL: " + url + " (" + httpMethod + ") - Handler "
				+ data.getControllerClass() + "." + data.getControllerMethod());

		Object controller;
		IsiLogger.log("IsiDispatchServlet", "Searching for Controller Instance...");
		try {
			// quero verificar se tenho uma instância da classe correspondente
			controller = ControllersInstances.instances.get(data.controllerClass);
			if (controller == null) {
				IsiLogger.log("IsiDispatchServlet", "Creating new Controller Instance...");
				controller  = Class.forName(data.controllerClass).getDeclaredConstructor().newInstance();
				ControllersInstances.instances.put(data.controllerClass, controller);
			}
			
			// preciso extrair o método desta classe, ou seja, aquele método que vai atender a requisição
			Method controllerMethod = null;
			
			for (Method  method : controller.getClass().getMethods()) {
				if (method.getName().equals(data.controllerMethod)) {
					controllerMethod = method;
					break;
				}
			}
			
			IsiLogger.log("IsiDispatchServlet", "Invoking Method " + controllerMethod.getName() + " to handle request");
			PrintWriter out = new PrintWriter(response.getWriter());
			out.println(controllerMethod.invoke(controller));
			out.close();
			// executar o método e (no fundo, escrever na saída a execução dele)
			
			
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}

		

	}

}

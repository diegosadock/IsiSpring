package br.com.sadock.isispring.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.google.gson.Gson;

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
		PrintWriter out = new PrintWriter(response.getWriter());
		Gson gson = new Gson();

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
				controller = Class.forName(data.controllerClass).getDeclaredConstructor().newInstance();
				ControllersInstances.instances.put(data.controllerClass, controller);
			}

			// preciso extrair o método desta classe, ou seja, aquele método que vai atender
			// a requisição
			Method controllerMethod = null;

			for (Method method : controller.getClass().getMethods()) {
				if (method.getName().equals(data.controllerMethod)) {
					controllerMethod = method;
					break;
				}
			}

			IsiLogger.log("IsiDispatchServlet", "Invoking Method " + controllerMethod.getName() + " to handle request");

			// meu método tem parâmetros?
			if (controllerMethod.getParameterCount() > 0) {
				IsiLogger.log("IsiDispatchServlet", "Method " + controllerMethod.getName() + " has parameters");
				Object arg;
				Parameter parameter = controllerMethod.getParameters()[0];

				if (parameter.getAnnotations()[0].annotationType().getName()
						.equals("br.com.sadock.isispring.annotations.IsiBody")) {
					// preciso ler os dados que vem da requisição
					String body = readBytesFromRequest(request);
					IsiLogger.log("", "     Found Parameter from request of type " + parameter.getType().getName());
					IsiLogger.log("", "     Parameter content " + body);
					arg = gson.fromJson(body, parameter.getType());

					out.println(gson.toJson(controllerMethod.invoke(controller, arg)));
				} 
				else {
					out.println(gson.toJson(controllerMethod.invoke(controller)));
				}
			}

			out.close();
			// executar o método e (no fundo, escrever na saída a execução dele)

		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private String readBytesFromRequest(HttpServletRequest request) throws Exception {
		StringBuilder str = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

		while ((line = br.readLine()) != null) {
			str.append(line);
		}
		return str.toString();
	}

}

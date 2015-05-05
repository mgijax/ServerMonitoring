package org.jax.mgi.servermonitoring.service;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.reader.ClassReaders;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
@WebServlet(name = "SwaggerJaxrsConfiguration", loadOnStartup = 1)
public class SwaggerJaxrsConfiguration extends HttpServlet {


	@Override
	public void init(ServletConfig servletConfig) {
		try {
			super.init(servletConfig);
			System.out.println("getServletInfo(): " + getServletInfo());
			System.out.println("getServletName(): " + getServletName());
			SwaggerConfig swaggerConfig = new SwaggerConfig();
			//swaggerConfig.setBasePath("http://localhost.jax.org/rest");
			swaggerConfig.setBasePath("http://mgi-testapp3.jax.org:8080/rest");
			swaggerConfig.setApiVersion("1.0.0");
			swaggerConfig.setApiInfo(apiInfo());
			ConfigFactory.setConfig(swaggerConfig);
			ScannerFactory.setScanner(new DefaultJaxrsScanner());
			ClassReaders.setReader(new DefaultJaxrsApiReader());
		} catch (ServletException e) {
			System.out.println(e.getMessage());
		}
	}

	private ApiInfo apiInfo() {
		String title = "Server Monitoring REST API";
		String description = "This API is for monitoring clients to log entries based on gathered information from each server that the client is running on.";
		String termsOfServiceUrl = "";
		String contact = "olin.blodgett@jax.org";
		String license = "Open";
		String licenseUrl = "";
		return new ApiInfo(title, description, termsOfServiceUrl, contact, license, licenseUrl);
	}
}

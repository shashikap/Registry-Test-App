package org.wso2.carbon.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Collection;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.Resource;

/**
 * Servlet implementation class RegistryTest
 */
@WebServlet("/RegistryTest")
public class RegistryTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public RegistryTest() {
		// TODO Auto-generated constructor stub
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			PrintWriter out = response.getWriter();
			System.out.print("URL = " + request.getRequestURL());
			System.out.print(" :: action = " + request.getParameter("action"));
			System.out.print(" :: path = " + request.getParameter("path"));
			System.out.println(" :: resource = "+ request.getParameter("resource"));

			String resourcePath = request.getParameter("path");
			String resourceValue = request.getParameter("resource");
			String action = request.getParameter("action");
			CarbonContext cCtx = CarbonContext.getThreadLocalCarbonContext();
			Registry registry = cCtx.getRegistry(RegistryType.SYSTEM_CONFIGURATION);

			if (resourcePath != null && action != null) {
				if (action.equalsIgnoreCase("add")) {
					if( resourceValue != null){
					Resource resource = registry.newResource();
					resource.setContent(resourceValue);
					registry.put(resourcePath, resource);
					out.println("Resource added successfully!!");
					out.println("Registry path :: " + resourcePath);
					out.println("Registry value :: " + resourceValue);
					}else{
						out.println("ERROR :: Resource Value Empty!!!");
					}
				} else if (action.equals("get")) {
					if (registry.resourceExists(resourcePath)) {
						Resource resource = registry.get(resourcePath);
						String content = new String((byte[]) resource.getContent());
						response.addHeader("resource-content", content);
						out.println("Resource Found in Registry returned!!!");
						out.println("Registry path :: " + resourcePath);
						out.println("Registry value :: " + content);
					} else {
						out.println("ERROR :: Resource Not Found in Registry!!!");
						out.println("Registry path :: " + resourcePath);
					}
				} else if (action.equalsIgnoreCase("delete")) {
					if (registry.resourceExists(resourcePath)) {
						Resource resource = registry.get(resourcePath);
						String content = new String((byte[]) resource.getContent());
						registry.delete(resourcePath);
						out.println("Resource Found and deleted!!!");
						out.println("Registry path :: " + resourcePath);
						out.println("Registry value :: " + content);
					} else {
						out.println("ERROR :: Resource Not Found in Registry!!!");
						out.println("Registry path :: " + resourcePath);
					}
				}else if (action.equalsIgnoreCase("addCollection")){
                    if( resourceValue != null){
                        Collection collection  = registry.newCollection();
                        registry.put(resourcePath, collection);
                        out.println("Resource Collection added successfully!!");
                        out.println("Registry path :: " + resourcePath);
                        out.println("Registry value :: " + resourceValue);

                        Resource collectionResource = registry.newResource();
                        collectionResource.setContent( resourceValue + "===== 1");
                        registry.put(resourcePath+"/col/1", collectionResource);
                        Resource collectionResource2 = registry.newResource();
                        collectionResource2.setContent(resourceValue + "===== 2");
                        registry.put(resourcePath+"/col/2", collectionResource2);

                    }else{
                        out.println("ERROR :: Resource Collection Value Empty!!!");
                    }
                }else if (action.equals("getCollection")) {
                    if (registry.resourceExists(resourcePath)) {
                        Resource resource = registry.get(resourcePath);
                        if(resource instanceof Collection){
                            out.println("=====This resource is a collection!!!=====");
                        }
                        if(registry.resourceExists(resourcePath+"/col/1")){
                            Resource resource1 = registry.get(resourcePath+"/col/1");
                            String content = new String((byte[]) resource1.getContent());
                            response.addHeader("resource-content", content);
                            out.println("Resource Found in Registry collection returned!!!");
                            out.println("Registry path :: " + resourcePath+"/col/1");
                            out.println("Registry value :: " + content);
                        }
                        if(registry.resourceExists(resourcePath+"/col/2")){
                            Resource resource1 = registry.get(resourcePath+"/col/2");
                            String content = new String((byte[]) resource1.getContent());
                            response.addHeader("resource-content", content);
                            out.println("Resource Found in Registry collection returned!!!");
                            out.println("Registry path :: " + resourcePath+"/col/2");
                            out.println("Registry value :: " + content);
                        }
                    } else {
                        out.println("ERROR :: Resource Not Found in Registry!!!");
                        out.println("Registry path :: " + resourcePath);
                    }
                } else if (action.equalsIgnoreCase("deleteCollection")) {
                    if (registry.resourceExists(resourcePath)) {
                        registry.delete(resourcePath);
                        if(!registry.resourceExists(resourcePath+"/col/1") && !registry.resourceExists(resourcePath+"/col/2")){
                            out.println("Resource Collection deleted successfully!!!");
                        }
                    } else {
                        out.println("ERROR :: Resource Not Found in Registry!!!");
                        out.println("Registry path :: " + resourcePath);
                    }
                }
			} else {
					out.println("ERROR :: Resource Error!!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}

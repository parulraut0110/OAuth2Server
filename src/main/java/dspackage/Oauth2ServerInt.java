package dspackage;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@WebFilter(servletNames = "RegstrEndPoint")
public class Oauth2ServerInt implements Filter {
    private MongoClient mongoClient = null;
    
    public Oauth2ServerInt() {
        super();
        
    }

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setAttribute("client", mongoClient);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
        mongoClient = MongoClients.create("mongodb://oauth2serverAdmin:Raut0110@oauth2server:27017/?authSource=clientdb&authMechanism=SCRAM-SHA-256");
            
            
	}
}



import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter("/LoginForm")
public class ResetJsessionIdFilter extends HttpFilter implements Filter {


	public ResetJsessionIdFilter() {
		super();
	}


	public void destroy() {
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Cast the ServletRequest to HttpServletRequest
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// Cast the ServletResponse to HttpServletResponse
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Continue the filter chain
		chain.doFilter(request, response);

		// Get the JSESSIONID cookie from the request
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equals(cookie.getName())) {
					// Modify the path attribute of the JSESSIONID cookie
					cookie.setPath("/");
					// Update the cookie in the response
					httpResponse.addCookie(cookie);
				}
			}
		}
	}


	public void init(FilterConfig fConfig) throws ServletException {
	}

}

package dspackage;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/WebKeySet")
public class WebKeySetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public WebKeySetServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		String jwks = "{\"keys\":[{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"b2e1d173-4ca2-468c-bd15-ed801a060ec9\",\"n\":\"vHiKbZyQu_iG3OeHiaM7cd3yLq98_uyWgAKUw6jM3Q0KsvdXmQacC-cZsR0z0JOFUK88SeH53TWiEGvv5amJoDt0UqUzGUG79d2T75-22mlkCnKLvdyfOwfEx0KOrtCrR7R-NxZxqBkiLd-ymW2Pz05lClOoGpzWLjW4EqRzzELkgqX7KZ00dzZfa24UklFVvCwhoNYlASOLUY2Agg7Qn-HxPEtRm3oC_kHashqVr2AEJ1BQZh1mdSDtBG673L_2p2YYr64pVhkG6KbyV97zhbQ6Pb-Wkq71ngKD8RulIs1DU4TGRt0UHhdzuiY2xBPruXXJujP7aITPRx1G6tHAJQ\"},{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"b3609202-4350-494d-bce9-2bb563d47207\",\"n\":\"81e78jZCLkAsfgv8K_Cms06ZjAqPflNNDiCUwc6Gfsu98hJTUJDusl5W-WFuJ59KgpvYMTPHBjwWtGlqpE1YpL3i6sAPHfcZI5xfQQNxkKa7nWEc7jSS8dbxWuyVdIcoHEUe1eYsQi9nHZhavxU1dt2vpKqv_ZPzZ6Yz7JLXKfUbGQtmRgnl3bYlxFiUnM9zzSE_HRvkGmrc_2PRYI71B8k8GpMhVKpE7yrskvHfmwcBKy__1FfnkX1tCWSLwUglZ1ToAsIx90sHGWRbzD206snSpTzR3tB35e-ZVnvSUaKIj7gwC6NI0DDW8mxVPgADRhtXl54cQRKP5JL7__pFbQ\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"a878351a-a503-42e3-bcb5-3a9d65f70b07\",\"x\":\"_h_8NJi3eGWVefVcA8B5FHA1iGd56sn8SheWsBjeFeo\",\"y\":\"NM_qoMR5xxiSejxnWOVFlpkvvX8e7gxLqq94DNuQeio\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"f535bd02-85bf-4c95-9541-79be85c980a2\",\"x\":\"tB7ZALQSFpGVboKmh0FTAbo5s_t9UR0jWSMa-7i4n3g\",\"y\":\"sVeaQVPjpfa-tdIrrXRmFKlNrKFwLkd7OqlBGI-fUBc\"}]}";
		writer.println(jwks);		
	}

}

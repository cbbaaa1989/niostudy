package pers.fengyitian.server2.http;

public class HttpRequest {
	
	private int bufSize = 1024;

	private byte[] buf;
	
	private Method method;
	
	private Url url;
	
	public HttpRequest(){
		
		buf = new byte[bufSize];
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}
	
	
	
	
}

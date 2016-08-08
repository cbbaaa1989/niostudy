package pers.fengyitian.server2.http;

public class Header {
	
	private HeaderName headerName;
	
	private HeaderValue headerValue;
	

	

	public HeaderName getHeaderName() {
		return headerName;
	}

	public HeaderValue getHeaderValue() {
		return headerValue;
	}

	public void setHeaderName(HeaderName headerName) {
		this.headerName = headerName;
	}

	public void setHeaderValue(HeaderValue headerValue) {
		this.headerValue = headerValue;
	}
	
	
	// inner class
	
	
	
	public static class HeaderName extends HttpSegment{

		public HeaderName(int start, int end) {
			super(start, end);
		}

	}
	
	public static class HeaderValue extends HttpSegment{

		public HeaderValue(int start, int end) {
			super(start, end);
		}

	}
}

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import com.net.util.HttpConnection;

public class baidu {

	public final String url = "https://tieba.baidu.com/p/6656185856";
	String uri2 = "https://tieba.baidu.com/f/commit/post/add";
	
	public void main() throws IOException {
		HttpConnection.Mandatory mandatory = new HttpConnection.Mandatory();
		mandatory.setApiUrl(url);
		mandatory.setContentType(MediaType.APPLICATION_JSON);
		mandatory.setMethod(HttpMethod.POST);
		HttpConnection http = new HttpConnection(mandatory, null);
		System.out.println(http.connect().getContent());
	}
}

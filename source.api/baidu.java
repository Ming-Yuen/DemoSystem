import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import com.net.util.HttpConnection;
import com.net.util.HttpConnection.HTTPConnMandatoryField;
import com.net.util.HttpConnection.HTTPConnSelectiveField;

public class baidu {

	public final String url = "https://tieba.baidu.com/p/6656185856";
	String uri2 = "https://tieba.baidu.com/f/commit/post/add";
	
	public void main() throws IOException {
		HTTPConnMandatoryField mandatoryField = new HTTPConnMandatoryField();
		mandatoryField.setApiUrl(url);
		mandatoryField.setConnContentType(MediaType.APPLICATION_JSON);
		mandatoryField.setMethod(HttpMethod.POST);
		HttpConnection http = new HttpConnection(mandatoryField, null);
		System.out.println(http.connect().exportAsString());
	}
}

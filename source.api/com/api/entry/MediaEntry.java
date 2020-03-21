package com.api.entry;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import com.api.model.VideoDownloadRequest;
import com.api.std.Entry;
import com.api.std.Response;
import com.api.util.MediaUtil;

import io.swagger.annotations.Api;

@Path("/Media")
@Api("/Media")
//@SwaggerDefinition(tags = {@Tag(name="user",description="user")})
public class MediaEntry extends Entry{

	@POST
	@Path("/videodownload")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response videoDownload(VideoDownloadRequest request) {
		Response res = new Response();
		try {
			MediaUtil.videoDownloadProcess(dbConn, request, res);
		}catch(Throwable e) {
			error(e);
		}finally {
			finish();
		}
		return res;
	}
}

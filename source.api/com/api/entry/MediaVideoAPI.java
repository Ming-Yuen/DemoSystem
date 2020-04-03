package com.api.entry;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import com.api.model.VideoDownload;
import com.api.std.API;
import com.api.std.ApiPath;
import com.api.std.Response;
import com.media.utils.MediaUtil;

import io.swagger.annotations.Api;

@Path(ApiPath.media)
@Api(ApiPath.media)
//@SwaggerDefinition(tags = {@Tag(name="user",description="user")})
public class MediaVideoAPI extends API {

	@POST
	@Path(ApiPath.videlDownload)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response videoDownload(VideoDownload request) {
		Response res = new Response();
		try {
			String savePath = request.getSaveURI();
			String downloadUrl = request.getDownloadURL();
			MediaUtil.videoDownloadProcess(downloadUrl, savePath);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish();
		}
		return res;
	}
}

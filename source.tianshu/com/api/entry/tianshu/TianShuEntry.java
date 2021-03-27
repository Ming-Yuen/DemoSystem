package com.api.entry.tianshu;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.api.entry.model.CreateMissionRequest;
import com.api.entry.model.CreateUserRequest;
import com.api.entry.model.DailyMissionResponse;
import com.api.model.VideoDownloadRequest;
import com.api.std.API;
import com.api.std.Response;
import com.api.tianshu.TianShuApiPath;
import com.api.tianshu.util.MissionUtil;
import com.api.tianshu.util.UserUtil;
import com.media.utils.MediaUtil;
import com.tianshu.database.DailyMissionRequest;

import io.swagger.annotations.Api;

@Path(TianShuApiPath.tianshu)
@Api(TianShuApiPath.tianshu)
public class TianShuEntry extends API {

	@POST
	@Path(TianShuApiPath.chubao)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response chubaoQuery(VideoDownloadRequest request) {//除暴
		Response resp = new Response();
		try {
			init("chubaoQuery");
			String savePath = request.getSaveURI();
			String downloadUrl = request.getDownloadURL();
			MediaUtil.videoDownloadProcess(downloadUrl, savePath);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish(resp);
		}
		return resp;
	}
	
	@POST
	@Path(TianShuApiPath.userQuery)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userQuery(VideoDownloadRequest request) {//除暴
		Response resp = new Response();
		try {
			init("chubaoQuery");
			String savePath = request.getSaveURI();
			String downloadUrl = request.getDownloadURL();
			MediaUtil.videoDownloadProcess(downloadUrl, savePath);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish(resp);
		}
		return resp;
	}

	@POST
	@Path(TianShuApiPath.createUser)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(CreateUserRequest request) {
		Response resp = new Response();
		try {
			init("chubaoQuery");
			UserUtil util = new UserUtil();
			util.createUser(this.dbConn, request);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish(resp);
		}
		return resp;
	}
	
	@POST
	@Path(TianShuApiPath.createMission)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMission(CreateMissionRequest request) {//除暴
		Response resp = new Response();
		try {
			init("chubaoQuery");
			MissionUtil util = new MissionUtil();
			util.createMission(this.dbConn, request);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish(resp);
		}
		return resp;
	}
	
	@POST
	@Path(TianShuApiPath.dailyMission)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public DailyMissionResponse dailyMission(DailyMissionRequest request) {
		DailyMissionResponse resp = new DailyMissionResponse();
		try {
			init("chubaoQuery");
			MissionUtil util = new MissionUtil();
			resp = util.dailyMission(this.dbConn, request);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish(resp);
		}
		return resp;
	}
}

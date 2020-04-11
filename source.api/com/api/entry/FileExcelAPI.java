package com.api.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.api.model.FileExcelRecord;
import com.api.std.API;
import com.api.std.ApiPath;
import com.api.std.Response;
import com.common.filereader.FileWriteExcelUtil;

import io.swagger.annotations.Api;

@Path(ApiPath.excel)
@Api(ApiPath.excel)
public class FileExcelAPI extends API {
	
	@POST
	@Path(ApiPath.xlsxwrite)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response xlsxExcelWrite(FileExcelRecord record) {
		Response resp = new Response();
		try {
			init("xlsxExcelWrite");
			List<FileExcelRecord> recList = new ArrayList<FileExcelRecord>();
			for(int index = 0; index < 100; index++) {
				FileExcelRecord rec = new FileExcelRecord();
				rec.itemCode = "Item" + index;
				rec.amount = index;
				rec.modifyDate = new Date();
				recList.add(rec);
			}
			new FileWriteExcelUtil().writeXlsxExcelRecord(recList, "C:\\Users\\Admin\\Documents\\test\\excel.xlsx", FileExcelRecord.class);
		} catch (Throwable e) {
			error(e);
		} finally {
			finish(resp);
		}
		return resp;
	}
}

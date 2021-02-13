package com.scheduler;

import java.util.Date;

import com.common.util.DateUtils;
import com.global.Global;

public class ThreadScheduler implements Runnable {

	private String scheduleId;
	private long scheduleFrequency;
	private long lastRuntime;

	public ThreadScheduler(String scheduleId, long scheduleFrequency) {
		this.scheduleId = scheduleId;
		this.scheduleFrequency = scheduleFrequency;
	}

	@Override
	public void run() {
		try {
			Date current = null;
			while (true) {
				current = new Date();
				if ((current.getTime() - lastRuntime) < scheduleFrequency) {
					Thread.sleep(1000);
					continue;
				}
				lastRuntime = DateUtils.cutTime(current, 3).getTime();
				Thread sc = new Thread(new Scheduler1());
				sc.start();
			}
		} catch (Throwable e) {
			Global.getLogger.error(this.getClass().getName(), e.getMessage(), e);
		}
	}
}

class Scheduler1 implements Runnable {

	@Override
	public void run() {
		Global.getLogger.info(this.getClass().getName(), DateUtils.convDateToString(new Date(), "HH:mm:ss.SSS"));
	}

}
package com.lody.virtual.client.hook.patchs.am;

import android.app.ActivityManager;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.VActivityManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lody
 *
 */
@SuppressWarnings("unchecked")
/* package */ class Hook_GetRunningAppProcesses extends Hook {

	@Override
	public String getName() {
		return "getRunningAppProcesses";
	}

	@Override
	public synchronized Object onHook(Object who, Method method, Object... args) throws Throwable {
		List<ActivityManager.RunningAppProcessInfo> infoList = (List<ActivityManager.RunningAppProcessInfo>) method
				.invoke(who, args);
		if (infoList != null) {
			for (ActivityManager.RunningAppProcessInfo info : infoList) {
				if (VActivityManager.getInstance().isAppPid(info.pid)) {
					List<String> pkgList = VActivityManager.getInstance().getProcessPkgList(info.pid);
					String processName = VActivityManager.getInstance().getAppProcessName(info.pid);
					if (processName != null) {
						info.processName = processName;
					}
					info.pkgList = pkgList.toArray(new String[pkgList.size()]);
					info.uid = VActivityManager.getInstance().getUidByPid(info.pid);
				}
			}
		}
		return infoList;
	}
}

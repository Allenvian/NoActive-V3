package cn.myflv.noactive.server;

import android.os.RemoteException;

import cn.myflv.noactive.INoActiveService;
import cn.myflv.noactive.core.util.XLog;

public class NoActiveService extends INoActiveService.Stub {

    @Override
    public void log(String msg) throws RemoteException {
        XLog.i(msg);
    }
}
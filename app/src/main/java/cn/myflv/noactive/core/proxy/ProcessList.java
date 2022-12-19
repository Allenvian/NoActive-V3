package cn.myflv.noactive.core.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.myflv.noactive.constant.FieldConstants;
import cn.myflv.noactive.core.entity.LockObj;
import cn.myflv.noactive.core.entity.ProcessRecord;
import de.robv.android.xposed.XposedHelpers;
import lombok.Data;

@Data
public class ProcessList {
    private final static LockObj<Object> processList = new LockObj<>();

    public static void setInstance(Object processList) {
        ProcessList.processList.setObj(processList);
    }

    public Map<String, List<ProcessRecord>> getProcessMap() {
        Map<String, List<ProcessRecord>> processMap = new HashMap<>();
        synchronized (processList.getObj()) {
            try {
                List<?> processRecordList = (List<?>) XposedHelpers.getObjectField(processList, FieldConstants.mLruProcesses);
                for (Object proc : processRecordList) {
                    ProcessRecord processRecord = new ProcessRecord(proc);
                    String packageName = processRecord.getPackageName();
                    List<ProcessRecord> list = processMap.computeIfAbsent(packageName, k -> new ArrayList<>());
                    list.add(processRecord);
                }
            } catch (Exception ignored) {

            }
        }
        return processMap;
    }

    public List<ProcessRecord> getProcessList(String packageName) {
        List<ProcessRecord> processRecords = getProcessMap().get(packageName);
        if (processRecords == null) {
            return new ArrayList<>();
        }
        return processRecords;
    }
}

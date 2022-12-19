package cn.myflv.noactive.core.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class S2Map<R, S, T> {

    private final Map<R, Map<S, T>> hashMap = new HashMap<>();

    private Map<S, T> getInnerMap(R r) {
        Map<S, T> innerMap = hashMap.get(r);
        if (innerMap != null) {
            return innerMap;
        }
        synchronized (hashMap) {
            return hashMap.computeIfAbsent(r, k -> new HashMap<>());
        }
    }

    public T get(R r, S s, Function<S, T> mappingFunction) {
        T t = getInnerMap(r).get(s);
        if (t != null) {
            return t;
        }
        synchronized (getInnerMap(r)) {
           return getInnerMap(r).computeIfAbsent(s, mappingFunction);
        }
    }

}

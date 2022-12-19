package cn.myflv.noactive.core.entity;

import lombok.Data;

@Data
public class LockObj<T> {
    private T obj;
}

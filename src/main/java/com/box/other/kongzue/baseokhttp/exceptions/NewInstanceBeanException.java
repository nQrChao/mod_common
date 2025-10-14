package com.box.other.kongzue.baseokhttp.exceptions;

public class NewInstanceBeanException extends Exception {
    public NewInstanceBeanException(String reason){
        super("实例化错误：无法创建 Bean 目标类：" + reason);
    }
}

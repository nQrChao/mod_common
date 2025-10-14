package com.box.other.kongzue.baseokhttp.listener;

import com.box.other.kongzue.baseokhttp.exceptions.DecodeJsonException;
import com.box.other.kongzue.baseokhttp.exceptions.NewInstanceBeanException;
import com.box.other.kongzue.baseokhttp.util.JsonBean;
import com.box.other.kongzue.baseokhttp.util.JsonMap;

import java.lang.reflect.ParameterizedType;

public abstract class BeanResponseListener<T> implements BaseResponseListener {
    
    @Override
    public void response(Object response, Exception error) {
        T tInstance = null;
        Class<T> tClass;
        try {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            tClass = (Class<T>) pt.getActualTypeArguments()[0];
            tInstance = tClass.newInstance();
        } catch (Exception e) {
            //这种情况下没办法实例化泛型对象
            onResponse(null, new NewInstanceBeanException("请检查该 Bean 是否为 public 且其构造方法为 public"));
            return;
        }
        if (error == null) {
            JsonMap data = new JsonMap(response.toString());
            if (data.isEmpty()) {
                onResponse(tInstance, new DecodeJsonException(response.toString()));
            }
            tInstance = JsonBean.getBean(data, tClass);
            
            onResponse(tInstance, null);
        } else {
            onResponse(tInstance, error);
        }
    }
    
    public abstract void onResponse(T main, Exception error);
}
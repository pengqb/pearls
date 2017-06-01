package com.vela.iot.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.google.common.collect.Maps;

public class MapObjectConverter {

    static {
//        ConvertUtils.register(new LongConverter(null), Long.class);
//        ConvertUtils.register(new ByteConverter(null), Byte.class);
//        ConvertUtils.register(new IntegerConverter(null), Integer.class);
//        ConvertUtils.register(new DoubleConverter(null), Double.class);
//        ConvertUtils.register(new ShortConverter(null), Short.class);
//        ConvertUtils.register(new MyDateConverter(null), Date.class);
//        ConvertUtils.register(new FloatConverter(null), Float.class);
    }

    public static Map<String, String> objectToHash(Object obj) {
        try {
            Map<String, String> map = Maps.newHashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (!property.getName().equals("class")) {
                    if (property.getReadMethod().invoke(obj) != null) {
                        map.put(property.getName(), "" + property.getReadMethod().invoke(obj));
                    }
                }
            }
            return map;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void hashToObject(Map<?, ?> map, Object obj) {
        try {
            BeanUtils.populate(obj, (Map) map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T hashToObject(Map<?, ?> map, Class c) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            Object obj = c.newInstance();
            hashToObject(map, obj);
            return (T) obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

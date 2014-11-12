/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Tools;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author ajun
 */
public class DynamicBean {
	/**
	 * 实体Object
	 */
	private Object object = null;
	/**
	 * 属性map
	 */
	private BeanMap beanMap = null;

	public DynamicBean() {
		super();
	}

	public DynamicBean(PropertyMap propertyMap) {
		this.object = generateBean(propertyMap);
		this.beanMap = BeanMap.create(this.object);
	}

	/**
	 * 给bean属性赋值
	 *
	 * @param property 属性名
	 * @param value    值
	 */
	public void setValue(String property, Object value, String type) {
		Object object = null;
		if (type.equals(java.sql.Timestamp.class.getName())
		    || type.equals(java.sql.Date.class.getName())
		    || type.equals(java.sql.Time.class.getName())) {

			type = String.class.getName();
		}
		try {
			if (value == null) {
				value = new String("NULL");
			}
			object = Class.forName(type).getConstructor(String.class).newInstance(value);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		beanMap.put(property, object);
	}

	/**
	 * 通过属性名得到属性值
	 *
	 * @param property 属性名
	 * @return 值
	 */
	public Object getValue(String property) {
		return beanMap.get(property);
	}

	/**
	 * 得到该实体bean对象
	 *
	 * @return
	 */
	public Object getObject() {
		return this.object;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object generateBean(Map propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		generator.addProperties(generator, propertyMap);
//		Set keySet = propertyMap.keySet();
//		for (Iterator i = keySet.iterator(); i.hasNext(); ) {
//			String key = (String) i.next();
//			generator.addProperty(key, (Class) propertyMap.get(key));
//		}
		return generator.create();
	}

}

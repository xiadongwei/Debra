/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Tools;

import java.util.HashMap;

/**
 * Created by xiadongwei on 14-2-13.
 */
public class PropertyMap extends HashMap<String, Object> {
	public PropertyMap() {
		super();
	}

	@Override
	public String put(String propertyName, Object propertyType) {
		if (Character.isUpperCase(propertyName.charAt(0))) {
			propertyName = new StringBuilder().append(Character.toLowerCase(propertyName.charAt(0))).append(propertyName.substring(1)).toString();
		}
		if (propertyType.equals(java.sql.Timestamp.class.getName())
		    || propertyType.equals(java.sql.Date.class.getName())
		    || propertyType.equals(java.sql.Time.class.getName())) {
			propertyType = String.class.getName();
		}
		try {
			super.put(propertyName, Class.forName((String) propertyType));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return propertyName;
	}

}

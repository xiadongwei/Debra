
/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Interface;

/**
 * @author user
 */
public abstract class DebraDao {
	private int columnCount;

	final public String getDaoName() {
		return this.getClass().getSimpleName();
	}

	final public int getColumnCount() {
		return columnCount;
	}

	final public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
}

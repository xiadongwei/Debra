/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework;

import debra.Framework.Interface.DebraWidget;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tony Dw. Xia
 *         Wieget对象堆栈管理
 */
public final class WidgetStack<t extends DebraWidget> {
	private final static int MAXSTACK = 64;
	private DebraWidget[] stack = new DebraWidget[MAXSTACK];
	public int current = -1;
	private static WidgetStack widgetStack;

	public static WidgetStack getInstance() {
		if (widgetStack == null) {
			widgetStack = new WidgetStack();
		}
		return widgetStack;
	}

	public void push(DebraWidget dWidget) {
		if (current < MAXSTACK) {
			stack[++current] = dWidget;
		} else {
			Logger.getLogger(WidgetStack.class.getName()).log(Level.OFF, "Widget stack over");
		}
	}

	public DebraWidget pop() {
		DebraWidget dw = null;
		if (current >= 0) {
			dw = stack[current];
			stack[current--] = null;
		}
		return dw;
	}

	public void clear() {
		while (current >= 0) {
			stack[current].release();
			stack[current--] = null;
		}
	}

	public String toString(){
		StringBuilder result = new StringBuilder("WidgetStack:\n");
		for (int i = current; i >= 0; i--) {
			result.append("offset = "+i+" value = "+stack[i]+"\n");
		}
		return result.toString();
	}
}

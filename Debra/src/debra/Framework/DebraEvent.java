/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework;

import debra.Debra;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

/**
 * @author user
 */
public class DebraEvent implements EventHandler<ActionEvent> {

	public static DebraEvent debraEvent;

	@Override
	public void handle(ActionEvent event) {

		if (event.getSource() instanceof MenuItem) {

			MenuItem mi = (MenuItem) event.getSource();

			if (mi.getId().equals(DebraProperty.DEBRA_CLOSE)) {
				FrameworkFactory.getInstance().release();
				Debra.primaryStage.close();
			}
			if (mi.getId().equals(DebraProperty.DEBRA_REFRESH)) {

				FrameworkFactory.debraWidget.refresh();
			}
			if (mi.getId().equals(DebraProperty.DEBRA_ABOUT)) {

				new AboutWindow().show();
			}
		} else if (event.getSource() instanceof TextField) {

			TextField tf = (TextField) event.getSource();

			if (tf.getId().equals(DebraProperty.DEBRA_ACTION_FIELD)) {

				if ((tf.getText() != null) && (!tf.getText().equals(""))) {

					FrameworkFactory.getInstance().actionProcess(tf.getText().trim());

					tf.setText("");
				}

			}
		} else if (event.getSource() instanceof Button) {
			Button tb = (Button) event.getSource();

			switch (tb.getId()) {

				case DebraProperty.DEBRA_ACTION_BUTTON:

					if ((Debra.actionField.getText() != null) && (!Debra.actionField.getText().equals(""))) {

						FrameworkFactory.getInstance().actionProcess(Debra.actionField.getText().trim());

						Debra.actionField.setText("");
					}
					break;
				case DebraProperty.DEBRA_BACK_BUTTON:

					FrameworkFactory.getInstance().actionBack();
					break;
			}
		}
	}

	public static EventHandler<ActionEvent> getInstance() {
		if (debraEvent == null) {

			debraEvent = new DebraEvent();
		}
		return debraEvent;
	}

}

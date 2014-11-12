/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Widgets.Calc;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class CalcController implements Initializable {
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Button b5;
    @FXML
    private Button b6;
    @FXML
    private Button b7;
    @FXML
    private Button b8;
    @FXML
    private Button b9;
    @FXML
    private Button b0;
    @FXML
    private Button equal;
    @FXML
    private Button addition;
    @FXML
    private Button division;
    @FXML
    private Button multiply;
    @FXML
    private Button subtraction;
    @FXML
    private Button backspace;
    @FXML
    private Button empty;
    @FXML
    private Button dot;
    @FXML
    private Button pushBuffer;
    @FXML
    private Button popBuffer;
    @FXML
    private Button plusMinus;
    @FXML
    private TextField result;
    @FXML
    private ListView<String> listBuffer;
    private String numStr = "";
    private Stack stack = new Stack();
    private boolean isFinish = true;
    private boolean canDot = false;

    @FXML
    protected void calcAction(ActionEvent event) {
        Button button = (Button) (event.getSource());
        char ch = button.getText().charAt(0);
        if (ch >= '0' && ch <= '9' || ch == '.') {
            this.digitPressed(ch);
        } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
            this.operandPressed(ch);
            isFinish = true;
            canDot = true;
        } else if (ch == '=') {
            this.enterPressed();
            isFinish = true;
            canDot = true;
        } else if (ch == 'C') {
            this.clearPressed();
            isFinish = true;
            canDot = true;
        } else if (ch == '←' && isFinish == false) {
            int length = result.getText().length();
            if (length > 1) {
                result.setText(result.getText().substring(0, length - 1));
            }
            if (length == 1) {
                result.setText("0");
                isFinish = true;
                canDot = true;
            }
        } else if (ch == '±') {
//			result.setText(BigDecimal.valueOf(Double.valueOf(result.getText())).multiply(BigDecimal.valueOf(-1)).toString());
        } else if (ch == '>') {
            ObservableList<String> data = listBuffer.getItems();
            data.add(result.getText());
            listBuffer.setItems(data);
            isFinish = true;
            canDot = true;
            this.clearPressed();
        } else if (ch == '<') {
            if (isFinish) {
                String selected = (String) listBuffer.getSelectionModel().selectedItemProperty().getValue();
                if (selected != null) {
                    result.setText((String) listBuffer.getSelectionModel().selectedItemProperty().getValue());
                }
            }
            listBuffer.getSelectionModel().clearSelection();
        }
    }

    @FXML
    protected void clearBuffer(ActionEvent event) {
        listBuffer.getItems().clear();
    }

    private void digitPressed(char ch) {
        if (isFinish) {
            if (ch != '.') {
                result.setText(String.valueOf(ch));
                isFinish = false;
                canDot = true;
            }
        } else {
            if (ch != '.') {
                if (!(result.getText().equals("0") && ch == '0')) {
                    if (result.getText().equals("0")) {
                        result.setText(String.valueOf(ch));
                    } else {
                        result.setText(result.getText() + (String.valueOf(ch)));
                    }
                }
            } else if (canDot) {
                result.setText(result.getText() + (String.valueOf(ch)));
                canDot = false;
            }
        }
    }

    private void operandPressed(char ch) {
        enterPressed();
        stack.push(result.getText());
        stack.push(String.valueOf(ch));

    }

    private void enterPressed() {
        String operand = stack.pop();
        if (operand == null) {
            return;
        }
        BigDecimal stackValue = null;
        BigDecimal displayValue = new BigDecimal(result.getText());
        BigDecimal res = null;
        switch (operand) {
            case "+":
                stackValue = new BigDecimal(stack.pop());
                res = stackValue.add(displayValue);
                break;
            case "-":
                stackValue = new BigDecimal(stack.pop());
                res = stackValue.subtract(displayValue);
                break;
            case "*":
                stackValue = new BigDecimal(stack.pop());
                res = stackValue.multiply(displayValue);
                break;
            case "/":
                stackValue = new BigDecimal(stack.pop());
//                     res = stackValue.divide(displayValue);
                res = stackValue.divide(displayValue, 16, RoundingMode.HALF_DOWN).stripTrailingZeros();
                break;
            default:
                return;
        }
        clearPressed();
        stack.push(res.toPlainString());
        result.setText(res.toPlainString());
    }

    private void clearPressed() {
        stack.clear();
        result.setText("0");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    class Stack {
        private String[] stack = new String[512];
        private int current = 0;

        public void push(String dc) {
            stack[current] = dc;
            current++;
            if (current > 511) {
                try {
                    throw new Exception();
                } catch (Exception ex) {
                    Logger.getLogger(CalcController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public String pop() {
            String result = null;
            if (current > 0) {
                current--;
                result = new String(stack[current]);
                stack[current] = null;
            } else if (current == 0) {
                if (stack[current] == null) {
                    result = null;
                } else {
                    result = new String(stack[current]);
                }
                stack[current] = null;
            }
            return result;
        }

        public void clear() {
            for (int i = 0; i < 512; i++) {
                stack[i] = null;
            }
            current = 0;
        }

        public void println() {
            for (int i = 0; stack[i] != null; i++) {
                System.out.println(stack[i]);
            }
        }
    }
}

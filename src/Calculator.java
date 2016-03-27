//
// Name:        Chuong, Michelle
// Project:     #2
// Due:         October 25, 2015
// Course:      cs-245-01-f15
// 
// Description:
//      Simple integer calculator.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * @author mchuong
 */
public class Calculator implements ActionListener {
    private JLabel calculatorDisplay;
    private boolean error;
    private boolean flag;
    private int operand;
    private int total;
    private char operator;
    
    public Calculator() {
        error = false;
        flag = true;
        operand = 0;
        total = 0;
        operator = '\0';
        
        JFrame jframe = new JFrame("Calculator");
        jframe.getContentPane().setLayout(new GridLayout(2, 1));
        jframe.setSize(300, 500);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            URL url = new URL("http://www.cpp.edu/~tvnguyen7/courses/cs245f15/projs/Calculator.png");
            Image img = ImageIO.read(url);
            jframe.setIconImage(img);
        } catch (Exception e) { }

        calculatorDisplay = new JLabel("0", JLabel.RIGHT);
        jframe.add(calculatorDisplay);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4));
        
        //creates number buttons
        JButton[] numButtons = new JButton[10];
        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
            numButtons[i].addActionListener(this);
        }
        
        JButton plusButton = new JButton("+");
        plusButton.addActionListener(this);
        
        JButton minusButton = new JButton("-");
        minusButton.addActionListener(this);
        
        JButton multiplyButton = new JButton("*");
        multiplyButton.addActionListener(this);
        
        JButton divideButton = new JButton("/");
        divideButton.addActionListener(this);
        
        JButton equalsButton = new JButton("=");
        equalsButton.addActionListener(this);
        
        JButton clearButton = new JButton("<HTML><U>C</U></HTML>");
        clearButton.addActionListener(this);
        clearButton.setActionCommand("C");
        
        //4th or top row of buttons: 7 8 9 /
        buttonPanel.add(numButtons[7]);
        buttonPanel.add(numButtons[8]);
        buttonPanel.add(numButtons[9]);
        buttonPanel.add(divideButton);
        
        //3rd row of buttons: 4 5 6 *
        buttonPanel.add(numButtons[4]);
        buttonPanel.add(numButtons[5]);
        buttonPanel.add(numButtons[6]);
        buttonPanel.add(multiplyButton);
        
        //2nd row of buttons: 1 2 3 -
        buttonPanel.add(numButtons[1]);
        buttonPanel.add(numButtons[2]);
        buttonPanel.add(numButtons[3]);
        buttonPanel.add(minusButton);
        
        //1st or bottom row of buttons: 0 [C] = +
        buttonPanel.add(numButtons[0]);
        buttonPanel.add(clearButton);
        buttonPanel.add(equalsButton);
        buttonPanel.add(plusButton);

        //set default button to equals
        jframe.getRootPane().setDefaultButton(equalsButton);
        jframe.add(buttonPanel);
        jframe.setVisible(true);
    }
    
    /*
     * Action method.
     */
    public void actionPerformed(ActionEvent ae) {
        try {
            numberAction(Integer.parseInt(ae.getActionCommand()));
        } catch (NumberFormatException e) {
            operatorAction(ae.getActionCommand(), ae);
        }
    }
    
    /*
     * Handles the actions with numbers.
     */
    public void numberAction(int n) {
        if (error == true || isFull() == true) {
            return;
        } 
        if (n == 0 && calculatorDisplay.getText().equals("0")) {
            return;
        }
        if (flag) {
            calculatorDisplay.setText("" + n);
            flag = false;
        } else if (calculatorDisplay.getText().equals("0")) {
            calculatorDisplay.setText("" + n);
        } else {
            calculatorDisplay.setText(calculatorDisplay.getText() + n);
        }
    }
    
    /*
     * Handles the actions performed with each operator.
     */
    public void operatorAction(String op, ActionEvent ae) {        
        switch (op.charAt(0)) {
            case '\0':
                break;
            case 'C' :
                clearAction(op, ae);
                break;
            case '+':
                if (error) {
                    break;
                }
                flag = true;
                operand += Integer.parseInt(calculatorDisplay.getText());
                operator = '+';
                break;
            case '-':
                if (error) {
                    break;
                }                
                if (operator == '\0') {
                    operand = Integer.parseInt(calculatorDisplay.getText());
                } else {
                    operand -= Integer.parseInt(calculatorDisplay.getText());
                }
                flag = true;
                operator = '-';
                break;
            case '*':
                if (error) {
                    break;
                }
                if (operator == '\0') {
                    operand = Integer.parseInt(calculatorDisplay.getText());
                } else {
                    operand *= Integer.parseInt(calculatorDisplay.getText());
                }
                flag = true;
                operator = '*';
                break;
            case '/':
                if (error) {
                    break;
                }                
                if (operator == '\0') {
                    operand = Integer.parseInt(calculatorDisplay.getText());
                } else {
                    try {
                        operand /= Integer.parseInt(calculatorDisplay.getText());
                    } catch (ArithmeticException e) {
                        //Div by 0 error already handled
                    }
                }
                flag = true;
                operator = '/';
                break;
            case '=':
                if (error) {
                    break;
                }
                equalsAction(op, ae);
                break;
        }
    }
    
    /*
     * Show copyright info or clears the display.
     */
    public void clearAction(String op, ActionEvent ae) {
        //check for CTRL and [C] to display copyright
        int ctrlC = InputEvent.BUTTON1_MASK | InputEvent.CTRL_MASK;
        if ((ae.getModifiers() & ctrlC) == ctrlC) {
            if (error) {
                return;
            }
            calculatorDisplay.setText("(c) 2015 Michelle Chuong");
        } else {
            //clears the display
            error = false;
            flag = true;
            operand = 0;
            total = 0;
            operator = '\0';
            calculatorDisplay.setText("0");
        }
    }
    
    /*
     * Show appropriate error message or performs calculated result and
     * prints to the display.
     */
    public void equalsAction(String op, ActionEvent ae) {              
        operatorAction("" + operator, ae);
        //Divide by Zero Error
        if (calculatorDisplay.getText().equals("0")) {
            error = true;
            calculatorDisplay.setText("ERROR: Div by 0");
        } else {
            total = operand;
            operator = '\0';
            
            String answer = "" + total;
            //Overflow Error
            if (answer.length() > 9) {
                error = true;
                calculatorDisplay.setText("ERROR: Overflow");
                return;
            }
            
            //Performs equals action
            calculatorDisplay.setText("" + total);
            flag = true;
            operand = 0;
            total = 0;
        }
    }
    
    /*
     * Only allow up to 10 digits.
     */
    public boolean isFull() {
        return (calculatorDisplay.getText().length() >= 9);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Calculator();
            }
        });
    }
}

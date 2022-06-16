package com.mygdx;

import javax.swing.JOptionPane;

public class Test {
    public static void main(String[] args) {
        String[] choices = new String[]{"cats", "dogs"};
        String choice = askUser(choices);
        System.out.println("selected: " + choice);
    }

    static String askUser(String[] choices) {
        String s = (String) JOptionPane.showInputDialog(
                null,
                "make your choice",
                "Try GUI",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                choices[0]);
        return s;
    }
}

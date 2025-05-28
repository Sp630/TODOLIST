import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Main {
    public static void main(String[] args) {

        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Hellow");

        //swing integration
        JFrame frame = new JFrame("clicker game");

        JButton one = new JButton("");
        JButton two = new JButton("");
        JButton three = new JButton("");
        JButton one2 = new JButton("");
        JButton two2 = new JButton("");
        JButton three2 = new JButton("");
        JButton one3 = new JButton("");
        JButton two3 = new JButton("");
        JButton three3 = new JButton("");


        frame.setLayout(new GridLayout(3,1));
        frame.setSize(300,300);
        frame.add(one);
        frame.add(two);
        frame.add(three);
        frame.add(one2);
        frame.add(two2);
        frame.add(three2);
        frame.add(one3);
        frame.add(two3);
        frame.add(three3);

        frame.setVisible(true);
    }
}
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        Screen screen = new Screen(tasks);
        screen.RestoreInfo();
        ArrayList<JButton> buttons = screen.Draw();
        ReDoCanvas(buttons, tasks, screen);



    }
    static void ReDoCanvas(ArrayList<JButton> buttons, ArrayList<Task> tasks, Screen screen){
        for (JButton button : buttons) {
            if(!button.getText().equals("Add")) {
                button.addActionListener(new ActionListener() {
                    Task taskToRemove = null;
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        for (Task task : tasks) {
                            if(task.name.equals(button.getText())) {
                                //tasks.remove(task);
                                taskToRemove = task;
                            }
                        }
                        button.getParent().remove(button);
                        tasks.remove(taskToRemove);
                        screen.UpdateTasks(tasks);
                        ArrayList<JButton> buttons = screen.Draw();
                        ReDoCanvas(buttons, tasks, screen);
                    }
                });
            }
            if (button.getText().equals("Add")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {

                        tasks.add(new Task(screen.textField.getText()));
                        screen.UpdateTasks(tasks);
                        ArrayList<JButton> buttons = screen.Draw();
                        button.setText(screen.textField.getText());
                        ReDoCanvas(buttons, tasks, screen);
                    }
                });
            }
            if(button.getText().equals("Update")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {

                        for(int i = 0; i < tasks.size(); i++) {
                            if(i == tasks.size() - 1) {
                                tasks.get(i).name = screen.textField.getText();
                            }
                        }

                        screen.UpdateTasks(tasks);
                        ArrayList<JButton> buttons = screen.Draw();
                        ReDoCanvas(buttons, tasks, screen);
                    }
                });
            }
        }
    }
}
class Screen{
    ArrayList<Task> tasks;
    JTextField textField = new JTextField();
    JFrame frame = new JFrame("clicker game");


    public Screen(ArrayList<Task> tasks){
        this.tasks = tasks;

    }
    void UpdateTasks(ArrayList<Task> tasks){
        this.tasks = tasks;

    }
    public ArrayList<JButton> Draw(){

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.setSize(300,300);

        ArrayList<JButton> buttons = new ArrayList<>();
        for(int i = 0; i<tasks.size(); i++){
            JButton button = new JButton(tasks.get(i).name);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            Dimension size = new Dimension(200, 50);
            button.setMaximumSize(size);
            button.setPreferredSize(size);
            button.setMinimumSize(size);
            panel.add(button);
            buttons.add(button);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());


        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");

        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        inputPanel.add(updateButton, BorderLayout.WEST);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        buttons.add(addButton);
        buttons.add(updateButton);
        SaveInfo();
        return buttons;
    }
    void SaveInfo(){
        try {
            FileWriter writer = new FileWriter("SavedTasks", false);
            writer.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }


        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SavedTasks"))){
            for(int i = 0; i<tasks.size(); i++){
                oos.writeObject(tasks.get(i));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    void RestoreInfo(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SavedTasks"))){
            while (true){
                try {
                    Task task = (Task) ois.readObject();
                    tasks.add(task);
                }
                catch (EOFException e){
                    break;
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
class Task implements Serializable {
    String name;
    public Task(String name){
        this.name = name;
    }

}
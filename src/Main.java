import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        Screen screen = new Screen(tasks);
        screen.restoreInfo();
        ArrayList<JButton> buttons = screen.draw();
        redoCanvas(buttons, tasks, screen);
    }

    static void redoCanvas(ArrayList<JButton> buttons, ArrayList<Task> tasks, Screen screen) {
        for (JButton button : buttons) {
            if (!button.getText().equals("Add")) {
                button.addActionListener(new ActionListener() {
                    Task taskToRemove = null;

                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        
                        for (Task task : tasks) {
                            if (task.getName().equals(button.getText())) {
                                taskToRemove = task;
                                break;
                            }
                        }
                        button.getParent().remove(button);
                        if (taskToRemove != null) {
                            tasks.remove(taskToRemove);
                        }
                        screen.updateTasks(tasks);
                        ArrayList<JButton> buttons = screen.draw();
                        redoCanvas(buttons, tasks, screen);
                        screen.getSoundPlayer().playClickSound();
                    }
                });
            }
            if (button.getText().equals("Add")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String inputText = screen.getTextField().getText();
                        if (!inputText.isEmpty()) {
                            tasks.add(new Task(inputText));
                            screen.updateTasks(tasks);
                            ArrayList<JButton> buttons = screen.draw();
                            // Update the button text for the newly added task
                            button.setText(inputText);
                            redoCanvas(buttons, tasks, screen);
                        }
                    }
                });
            }
            if (button.getText().equals("Update")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String inputText = screen.getTextField().getText();
                        if (!inputText.isEmpty() && !tasks.isEmpty()) {
                            tasks.get(tasks.size() - 1).setName(inputText);
                        }
                        screen.updateTasks(tasks);
                        ArrayList<JButton> buttons = screen.draw();
                        redoCanvas(buttons, tasks, screen);
                    }
                });
            }
        }
    }
}

class Screen {
    private ArrayList<Task> tasks;
    private final JTextField textField = new JTextField();
    private final JFrame frame = new JFrame("Clicker Game");
    private SoundPlayer soundPlayer = new SoundPlayer("pickupCoin.wav");

    public Screen(ArrayList<Task> tasks) {
        
        this.tasks = tasks;
    }

    public SoundPlayer getSoundPlayer(){
        return soundPlayer;
    }
    public void updateTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public JTextField getTextField() {
        return textField;
    }

    public ArrayList<JButton> draw() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.setSize(300, 300);

        ArrayList<JButton> buttons = new ArrayList<>();
        for (Task task : tasks) {
            JButton button = new JButton(task.getName());
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

        JPanel inputPanel = new JPanel(new BorderLayout());

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");

        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        inputPanel.add(updateButton, BorderLayout.WEST);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        buttons.add(addButton);
        buttons.add(updateButton);

        saveInfo();

        return buttons;
    }

    private void saveInfo() {
        // Clear file first
        try (FileWriter writer = new FileWriter("SavedTasks", false)) {
            // Clear contents
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SavedTasks"))) {
            for (Task task : tasks) {
                oos.writeObject(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreInfo() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SavedTasks"))) {
            while (true) {
                try {
                    Task task = (Task) ois.readObject();
                    tasks.add(task);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Task implements Serializable {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class SoundPlayer{
    String path;
    public SoundPlayer(String path){
        this.path = path;
    }

    void playClickSound(String path) {
        try {
            File soundFile = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    void playClickSound() {
        try {
            File soundFile = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}

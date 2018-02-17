/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    @FXML
    private Button task1Button, task2Button, task3Button;
    
    @FXML
    private Label task1Label, task2Label, task3Label;
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        if (task1 == null) {
            task1 = new Task1(2147483647, 1000000, task1Label);
            task1.setNotificationTarget(this);
            task1.start();
            
            task1Label.setText(task1.getNotificationState().toString());
            task1Button.setText("End Task 1");
        } else if(task1.isAlive()) { // Check if the thread is alive... If so, end it
            task1.end();
            
            task1Label.setText(task1.getNotificationState().name());
            
            task1 = null;
            
            textArea.appendText("Task 1 has been shutdown\n");
            task1Button.setText("Start Task 1");
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 done.")) {            
            task1Label.setText(task1.getNotificationState().name());
            
            task1 = null;
            
            task1Button.setText("Start Task 1");
        }
        textArea.appendText(message + "\n");
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        if (task2 == null) {
            task2 = new Task2(2147483647, 1000000, task2Label);
            task2.setOnNotification((String message) -> {
                textArea.appendText(message + "\n");
                
                if(message.equals("Task2 done.")) {
                    task2Label.setText(NotificationStates.ENDED.name());
                    task2Button.setText("Start Task 2");
                    task2 = null;
                }
            });
                        
            task2.start();
            
            task2Label.setText(task2.getNotificationState().name());
            task2Button.setText("End Task 2");
        } else if(task2.isAlive()) { // Check if the thread is alive... If so, end it
            task2.end();
            
            task2Label.setText(task2.getNotificationState().name());
            
            task2 = null;
            
            textArea.appendText("Task 2 has been shutdown\n");
            task2Button.setText("Start Task 1");
        }
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        if (task3 == null) {
            task3 = new Task3(2147483647, 1000000, task3Label);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                textArea.appendText((String)evt.getNewValue() + "\n");
                
                if(((String)evt.getNewValue()).equals("Task3 done.")) {
                    task3Label.setText(NotificationStates.STOPPED.name());
                    task3Button.setText("Start Task 3");
                    task3 = null;
                }
            });
            
            task3.start();
            task3Label.setText(task3.getNotificationState().name());
            task3Button.setText("End Task 3");
        } else if(task3.isAlive()) { // Check if the thread is alive... If so, end it
            task3.end();
            
            task3Label.setText(task3.getNotificationState().name());
            
            task3 = null;
            
            textArea.appendText("Task 3 has been shutdown\n");
            task3Button.setText("Start Task 3");
        }
    } 
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Label;
import notifcationexamples.NotificationStates;

/**
 *
 * @author dalemusser
 * 
 * This example uses a Notification functional interface.
 * This allows the use of anonymous inner classes or
 * lambda expressions to define the method that gets called
 * when a notification is to be made.
 */
public class Task2 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private ArrayList<Notification> notifications = new ArrayList<>();
    private NotificationStates state;
    private Label taskLabel;
    
    public Task2(int maxValue, int notifyEvery, Label taskLabel)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.taskLabel = taskLabel;
        this.state = NotificationStates.STOPPED;
    }
    
    @Override
    public void run() {
        doNotify("Started Task2!");
        state = NotificationStates.RUNNING;
        Platform.runLater(() -> {
            taskLabel.setText(this.state.name());
        });
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task2: " + i);
            }
            
            if (exit) {
                return;
            }
        }
        state = NotificationStates.ENDED;
        doNotify("Task2 done.");
    }
    
    public void end() {
        state = NotificationStates.STOPPED;
        exit = true;
    }
    
    // this method allows a notification handler to be registered to receive notifications
    public void setOnNotification(Notification notification) {
        this.notifications.add(notification);
    }
    
    private void doNotify(String message) {
        // this provides the notification through the registered notification handler
        for (Notification notification : notifications) {
            Platform.runLater(() -> {
                notification.handle(message);
                taskLabel.setText(this.state.name());
            });
        }
    }

    public NotificationStates getNotificationState() {
        return state;
    }

    public void setState(NotificationStates state) {
        this.state = state;
    }
}
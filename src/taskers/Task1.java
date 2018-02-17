/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import notifcationexamples.NotificationStates;

/**
 *
 * @author dalemusser
 * 
 * This example uses an object passed in with a notify()
 * method that gets called when a notification is to occur.
 * To accomplish this the Notifiable interface is needed.
 * 
 */
public class Task1 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private Notifiable notificationTarget;
    private NotificationStates state;
    
    private Label taskLabel;
    
    public Task1(int maxValue, int notifyEvery, Label taskLabel)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.taskLabel = taskLabel;
        this.state = NotificationStates.STOPPED;
    }
    
    @Override
    public void run() {
        doNotify("Task1 start.");
        setState(NotificationStates.RUNNING);
        Platform.runLater(() -> {
            taskLabel.setText(this.state.name());
        });
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task1: " + i);
            }
            
            if (exit) {
                return;
            }
        }
        state = NotificationStates.ENDED;
        doNotify("Task1 done.");
    }
    
    public void end() {
        state = NotificationStates.STOPPED;
        exit = true;
    }
    
    public void setNotificationTarget(Notifiable notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
    
    private void doNotify(String message) {
        // this provides the notification through a method on a passed in object (notificationTarget)
        if (notificationTarget != null) {
            Platform.runLater(() -> {
                notificationTarget.notify(message);
                taskLabel.setText(this.state.name());
            });
        }
    }

    public NotificationStates getNotificationState() {
        return this.state;
    }

    public void setState(NotificationStates state) {
        this.state = state;
    }
}

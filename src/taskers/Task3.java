/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.scene.control.Label;
import notifcationexamples.NotificationStates;

/**
 *
 * @author dalemusser
 * 
 * This example uses PropertyChangeSupport to implement
 * property change listeners.
 * 
 */
public class Task3 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private NotificationStates state;
    private Label taskLabel;
    
    public Task3(int maxValue, int notifyEvery, Label taskLabel)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.taskLabel = taskLabel;
        this.state = NotificationStates.STOPPED;
    }
    
    @Override
    public void run() {
        doNotify("Task3 start.");
        state = NotificationStates.RUNNING;
        Platform.runLater(() -> {
            taskLabel.setText(this.state.name());
        });
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task3: " + i);
            }
            
            if (exit) {
                return;
            }
        }
        state = NotificationStates.ENDED;
        doNotify("Task3 done.");
    }
    
    public void end() {
        state = NotificationStates.STOPPED;
        exit = true;
    }
    
    // the following two methods allow property change listeners to be added
    // and removed
    public void addPropertyChangeListener(PropertyChangeListener listener) {
         pcs.addPropertyChangeListener(listener);
     }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
         pcs.removePropertyChangeListener(listener);
     }
    
    private void doNotify(String message) {
        // this provides the notification through the property change listener
        Platform.runLater(() -> {
            // I'm choosing not to send the old value (second param).  Sending "" instead.
            pcs.firePropertyChange("message", "", message);
            taskLabel.setText(this.state.name());
        });
    }

    public NotificationStates getNotificationState() {
        return state;
    }

    public void setState(NotificationStates states) {
        this.state = states;
    }
}
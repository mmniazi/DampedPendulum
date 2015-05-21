package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.transform.Rotate;

public class Controller {

    @FXML
    private TextField theeta0TextField;

    @FXML
    private TextField lTextField;

    @FXML
    private TextField kTextField;

    @FXML
    private TextField gTextField;

    @FXML
    private TextField v0TextField;

    @FXML
    private TextField mTextField;

    @FXML
    private TextField AngVelTextField;

    @FXML
    private TextField theetaTextField;

    @FXML
    private Group pendulum;

    Thread simulation;
    boolean running;

    @FXML
    private void onStart(ActionEvent actionEvent) {
        double h = 0.005;
        double m = Double.parseDouble(mTextField.getText());
        double g = Double.parseDouble(gTextField.getText());
        double k = Double.parseDouble(kTextField.getText());
        double l = Double.parseDouble(lTextField.getText());
        double theeta0 = Double.parseDouble(theeta0TextField.getText());
        double v0 = Double.parseDouble(v0TextField.getText());

        pendulum.getTransforms().add(new Rotate(theeta0, 0, 0));

        if (k == 0) {
            k = 0.05;
        }
        Euler solver = new Euler(h, g, l, k, m);

        simulation = new Thread(() -> {
            running = true;
            DataPoint oldData = new DataPoint(theeta0, v0);
            while (running) {
                DataPoint newData = solver.solve(oldData);
                Platform.runLater(new Animation(oldData.theeta, newData.theeta, newData.v));
                oldData = newData;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        simulation.start();
    }


    @FXML
    private void onStop(ActionEvent actionEvent) {
        running = false;
        Platform.runLater(() -> {
            pendulum.getTransforms().clear();
            theetaTextField.setText("");
            AngVelTextField.setText("");
        });
    }

    private class Animation implements Runnable {
        double newTheeta;
        double oldTheeta;
        double v;

        public Animation(double oldTheeta, double newTheeta, double v) {
            this.oldTheeta = oldTheeta;
            this.newTheeta = newTheeta;
            this.v = v;
        }

        @Override
        public void run() {
            pendulum.getTransforms().add(new Rotate(newTheeta - oldTheeta, 0, 0));
            theetaTextField.setText(String.valueOf(newTheeta));
            AngVelTextField.setText(String.valueOf(v));
        }
    }
}

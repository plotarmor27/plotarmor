import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
/**
 * The ResetPassword class represents a controller for resetting a user's password.
 * It provides methods to open a reset password view, handle close button clicks,
 * and initiate a password reset request.
 *
 * Features include:
 * - Opening a reset password view.
 * - Handling close button clicks to close the view.
 * - Initiating a password reset request (placeholder for future functionality).
 *
 * Usage:
 * - Create an instance of the ResetPassword class.
 * - Call the `openResetPasswordView` method to display the reset password view.
 * - Implement functionality for handling close button clicks and password reset requests.
 */
public class ResetPassword {


    Stage resetPasswordView = new Stage();
    TitleBarController titleBarController = new TitleBarController();


    public void openResetPasswordView() throws IOException {

        resetPasswordView.setTitle("PlotArmor - Registration");
        Parent resetPassword = FXMLLoader.load(getClass().getResource("/resetPasswordView/resetPasswordView.fxml"));
        resetPasswordView.getIcons().add(new Image("login/rustung.png"));
        Scene scene = new Scene(resetPassword);
        resetPasswordView.initStyle(StageStyle.UNDECORATED);
        resetPasswordView.setResizable(false);
        resetPasswordView.setScene(scene);
        resetPasswordView.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
                guiWindowManager.setResetPasswordOpen(false);
                System.out.println("Window was closed without pressing x button, for example closing the process in process manager");
            }
        });
        titleBarController.controllTitleBar(resetPassword,resetPasswordView);
        resetPasswordView.show();
    }


}

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;

/**
 * The Register class handles user registration in the PlotArmor application.
 * It allows users to provide their email, username, and password for registration,
 * with additional verification steps such as age confirmation and email verification.
 *
 * Features include:
 * - Collecting user input for email, username, password, and repeated password.
 * - Validating user credentials, ensuring proper email format, non-empty fields, and matching passwords.
 * - Initiating an email verification process for the registered user.
 * - Displaying error messages for invalid inputs or database connection issues.
 * - Redirecting users back to the login view.
 *
 * The class interacts with the database to insert user information upon successful registration.
 */
public class Register {
    private static Register instance;
    public static Register getInstance() {
        if (instance == null) {
            instance = new Register();
        }
        return instance;
    }
    Stage register;
    Stage registerStage = new Stage();
    public Stage getRegisterStage() {
        return register;
    }


    TitleBarController titleBarController = new TitleBarController();
    public void openRegister() throws IOException {


        FXMLLoader loader =  new FXMLLoader((getClass().getResource("./register/register.fxml")));
        Parent register =  loader.load();
        registerStage.getIcons().add(new Image("login/rustung.png"));
        Image background = new Image("./register/register-background.png");
        RegisterController ctrl = loader.getController();
         Scene scene = new Scene(register);
        registerStage.initStyle(StageStyle.UNDECORATED);
        registerStage.setResizable(false);

        registerStage.setScene(scene);
        titleBarController.controllTitleBar(register,registerStage);
        registerStage.show();

        Register reg = Register.getInstance();
        reg.register = this.registerStage;

        ctrl.setBackgroundImage(background);
    }



}


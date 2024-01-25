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


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register/register.fxml"));
        Parent register =  loader.load();
        registerStage.getIcons().add(new Image("login/rustung.png"));
        Image background = new Image(getClass().getResourceAsStream("/register/register-background.png"));
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


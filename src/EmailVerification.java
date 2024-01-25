
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import java.util.Random;
import java.io.IOException;
public class EmailVerification {

    // Use a static method to get the instance
    private static EmailVerification instance;
    public boolean verificationIsSuccessful = false;

    public static EmailVerification getInstance() {
        if (instance == null) {
            instance = new EmailVerification();
        }
        return instance;
    }

    RegisterController registerController;
    ResetPasswordController resetPasswordController;
    Stage emailStage = new Stage();
    TitleBarController titleBarController = new TitleBarController();

    public void openEmailVerification() throws IOException {


        Parent emailView = FXMLLoader.load(getClass().getResource("/emailVerification/emailVerification.fxml"));
        emailStage.getIcons().add(new Image("login/rustung.png"));
        emailStage.initStyle(StageStyle.UNDECORATED);

        Scene scene = new Scene(emailView);
        emailStage.setResizable(false);
        emailStage.setScene(scene);

        // Set up event handler for window close event
        emailStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
                guiWindowManager.setEmailVerificationOpen(false);
                System.out.println("Window was closed without pressing x button, for example closing the process in process manager");
            }
        });
        titleBarController.controllTitleBar(emailView,emailStage);
        emailStage.show();
        // Asynchronously generate a verification code and send it via email
        Task<String> emailTask = new Task<>() {
            @Override
            protected String call() {
                String code = generateCode();
                getInstance().getRegisterController().code = code;
                SendingEmail email = new SendingEmail(getInstance().getRegisterController().email);
                System.out.println(getInstance().getRegisterController().email);
                email.sendMail(code);
                return code;
            }
        };

        Thread emailThread = new Thread(emailTask);
        emailThread.setDaemon(true);
        emailThread.start();

    }

    public void openEmailVerificationResetPassword() throws IOException {



        Parent emailView = FXMLLoader.load(getClass().getResource("/emailVerification/emailVerification.fxml"));
        emailStage.getIcons().add(new Image("login/rustung.png"));
        emailStage.initStyle(StageStyle.UNDECORATED);

        Scene scene = new Scene(emailView);
        emailStage.setResizable(false);
        emailStage.setScene(scene);

        emailStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
                guiWindowManager.setEmailVerificationOpen(false);
                System.out.println("Window was closed without pressing x button, for example closing the process in process manager");
            }
        });
        titleBarController.controllTitleBar(emailView,emailStage);
        emailStage.show();
            // Asynchronously generate a verification code and send it via email
        Task<String> emailTask = new Task<>() {
            @Override
            protected String call() {
                String code = generateCode();
                getInstance().getResetPasswordController().code = code;
                SendingEmail email = new SendingEmail(getInstance().getResetPasswordController().email);
                System.out.println(getInstance().getResetPasswordController().email);
                email.sendMail(code);
                return code;
            }
        };

        Thread emailThread = new Thread(emailTask);
        emailThread.setDaemon(true);
        emailThread.start();
    }

    public void setRegisterController(RegisterController r){
        this.registerController = r;
    }
    public void setResetPasswordController(ResetPasswordController r){
        this.resetPasswordController = r;
    }
    public RegisterController getRegisterController(){
        return this.registerController;
    }

    public ResetPasswordController getResetPasswordController(){
        return this.resetPasswordController;
    }

    //function to generate a random five digit code
    public String generateCode(){
        int number;
        Random random = new Random();
        number = random.nextInt(99999);
        String code = String.valueOf(number);
        for (int i = 5; i > code.length(); i--){
            code = "0" + code;
        }
        return code;
    }
}

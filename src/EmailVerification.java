
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
/**
 * The EmailVerification class manages the email verification process in the PlotArmor application.
 * It provides a window for users to enter the verification code received via email,
 * enabling the completion of the registration process.
 *
 * Features include:
 * - Displaying the email verification window for users.
 * - Allowing users to enter the verification code.
 * - Communicating with the Register class to complete the registration upon successful verification.
 *
 * The class follows the Singleton design pattern to ensure only one instance exists during the application lifecycle.
 */
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
    Stage emailStage = new Stage();
    TitleBarController titleBarController = new TitleBarController();
    public void openEmailVerification() throws IOException {
        String code = generateCode();
        getInstance().getRegisterController().code = code;
        SendingEmail email = new SendingEmail(getInstance().getRegisterController().email);
        System.out.println(getInstance().getRegisterController().email);
        email.sendMail(code);
        emailStage.setTitle("PlotArmor - Verification");
        Parent emailView = FXMLLoader.load(getClass().getResource("./emailVerification/emailVerification.fxml"));
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
        //if(verificationIsSuccessful == true) emailStage.close();
    }
    RegisterController registerController;

    public void setRegisterController(RegisterController r){
        this.registerController = r;
    }

    public RegisterController getRegisterController(){
        return this.registerController;
    }

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
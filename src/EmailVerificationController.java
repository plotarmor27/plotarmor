import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class EmailVerificationController {
    public Button btnClose;
    public TextField txtCodeNr1;
    public TextField txtCodeNr2;
    public TextField txtCodeNr3;
    public TextField txtCodeNr4;
    public TextField txtCodeNr5;
    public Button btnVerify;
    public Text lblWrongCode;
    public Stage registerStage;
    public void setRegisterStage(Stage registerStage) {
        this.registerStage = registerStage;
    }



    // Reference to the EmailVerification instance, a singleton class
    EmailVerification emailV = EmailVerification.getInstance();

    // Setter method to update the RegisterController in the EmailVerification instance
    public void setRegisterController(RegisterController r){
        emailV.setRegisterController(r);
    }

    // Update the boolean variable in the register class
    public void setResetPasswordController(ResetPasswordController resetPassword){
        emailV.setResetPasswordController(resetPassword);
    }
    @FXML
    public void closeOnAction(ActionEvent e) {
        emailV = EmailVerification.getInstance();
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        guiWindowManager.setEmailVerificationOpen(false);
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }
    public void openEmailVerification() throws IOException {
        EmailVerification email = new EmailVerification();
        email.openEmailVerification();
    }
    public void openEmailVerificationResetPassword() throws IOException {
        EmailVerification email = new EmailVerification();
        email.openEmailVerificationResetPassword();
    }
    //button handler for the button in the email verification view
    public void verifyAccountCode(ActionEvent actionEvent) throws IOException, SQLException {
        emailV = EmailVerification.getInstance();

        String codeInput = txtCodeNr1.getText()+txtCodeNr2.getText()+txtCodeNr3.getText()+txtCodeNr4.getText()+txtCodeNr5.getText();

        if(emailV.getRegisterController() == null){
            verifyResetPasswordCode(codeInput);
        }
        else{
            verifyAccountRegisterCode(codeInput);
        }

    }
    //function for the verifying of the register process
    public void verifyAccountRegisterCode(String codeInput) throws IOException {
        emailV.getRegisterController().setCodeInput(codeInput);
        if(emailV.getRegisterController().insertUserToDB()){
            emailV.verificationIsSuccessful = true;
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
            GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
            guiWindowManager.setEmailVerificationOpen(false);

            //after successfully created account
            //close register view and email verification view and
            //open login view
            emailV.getRegisterController().close();
            Main main = new Main();
            main.openLoginView();
        }
        else{
            //send new code again to email
            lblWrongCode.setVisible(true);
            lblWrongCode.setText("Wrong Code, new Code is sent!");
            String code = emailV.generateCode();
            emailV.getRegisterController().code = code;
            SendingEmail email = new SendingEmail(emailV.getRegisterController().email);
            System.out.println(emailV.getRegisterController().email);
            email.sendMail(code);

        }
    }
    //function for the verifying of the resetpassword process
    public void verifyResetPasswordCode(String codeInput) throws SQLException {
        emailV.getResetPasswordController().setCodeInput(codeInput);
        boolean successfullySendPassword = emailV.getResetPasswordController().sendPasswordTokenToUser();
        if(successfullySendPassword){
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
            GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
            guiWindowManager.setEmailVerificationOpen(false);
        }
        else{
            //send new code again to email
            lblWrongCode.setVisible(true);
            lblWrongCode.setText("Wrong Code, new Code is sent!");
            String code = emailV.generateCode();
            emailV.getResetPasswordController().code = code;
            SendingEmail email = new SendingEmail(emailV.getResetPasswordController().email);
            System.out.println(emailV.getResetPasswordController().email);
            email.sendMail(code);
        }
    }
}

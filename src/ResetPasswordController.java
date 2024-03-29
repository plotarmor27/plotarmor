import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
//Controller class for the reset password view.
public class ResetPasswordController {
    public TextField txtFEmail;
    public Button btnSendRequest;
    public Button btnClose;
    public Label lblResetPasswordERROR;
    GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
    // Verification code and email variables
    String code = "";
    String email = "";
    Connection connection = DatabaseConnection.connect();
    DatabaseQueryUser query = new DatabaseQueryUser();
    String codeInput ="";
    //Closes the reset password view.
    public void CloseOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        guiWindowManager.setResetPasswordOpen(false);
        stage.close();
    }
    //Opens the reset password view.
    public void openResetPasswordView() throws IOException {
        ResetPassword resetP = new ResetPassword();
        resetP.openResetPasswordView();

    }
    //Sends a password reset request based on the entered email.
    public void sendRequestOnClick(ActionEvent actionEvent) throws SQLException, IOException {
        email = txtFEmail.getText();
        //check if the entered email is in the db | if not catch this and send an error to the user on gui

        lblResetPasswordERROR.setVisible(true);
        if(connection == null){
            lblResetPasswordERROR.setText("Error connecting to the database!");
        }

        else{

            boolean enteredEmailIsinDb = query.emailIsInDb(email);

                if(enteredEmailIsinDb)
                {
                    lblResetPasswordERROR.setTextFill(Color.GREEN);
                    lblResetPasswordERROR.setText("Successfully sent Verification Link");

                    Platform.runLater(() -> {
                        EmailVerificationController emailController = new EmailVerificationController();
                        emailController.setResetPasswordController(ResetPasswordController.this);
                        guiWindowManager.setEmailVerificationOpen(true);
                        try {
                            emailController.openEmailVerificationResetPassword();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });



                }
                else
                {
                    lblResetPasswordERROR.setText("Entered Email is not registered");
                }
        }



    }
    //Sends a new password to the user after successful verification.
    public boolean sendPasswordTokenToUser() throws SQLException {
        Connection connection = DatabaseConnection.connect();
        if(code.equals(codeInput)){
            SendingEmail sendingE = new SendingEmail(email);
            String password = generateRandomPassword();
            String hashValue = generateHashValue(password);
            query.updateNewPassword(hashValue,email);
            sendingE.sendMailForPwReset(password);
            //after sending new password unlock users account
            setUnlockedStatus(connection,email);
            lblResetPasswordERROR.setTextFill(Color.GREEN);
            lblResetPasswordERROR.setText("Sucessfully sent new Password");
            return true;
        } else {
            System.out.println("Der eingegebene Code ist falsch! Ein neuer Code wird generiert.");
            return false;
        }

    }
    public void setUnlockedStatus(Connection connection, String email){
        query.setLockedStatusToZero(connection,email);
    }
    //function to generate random password
    public String generateRandomPassword(){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        int passwordLength = 15;
        String allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            password.append(randomChar);
        }
        return password.toString();
    }

    public String generateHashValue(String pw){
        return PasswordHashing.hashPassword(pw);
    }
    public void setCodeInput(String codeInput){
        System.out.println(codeInput);
        this.codeInput = codeInput;
    }
}

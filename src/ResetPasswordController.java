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

public class ResetPasswordController {
    public TextField txtFEmail;
    public Button btnSendRequest;
    public Button btnClose;
    public Label lblResetPasswordERROR;

    String code = "";
    String email = "";

    DatabaseQuery query = new DatabaseQuery();
    String codeInput ="";

    public void CloseOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        guiWindowManager.setResetPasswordOpen(false);
        stage.close();
    }

    public void openResetPasswordView() throws IOException {
        ResetPassword resetP = new ResetPassword();
        resetP.openResetPasswordView();
    }

    public void sendRequestOnClick(ActionEvent actionEvent) throws SQLException, IOException {
        email = txtFEmail.getText();
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        //check if the entered email is in the db | if not catch this and send an error to the user on gui
        Connection connection = DatabaseConnection.connect();
        lblResetPasswordERROR.setVisible(true);
        if(connection == null){
            lblResetPasswordERROR.setText("Error connecting to the database!");
        }
        else{
            boolean enteredEmailIsinDb = query.emailIsInDb(email);

                if(enteredEmailIsinDb){
                    EmailVerificationController emailController = new EmailVerificationController();
                    emailController.setResetPasswordController(ResetPasswordController.this);
                    guiWindowManager.setEmailVerificationOpen(true);
                    emailController.openEmailVerificationResetPassword();
                    lblResetPasswordERROR.setTextFill(Color.GREEN);
                    lblResetPasswordERROR.setText("Successfully sent Verification Link");


                }
                else{
                    lblResetPasswordERROR.setText("Entered Email is not registered");
                }
        }



    }
    public boolean sendPasswordTokenToUser() throws SQLException {
        Connection connection = DatabaseConnection.connect();
        if(code.equals(codeInput)){
            SendingEmail sendingE = new SendingEmail(email);
            String password = generateRandomPassword();
            query.updateNewPassword(password,email);
            sendingE.sendMailForPwReset(password);
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

    public void setCodeInput(String codeInput){
        System.out.println(codeInput);
        this.codeInput = codeInput;
    }
}

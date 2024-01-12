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

    DatabaseQuery query = new DatabaseQuery();
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

    public void sendRequestOnClick(ActionEvent actionEvent) throws SQLException {
        String email = txtFEmail.getText();
        //check if the entered email is in the db | if not catch this and send an error to the user on gui
        Connection connection = DatabaseConnection.connect();
        lblResetPasswordERROR.setVisible(true);
        if(connection == null){

            lblResetPasswordERROR.setText("Error connecting to the database!");
        }
        else{
            boolean enteredEmailIsinDb = query.emailIsInDb(email);
                if(enteredEmailIsinDb){
                    SendingEmail sendingE = new SendingEmail(email);
                    String password = generateRandomPassword();
                    query.updateNewPassword(password,email);
                    sendingE.sendMailForPwReset(password);
                    lblResetPasswordERROR.setTextFill(Color.GREEN);
                    lblResetPasswordERROR.setText("Sucessfully sent new Password");
                }
                else{
                    lblResetPasswordERROR.setText("Entered Email is not in the database");
                }
        }



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
}

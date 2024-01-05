import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EmailVerificationController {
    public Button btnClose;
    public TextField txtCodeNr1;
    public TextField txtCodeNr2;
    public TextField txtCodeNr3;
    public TextField txtCodeNr4;
    public TextField txtCodeNr5;
    public Button btnVerify;

    // Reference to the EmailVerification instance, a singleton class
    EmailVerification emailV = EmailVerification.getInstance();

    // Setter method to update the RegisterController in the EmailVerification instance
    public void setRegisterController(RegisterController r){
        emailV.setRegisterController(r);
    }

    // Update the boolean variable in the register class
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
        email.openEmailVarification();
    }
    public void verifyAccountCode(ActionEvent actionEvent) {
        emailV = EmailVerification.getInstance();
        String codeNumbers = txtCodeNr1.getText()+txtCodeNr2.getText()+txtCodeNr3.getText()+txtCodeNr4.getText()+txtCodeNr5.getText();
        emailV.getRegisterController().setCodeNumbers(codeNumbers);
        emailV.getRegisterController().insertUserToDB();
    }
}

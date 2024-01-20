import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterController {
    String codeInput ="";
    String code = "12345";
    String email = "";
    String username = "";
    String password = "";
    String repeatPassword = "";
    Connection connection;
    public TextField txtFUsername;
    public TextField txtFEmail;
    public PasswordField txtFPassword;
    public PasswordField txtFRepeatPwd;
    public Button btnClose;
    public Label lblError;
    public Button btnBackToLogin;
    public CheckBox radioB16YearsOld;
    public AnchorPane registerPane;
    @FXML
    private Button btnRegister;

    DatabaseQueryUser dataQuery = new DatabaseQueryUser();
    EmailVerificationController emailController;
    public void backtoLoginOnClick(ActionEvent actionEvent) throws IOException {
        GUIWindowManager windowManager = GUIWindowManager.getInstance();
        if(!windowManager.isEmailVerificationOpen()){
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
            Main m1 = new Main();
            m1.openLoginView();
        }
    }
    @FXML
    public void closeOnAction(ActionEvent e){
        GUIWindowManager windowManager = GUIWindowManager.getInstance();
        if(!windowManager.isEmailVerificationOpen()){
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        }

    }
    @FXML
    public void registerOnAction(ActionEvent e) throws IOException, SQLException {
        emailController = new EmailVerificationController();
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        boolean emailVerificationIsActive = guiWindowManager.isEmailVerificationOpen();
        email = txtFEmail.getText();
        username = txtFUsername.getText();
        password = txtFPassword.getText();
        repeatPassword = txtFRepeatPwd.getText();
        connection = DatabaseConnection.connect();
        lblError.setText("");


        if(connection == null){
            lblError.setVisible(true);
            lblError.setText("Error connecting to the database");
        }


        if(!emailVerificationIsActive && connection != null){
            //check if email is already registered
            boolean emailIsAlreadyRegistered = dataQuery.emailIsInDb(email);
            if(emailIsAlreadyRegistered){
                lblError.setVisible(true);
                lblError.setText("Email is already registered!");
            }
            else{
                if(credentialsAreValid(email,username,password,repeatPassword) && radioB16YearsOld.isSelected()){
                    emailController.setRegisterController(RegisterController.this);
                    guiWindowManager.setEmailVerificationOpen(true);
                    emailController.openEmailVerification();
                }
                else
                {
                    System.out.println(radioB16YearsOld.isSelected());
                    lblError.setVisible(true);
                    lblError.setText("Error: Please make valid inputs and try again!");
                    guiWindowManager.setEmailVerificationOpen(false);
                }
            }

        }
    }
    public boolean credentialsAreValid(String email, String username, String password, String repeatPassword) {
        // Check if email is not empty
        if (email.isEmpty() || !isValidEmail(email)) {
            return false;
        }

        // Check if username is not empty
        if (username.isEmpty()) {
            return false;
        }

        // Check if password is not empty
        if (password.isEmpty()) {
            return false;
        }

        // Check if repeatPassword is not empty and matches the original password
        if (repeatPassword.isEmpty() || !repeatPassword.equals(password)) {
            return false;
        }

        // Return true if all conditions are met
        return true;
    }
    // Helper method to validate email address with simple checks for "@" and "."
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    public void setCodeInput(String codeInput){
        System.out.println(codeInput);
        this.codeInput = codeInput;
    }

    public boolean insertUserToDB(){
        if(code.equals(codeInput)){
            dataQuery.createNewUser(connection,email,username,password);
            System.out.println("Erfolgreich in die Datenbank eingetragen!");
            return true;
        } else {
            System.out.println("Der eingegebene Code ist falsch! Ein neuer Code wird generiert.");
            return false;
        }
    }

    public void setBackgroundImage(Image image){
        this.registerPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
    }

    public void openRegister() throws IOException {
        Register reg = new Register();
        reg.openRegister();
    }

    public void radioBClicked(ActionEvent actionEvent) {
    }

    public void close() {
        Register reg = Register.getInstance();
        reg.getRegisterStage().close();
    }
}

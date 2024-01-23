import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterController {
    public Label lblAvailableNotAvailable, lblError, lblPasswordMinChars;
    String codeInput ="";
    String code = "12345";
    String email,username,password,repeatPassword = "";
    Connection connection = DatabaseConnection.connect();
    public TextField txtFUsername,txtFEmail;
    public PasswordField txtFPassword,txtFRepeatPwd;

    public Button btnClose, btnBackToLogin;

    public CheckBox radioB16YearsOld;
    public AnchorPane registerPane;
    boolean userIsInUse = false;
    boolean isPwLengthCorrect = false;

    DatabaseQueryUser dataQuery = new DatabaseQueryUser();
    EmailVerificationController emailController;

    public void initialize() {
        txtFUsername.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost, check if username is in use by checking if the username has an entry in the database

                if(txtFUsername.getText().isEmpty()){
                    return;
                }
                try {
                    if(dataQuery.userNameIsInUse(connection,txtFUsername.getText())){
                        lblAvailableNotAvailable.setVisible(true);
                        lblAvailableNotAvailable.setTextFill(Color.RED);
                        lblAvailableNotAvailable.setText("Not Available");
                        userIsInUse = true;
                    }
                    else{
                        lblAvailableNotAvailable.setVisible(true);
                        lblAvailableNotAvailable.setTextFill(Color.LIGHTGREEN);
                        userIsInUse = false;
                        lblAvailableNotAvailable.setText("Available");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        txtFPassword.focusedProperty().addListener((ov, oldV, newV) -> {
            if(!newV){
                if(txtFPassword.getText().isEmpty()){
                    return;
                }
                if(txtFPassword.getText().length() < 10){
                    lblPasswordMinChars.setVisible(true);
                    lblPasswordMinChars.setTextFill(Color.RED);
                    isPwLengthCorrect = false;
                } else if(txtFPassword.getText().length() > 72){
                    lblPasswordMinChars.setVisible(true);
                    lblPasswordMinChars.setTextFill(Color.RED);
                    lblPasswordMinChars.setText("Max. 72 Characters!");
                    isPwLengthCorrect = false;
                } else {
                    lblPasswordMinChars.setVisible(false);
                    isPwLengthCorrect = true;
                }
            }
        });
    }

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
        lblError.setVisible(true);

        if(connection == null){
            lblError.setText("Error connecting to the database");
        }

        if(!emailVerificationIsActive && connection != null){
            //check if email is already registered
            boolean emailIsAlreadyRegistered = dataQuery.emailIsInDb(email);
            if(emailIsAlreadyRegistered){
                lblError.setText("Email is already registered!");
            }
            else{
                if(credentialsAreValid(email,username,password,repeatPassword) && radioB16YearsOld.isSelected() && !userIsInUse && isPwLengthCorrect){
                    emailController.setRegisterController(RegisterController.this);
                    guiWindowManager.setEmailVerificationOpen(true);
                    emailController.openEmailVerification();
                }
                else
                {
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
        if (!AccountSettings.passwordIsValid(password, repeatPassword)){
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
        this.codeInput = codeInput;
    }

    public boolean insertUserToDB(){
        if(code.equals(codeInput)){
            dataQuery.createNewUser(connection,email,username,PasswordHashing.hashPassword(password));
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

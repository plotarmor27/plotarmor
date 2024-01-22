import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.ResourceBundle;
/**
 * The AccountSettings class represents a controller for managing user account settings.
 * It implements the Initializable interface to initialize the controller during FXML loading.
 * The class provides methods to open the account settings overview, change username, email,
 * and password, delete the account, and view user information.
 *
 * Features include:
 * - Opening the account settings overview.
 * - Changing username, email, and password.
 * - Deleting the user account.
 * - Viewing user information.
 * - Handling button hover events.
 *
 * Usage:
 * - Create an instance of the AccountSettings class.
 * - Call the respective methods to perform actions such as opening the account settings overview
 *   or changing the username.
 * - Implement functionality for handling user interactions (e.g., button clicks, hover events).
 */
public class AccountSettings implements Initializable {
    UserInformation userInfo = UserInformation.getInstance();
    public Label lblMeanScore,lblLastLogin,lblregisterDate,
            lblErrorSuccesfullMessage,lblHelloUser,lblSettings,lblSetting,
            lblRepeatSetting,lblDelText,lblDelEmail,lblProfileEmail,
            lblProfileUsername,lblProfileMovieRated;
    public Button btnClose,btnChangeUsername,btnChangeEmail,
            btnChangePassword,btnDelAccount,btnVerifyNewProperty,
            btnDeleteAccount,btnViewAccountInformation;
    public TextField txtFChangeProperty,txtRepeatNewProperty,txtFAccountDelEmail;
    Stage accountSettingOverview = new Stage();
    TitleBarController titleBarController = new TitleBarController();
    Connection dbConnection;
    DatabaseQueryUser queryUser = new DatabaseQueryUser();

    public void openAccountSettingOverview() throws IOException {
        accountSettingOverview.setTitle("PlotArmor - MainPage");
        Parent accountSetting = FXMLLoader.load(getClass().getResource("/myAccount/accountOptionOverview.fxml"));
        accountSettingOverview.getIcons().add(new Image("login/rustung.png"));
        Scene scene = new Scene(accountSetting);
        accountSettingOverview.initStyle(StageStyle.UNDECORATED);
        accountSettingOverview.setResizable(false);
        titleBarController.controllTitleBar(accountSetting,accountSettingOverview);
        accountSettingOverview.setScene(scene);
        accountSettingOverview.show();
    }
    // Method to open the form for changing the username
    public void openUserNameFormular(ActionEvent actionEvent) {

        setLabelsVisible(true,lblSettings,lblSetting,lblRepeatSetting);
        setLabelsVisible(false,lblDelText,lblDelEmail,lblProfileEmail,lblProfileUsername,lblProfileMovieRated,lblErrorSuccesfullMessage,lblLastLogin,lblMeanScore,lblregisterDate);
        setTextFieldsVisible(true,txtFChangeProperty,txtRepeatNewProperty);
        setTextFieldsVisible(false,txtFAccountDelEmail);
        setButtonsVisible(true,btnVerifyNewProperty);
        setButtonsVisible(false,btnDeleteAccount);
        lblSettings.setText("Settings: Change Username");
        lblSetting.setText("Username: ");
        lblRepeatSetting.setText("Repeat Username: ");

        txtFChangeProperty.setText("");
        txtRepeatNewProperty.setText("");

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLabelsVisible(false,lblProfileEmail,lblProfileUsername,lblProfileMovieRated,lblLastLogin,lblMeanScore,lblregisterDate);
        lblHelloUser.setText("Hello: " + userInfo.getUsername());

        Connection connection = DatabaseConnection.connect();
        //get users movie ratings
        HashMap<String, Integer> movieRatings = null;
        try {
            movieRatings = queryUser.getMovieRatingsForUser(connection,userInfo.getID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //save the amount of movies rated in userinformation clas
        userInfo.setMoviesRated(movieRatings.keySet().size());
        //get the movie rating sum
        int sum = 0;
        for(int ratedSum : movieRatings.values()){
            sum+= ratedSum;
        }
        //save the movie rating sum to user information
        userInfo.setRatedSum(sum);

    }
    // Helper method to set the visibility of labels
    private void setLabelsVisible(boolean isVisible, Label... lbl){
        for (Label element : lbl) {
            element.setVisible(isVisible);
        }
    }
    // Helper method to set the visibility of text fields
    private void setTextFieldsVisible(boolean isVisible, TextField... txtF){
        for (TextField element : txtF) {
            element.setVisible(isVisible);
        }
    }
    private void setButtonsVisible(boolean isVisible, Button... btn){// Helper method to set the visibility of buttons
        for (Button element : btn) {
            element.setVisible(isVisible);
        }
    }
    // Method to open the form for deleting the account
    public void openDeleteAccountFormular(ActionEvent actionEvent) {
        // Set visibility for labels, text fields, and buttons

        setLabelsVisible(true,lblDelEmail,lblSettings,lblDelText);
        setLabelsVisible(false,lblRepeatSetting,lblSetting,lblLastLogin,lblMeanScore,lblregisterDate);
        setLabelsVisible(false,lblProfileEmail,lblProfileUsername,lblProfileMovieRated,lblErrorSuccesfullMessage);
        lblSettings.setText("Settings: Delete Account");
        lblDelEmail.setText("Email: ");
        setTextFieldsVisible(false,txtFChangeProperty,txtRepeatNewProperty);
        setTextFieldsVisible(true,txtFAccountDelEmail);
        txtFAccountDelEmail.setText("");
        setButtonsVisible(true,btnDeleteAccount);
        setButtonsVisible(false,btnVerifyNewProperty);
    }
    // Method to open the form for changing the password
    public void openPasswordFormular(ActionEvent actionEvent) {
        // Set visibility for labels, text fields, and buttons
        setLabelsVisible(true,lblSettings,lblSetting,lblRepeatSetting);
        setLabelsVisible(false,lblDelText,lblDelEmail,lblProfileEmail,lblProfileUsername,lblProfileMovieRated,lblErrorSuccesfullMessage,lblLastLogin,lblMeanScore,lblregisterDate);
        setTextFieldsVisible(true,txtRepeatNewProperty,txtFChangeProperty);
        setTextFieldsVisible(false,txtFAccountDelEmail);
        setButtonsVisible(true,btnVerifyNewProperty);
        setButtonsVisible(false,btnDeleteAccount);
        lblSettings.setText("Settings: Change Password");
        lblSetting.setText("Password: ");
        lblRepeatSetting.setText("Repeat Password: ");
        txtFChangeProperty.setText("");
        txtRepeatNewProperty.setText("");
    }
    // Method to open the form for changing the email
    public void openEmailFormular(ActionEvent actionEvent) {
        // Set visibility for labels, text fields, and buttons
        setLabelsVisible(true,lblSetting,lblRepeatSetting,lblSettings);
        setLabelsVisible(false,lblDelText,lblDelEmail,lblProfileUsername,lblProfileEmail,lblProfileMovieRated,lblErrorSuccesfullMessage,lblLastLogin,lblMeanScore,lblregisterDate);
        setTextFieldsVisible(true,txtFChangeProperty,txtRepeatNewProperty);
        setTextFieldsVisible(false,txtFAccountDelEmail);
        setButtonsVisible(false,btnDeleteAccount);
        setButtonsVisible(true,btnVerifyNewProperty);
        lblSettings.setText("Settings: Change Email");
        lblSetting.setText("Email: ");
        lblRepeatSetting.setText("Repeat Email: ");
        txtFChangeProperty.setText("");
        txtRepeatNewProperty.setText("");

    }
    // Method to handle verification of a new property (e.g., password)
    public void verifyNewPropertyOnAction(ActionEvent actionEvent) throws SQLException {
        if("Email: ".equals(lblSetting.getText())){
            changeEmail();
        } else if ("Username: ".equals(lblSetting.getText())) {
            changeUsername();
        } else if ("Password: ".equals(lblSetting.getText())) {
            changePassword();
        } else{
            lblErrorSuccesfullMessage.setVisible(true);
            lblErrorSuccesfullMessage.setText("Error");
        }



    }

    private void changePassword() throws SQLException {

        //Create a connection to the mysql database
        dbConnection = DatabaseConnection.connect();

        lblErrorSuccesfullMessage.setVisible(true);
        // Check if the database connection is successful
        if(dbConnection == null){
            lblErrorSuccesfullMessage.setText("Error connecting to the database!");
        }
        else
        {
            // Validate the new password
            if(!passwordIsValid(txtFChangeProperty.getText(),txtRepeatNewProperty.getText())){
                lblErrorSuccesfullMessage.setText("Please type in valid password with 10 chars");
            }
            else
            {
                String hashPassword = PasswordHashing.hashPassword(txtFChangeProperty.getText());
                // Perform the password change in the database
                boolean changedPassword = queryUser.changePassword(dbConnection, userInfo.getID(), hashPassword);

                if (changedPassword) {
                    // Update the user information and display success message
                    lblErrorSuccesfullMessage.setText("Successfully changed password: \n" + userInfo.getPassword() + " \nto: " + txtFChangeProperty.getText());
                    userInfo.setPassword(txtFChangeProperty.getText());
                } else {
                    // Display an error message if the password change was not successful
                    lblErrorSuccesfullMessage.setText("Error, something went wrong!");
                }
            }
        }

    }

    private boolean passwordIsValid(String password, String repeatPassword) {
        // Check if email is not empty
        if (password.isEmpty() || repeatPassword.isEmpty()  || !password.equals(repeatPassword) || password.length() < 10) {
            return false;
        }

        // Return true if all conditions are met
        return true;
    }

    public void changeEmail() throws SQLException {

            //Create a connection to the mysql database
            dbConnection = DatabaseConnection.connect();
        lblErrorSuccesfullMessage.setTextFill(Color.RED);
            lblErrorSuccesfullMessage.setVisible(true);
            // Check if the database connection is successful
            if(dbConnection == null){
                lblErrorSuccesfullMessage.setText("Error connecting to the database!");
            }
            else
            {
                //check if email is already registered
                boolean emailIsAlreadyRegistered = queryUser.emailIsInDb(txtFChangeProperty.getText());
                if(emailIsAlreadyRegistered){
                    lblErrorSuccesfullMessage.setText("This email address is already registered!");
                    return;
                }
                // Validate the new email address
                if(!emailIsValid(txtFChangeProperty.getText(),txtRepeatNewProperty.getText())){
                    lblErrorSuccesfullMessage.setText("Please type in valid Email address");
                }
                else
                {
                    // Perform the email change in the database
                    boolean changedEmail = queryUser.changeEmail(dbConnection, userInfo.getID(), txtFChangeProperty.getText());

                    if (changedEmail) {
                        // Update the user information and display success message
                        lblErrorSuccesfullMessage.setTextFill(Color.GREEN);
                        lblErrorSuccesfullMessage.setText("Successfully changed Email: " + userInfo.getEmail() + " to: " + txtFChangeProperty.getText());
                        userInfo.setEmail(txtFChangeProperty.getText());
                    } else {
                        // Display an error message if the email change was not successful
                        lblErrorSuccesfullMessage.setText("Error, something went wrong!");
                    }
                }
            }

        }


    public void changeUsername() throws SQLException {
        //Create a connection to the mysql database
        dbConnection = DatabaseConnection.connect();

        lblErrorSuccesfullMessage.setVisible(true);
        // Check if the database connection is successful
        if(dbConnection == null){
            lblErrorSuccesfullMessage.setText("Error connecting to the database!");
        }
        else
        {
            // Validate the new username
            if(!userNameIsValid(txtFChangeProperty.getText(),txtRepeatNewProperty.getText())){
                lblErrorSuccesfullMessage.setText("Please type in valid username");
            }
            else
            {
                boolean userNameRegistered = queryUser.userNameIsInUse(dbConnection,txtFChangeProperty.getText());
                if(userNameRegistered){
                    // Display an error message if the username is already in use
                    lblErrorSuccesfullMessage.setTextFill(Color.RED);
                    lblErrorSuccesfullMessage.setText("This username is already registered!");
                    return;
                }
                // Perform the username change in the database
                boolean changedUsername = queryUser.changeUsername(dbConnection, userInfo.getID(), txtFChangeProperty.getText());

                if (changedUsername) {
                    // Update the user information and display success message
                    lblErrorSuccesfullMessage.setText("Successfully changed \nUsername: " + userInfo.getUsername() + " to: " + txtFChangeProperty.getText());
                    userInfo.setUsername(txtFChangeProperty.getText());
                    lblHelloUser.setText("Hello: " + userInfo.getUsername());
                } else {
                    // Display an error message if the username change was not successful
                    lblErrorSuccesfullMessage.setTextFill(Color.RED);
                    lblErrorSuccesfullMessage.setText("Error, something went wrong!");
                }
            }

        }
    }
    public void verifyDelAccount(ActionEvent actionEvent) {
    }

    @FXML
    public void btnOnHover(MouseEvent mouseEvent) {
        Button sourceButton = (Button) mouseEvent.getSource();
        sourceButton.setStyle("-fx-background-color: #4D1D13; -fx-background-insets: 1; -fx-border-color: #4D1D13; -fx-border-radius: 10%;");
    }

    public void btnOnHoverExit(MouseEvent mouseEvent) {
        Button sourceButton = (Button) mouseEvent.getSource();
        sourceButton.setStyle("-fx-border-color: #4D1D13; -fx-background-insets: 1; -fx-background-color: transparent; -fx-border-radius: 10%;");
    }

    // Method to open the form for user information
    public void openUserInformation(ActionEvent actionEvent) throws SQLException {
            // Display user information in the form
        lblProfileEmail.setText("Email adress: " + userInfo.getEmail());
        lblProfileUsername.setText("Username: " + userInfo.getUsername());
        lblProfileMovieRated.setText("Amount of movies rated: " +userInfo.getMoviesRated());
        lblregisterDate.setText("Registerdate: " + userInfo.getRegisterDate());
        lblLastLogin.setText("Lastlogin: " + userInfo.getLastLogin());
        lblMeanScore.setText("Meanscore of movies rated: " + String.format("%.2f", userInfo.getMeanScore()));
        lblSettings.setText("Profile");

        // Hide unnecessary labels and text fields, and show user information labels
        setLabelsVisible(false,lblErrorSuccesfullMessage,lblSetting,lblDelEmail,lblDelText,lblRepeatSetting);
        setLabelsVisible(true,lblSettings,lblProfileEmail,lblProfileUsername,lblProfileMovieRated,lblLastLogin,lblMeanScore,lblregisterDate);
        setTextFieldsVisible(false,txtFChangeProperty,txtFAccountDelEmail,txtRepeatNewProperty);
        setButtonsVisible(false,btnDeleteAccount,btnVerifyNewProperty);
    }

    public void closeOnClick(ActionEvent actionEvent) {
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        guiWindowManager.setAccountSettingOpen(false);
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    public boolean userNameIsValid(String username,String repeatUsername){
        if(username.isEmpty() || !username.equals(repeatUsername)){
            return false;
        }
        return true;
    }
    public boolean emailIsValid(String email, String repeatEmail) {
        // Check if email is not empty
        if (email.isEmpty() || !isValidEmail(email) || !email.equals(repeatEmail)) {
            return false;
        }

        // Return true if all conditions are met
        return true;
    }
    // Helper method to validate email address with simple checks for "@" and "."
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}

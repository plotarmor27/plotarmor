// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;


/**
 * The Main class represents the main entry point of the PlotArmor application.
 * It handles user authentication, login, registration, and password reset functionalities.
 *
 * Features include:
 * - User authentication by checking credentials against the database.
 * - Opening the main movie view upon successful login.
 * - Providing options for user registration and password reset.
 *
 * The class extends the JavaFX Application class and is responsible for launching the
 * graphical user interface (GUI) of the application.
 */
public class Main extends Application {

    public Button btnResetPassword;
    public PasswordField txtFPassword;
    public TextField txtEmail;
    public Label lblLoginFailed;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnRegister;
    RegisterController registerGUI;

    ResetPasswordController resetPassword;

    Stage loginView = new Stage();
    DatabaseQuery dataQuery = new DatabaseQuery();
    Connection connection;
    TitleBarController titleBarController = new TitleBarController();


    @Override
    public void start(Stage loginView) throws IOException {
        loginView.setTitle("PlotArmor - User Login");
        Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(login);
        loginView.getIcons().add(new Image("login/rustung.png"));
        loginView.initStyle(StageStyle.UNDECORATED);
        loginView.setResizable(false);


        loginView.setScene(scene);
        loginView.show();
        titleBarController.controllTitleBar(login,loginView);

    }

    public static void main(String[] args) {
        launch(args);
    }
    public void openLoginView() throws IOException {

        loginView.setTitle("PlotArmor - User Login");
        Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(login);
        loginView.getIcons().add(new Image("login/rustung.png"));
        loginView.initStyle(StageStyle.UNDECORATED);
        loginView.setResizable(false);
        titleBarController.controllTitleBar(login,loginView);

        loginView.setScene(scene);
        loginView.show();
    }
    public void closeOnClick(ActionEvent e) throws URISyntaxException, IOException, InterruptedException {

        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        if(!guiWindowManager.isResetPasswordOpen()){
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void LoginOnAction(ActionEvent e) throws IOException {
        // Retrieve user input
        String email = txtEmail.getText();
        String password = txtFPassword.getText();

        //Create a connection to the mysql database
        connection = DatabaseConnection.connect();

        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        if(guiWindowManager.isResetPasswordOpen()){
            // login failed
            lblLoginFailed.visibleProperty().set(true);
            lblLoginFailed.setText("Login failed,\nplease close Reset password\n Window!");
        }
        else{
            lblLoginFailed.visibleProperty().set(true);
            if(connection == null){
                lblLoginFailed.setText("Error connecting to the database!");
            }
            else{

                if(dataQuery.isUserCredentialsValid(connection,email,password)){
                      // successfully logged in
                    lblLoginFailed.setTextFill(Color.GREEN);
                    lblLoginFailed.setText("You will be logged\nin a second...!");

                    //Platform working on the application thread. We set it later so that user
                    // can see a text of lblloginfailed
                    Platform.runLater(() -> {
                        Stage stage = (Stage) btnRegister.getScene().getWindow();
                        //mainMoviePageView.openMainMovieView();
                        mainMoviePage2Controller m2 = new mainMoviePage2Controller();
                        try {
                            m2.openMainMovieView();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("Erfolgreich eingeloggt!");
                        stage.close();
                            });

                }else {
                    // login failed
                    lblLoginFailed.setText("Login failed,\nemail or password is wrong!");
                }
            }
        }


    }
     public void registerOnAction(ActionEvent e) throws IOException {
         GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
         if(!guiWindowManager.isResetPasswordOpen()){
             registerGUI = new RegisterController();
             Stage stage = (Stage) btnRegister.getScene().getWindow();
             stage.close();
             registerGUI.openRegister();

         }
         else{
             lblLoginFailed.setVisible(true);
             lblLoginFailed.setText("Please close reset Password\nWindow.");
         }

    }
    public void resetPasswordOnAction(ActionEvent e) throws IOException {
        GUIWindowManager guiManager = GUIWindowManager.getInstance();
        boolean resetPasswordGuiIsOpen = guiManager.isResetPasswordOpen();

        if(!resetPasswordGuiIsOpen){
            resetPassword = new ResetPasswordController();
            guiManager.setResetPasswordOpen(true);
            resetPassword.openResetPasswordView();
        }

    }
}

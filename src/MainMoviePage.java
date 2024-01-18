import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

public class MainMoviePage {

    public TextField searchMoviesTxtField;
    public Button btnMyAccount;
    public Button btnCLOSE;
    Stage mainMovieStage = new Stage();

    MyRatedMovies myRatedMovies;
    AccountSettings accountSettings;

    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnMyMovies;
    private final ObservableList<String> movieList = FXCollections.observableArrayList();

    @FXML
    private ListView movieListView;
    DatabaseQuery query = new DatabaseQuery();
    TitleBarController titleBarController = new TitleBarController();
    Connection connection;
    GUIWindowManager guiWindowManager;
    @FXML
    public void initialize() throws IOException, SQLException {

        connection = DatabaseConnection.connect();
        if(movieList.size() < 1) {
            query.loadMoviesToList(connection, movieList);
            movieListView.setItems(movieList);
            System.out.println(movieList.size());
        }
        else{

        }
    }



    public void openMainMovieView() throws IOException {

        mainMovieStage.setTitle("PlotArmor - MainPage");
        Parent mainMoviePage = FXMLLoader.load(getClass().getResource("./mainMovieView/mainFilmPage.fxml"));
        mainMovieStage.getIcons().add(new Image("login/rustung.png"));
        Scene scene = new Scene(mainMoviePage);
        mainMovieStage.setResizable(false);
        mainMovieStage.initStyle(StageStyle.UNDECORATED);
        mainMovieStage.setScene(scene);
        titleBarController.controllTitleBar(mainMoviePage,mainMovieStage);
        mainMovieStage.show();

    }
    public void btnMyMoviesOnClick(ActionEvent e) throws IOException {
        guiWindowManager = GUIWindowManager.getInstance();
        if(!guiWindowManager.isMyRatedMoviesOpen()){
            guiWindowManager.setMyRatedMoviesOpen(true);
            myRatedMovies = new MyRatedMovies();
            myRatedMovies.openMyRatedMoviesView();
        }

    }
    public void logOutOnClick(ActionEvent e) throws IOException, URISyntaxException, InterruptedException {
        guiWindowManager = GUIWindowManager.getInstance();
        if(!guiWindowManager.isMovieInformationControllerOpen() && !guiWindowManager.isAccountSettingOpen() && !guiWindowManager.isMyRatedMoviesOpen()) {
            Stage stage = (Stage) btnLogOut.getScene().getWindow();
            stage.close();
            Main m1 = new Main();
            m1.openLoginView();
        }
    }



    public void myAccountBtnOnAction(ActionEvent actionEvent) throws IOException {
        guiWindowManager = GUIWindowManager.getInstance();
        if(!guiWindowManager.isAccountSettingOpen()){
            guiWindowManager.setAccountSettingOpen(true);
            accountSettings = new AccountSettings();
            accountSettings.openAccountSettingOverview();
        }

    }
    public void listViewItemClicked(MouseEvent mouseEvent) throws IOException, SQLException {
        if(mouseEvent.getClickCount() == 2){
            guiWindowManager = GUIWindowManager.getInstance();
            if(!guiWindowManager.isMovieInformationControllerOpen()) {

                MovieInformationController movieController = new MovieInformationController();
                String selectedItem = (String) movieListView.getSelectionModel().getSelectedItem();
                movieController.openMovieInformation(selectedItem);
                guiWindowManager.setMovieInformationControllerOpen(true);
            }
        }
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        String searchTerm = searchMoviesTxtField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            // Wenn das Suchfeld leer ist, zeige die Original-Filmliste an
            movieListView.setItems(movieList);
        } else {
            // Andernfalls filtere die Liste basierend auf dem Suchbegriff
            ObservableList<String> filteredList = movieList.filtered(movie -> movie.toLowerCase().contains(searchTerm));
            movieListView.setItems(filteredList);
        }
    }

    public void closeOnClick(ActionEvent actionEvent) {
        guiWindowManager = GUIWindowManager.getInstance();
        if(!guiWindowManager.isAccountSettingOpen() && !guiWindowManager.isMovieInformationControllerOpen() && !guiWindowManager.isMyRatedMoviesOpen()){
            Stage stage = (Stage) btnCLOSE.getScene().getWindow();
            stage.close();
        }

    }
}

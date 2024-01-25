import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
/**
 * The mainMoviePage2Controller class is the JavaFX controller for the main movie page in the application.
 * It manages UI elements, user interactions, and database queries to create a dynamic movie browsing experience.
 *
 * Features include:
 * - Connecting to the database and retrieving movie information.
 * - Dynamic creation of movie cells in a grid layout.
 * - User interaction handling, such as button clicks, key events, and scroll events.
 * - Searching for movies or users based on user input.
 * - Navigation to different views, including "My Movies" and account settings.
 * - Opening and closing additional windows, such as detailed movie information and account settings.
 * - Loading more movie cells dynamically when scrolling near the bottom of the page.
 * - Switching between movie and user searches using a ComboBox.
 *
 * The class integrates database functionality, UI management, and event handling to create an interactive
 * and feature-rich main movie page within the application.
 */

public class mainMoviePage2Controller {
    public Button btnLogOut,btnCLOSE,btnMyMovies,btnMyAccount;
    public TextField searchMoviesTxtField;
    public Label lblMainMovie;
    public ScrollPane scrollMovie;
    public ComboBox cBoxSelection;
    public ListView<String> searchUserMovieList;
    @FXML
    private GridPane gridPane;
    DatabaseQuery query = new DatabaseQuery();
    DatabaseQueryUser queryUser = new DatabaseQueryUser();
    Connection connection;
    GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
    AccountSettings accountSettings;
    // ObservableLists for storing movie names, ratings, poster paths, user names, and selection options
    private final ObservableList<String> movieNameList = FXCollections.observableArrayList();
    private final ObservableList<String> movieFilteredNameList = FXCollections.observableArrayList();
    private final ObservableList<String> movieRatingList = FXCollections.observableArrayList();
    private final ObservableList<String> moviePosterPath = FXCollections.observableArrayList();
    private final ObservableList<String> userNameList = FXCollections.observableArrayList();
    private final ObservableList<String> selectionUserMovie = FXCollections.observableArrayList();
    public void initialize() throws IOException, SQLException {
        // Hide the searchUserMovieList
        searchUserMovieList.setVisible(false);
        // Establish a connection to the database
        connection = DatabaseConnection.connect();
        // Add "Movie" and "User" options to the selectionUserMovie list
        selectionUserMovie.add("Movie");
        selectionUserMovie.add("User");
        // Retrieve the list of movie names from the database
        query.getMovieName(connection,movieNameList);
        //copy movieNameList to other List for the filter function
        movieFilteredNameList.addAll(movieNameList);

        // Set the options for the ComboBox
        cBoxSelection.setItems(selectionUserMovie);
        cBoxSelection.getSelectionModel().selectFirst();

        // Retrieve movie ratings, poster paths, and user names from the database
        query.getRatings(connection,movieRatingList);
        query.getPosterPath(connection,moviePosterPath);
        queryUser.getUserName(connection,userNameList);
        // Close the database connection
        connection.close();

        for (int row = 0; row < 10; row++) {
            if(row > gridPane.getRowConstraints().size()-1){
                gridPane.getRowConstraints().add(new RowConstraints());
            }
            // Add a cell to the GridPane for each column
            for (int col = 0; col < 6; col++) {
                Pane cellPane = createCellPane();
                gridPane.add(cellPane, col, row);
            }
        }

    }

    private Pane createCellPane() throws IOException, SQLException {
        // Load the FXML file for the moviePane(-> moviepane is template for displaying the movie informations)
        FXMLLoader load = new FXMLLoader(getClass().getResource("/mainMovieView/moviePane.fxml"));
        load.load();
        // Retrieve UI elements from the loaded FXML file
        Pane pane = (Pane)load.getNamespace().get("paneId");
        Label lbl = (Label)load.getNamespace().get("movieLbl");
        ImageView image = (ImageView)load.getNamespace().get("posterImage");

        // Set the poster image for the cell
        Image poster = new Image(moviePosterPath.get(0));
        image.setImage(poster);
        moviePosterPath.remove(0);

        // Set the movie name for the cell
        lbl.setText(movieNameList.get(0));
        movieNameList.remove(0);

        // Set the star rating for the cell
        Label star = (Label) load.getNamespace().get("starlbl");
        String ratingValueAsString = movieRatingList.get(0);
        double ratingValue = Double.parseDouble(ratingValueAsString);
        String formattedRating = String.format("%.2f", ratingValue);
        star.setText(formattedRating);
        movieRatingList.remove(0);

        // Set cursor property for label to indicate it's clickable
        lbl.cursorProperty().set(Cursor.cursor("HAND"));

        // Open the MovieInformationController when the label is clicked
        lbl.setOnMouseClicked(mouseEvent ->{
             MovieInformationController movieInfo = new MovieInformationController();
            try {
                // Check if the MovieInformationController is already open
                if(!guiWindowManager.isMovieInformationControllerOpen()){
                    guiWindowManager.setMovieInformationControllerOpen(true);
                    movieInfo.openMovieInformation(lbl.getText());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return pane;
    }
    public void openMainMovieView() throws IOException {
        mainmoviepage2 m2 = new mainmoviepage2();
        m2.openMainMovieView();
    }
    public void btnMyMoviesOnClick(ActionEvent e) throws IOException {

        if(!guiWindowManager.isMyRatedMoviesOpen()){
            guiWindowManager.setMyRatedMoviesOpen(true);
            MyRatedMoviesController myRatedMoviesController = new MyRatedMoviesController();

            myRatedMoviesController.openRatedLists();
        }

    }
    public void logOutOnClick(ActionEvent e) throws IOException, URISyntaxException, InterruptedException {
        if(!guiWindowManager.isMovieInformationControllerOpen() && !guiWindowManager.isAccountSettingOpen() && !guiWindowManager.isMyRatedMoviesOpen()) {
            Stage stage = (Stage) btnLogOut.getScene().getWindow();
            stage.close();
            Main m1 = new Main();
            m1.openLoginView();
        }

    }
    public void myAccountBtnOnAction(ActionEvent actionEvent) throws IOException {

        if(!guiWindowManager.isAccountSettingOpen()){
            guiWindowManager.setAccountSettingOpen(true);
            accountSettings = new AccountSettings();
            accountSettings.openAccountSettingOverview();
        }

    }
    public void listViewItemClicked(MouseEvent mouseEvent) throws IOException, SQLException {
        // Check if the item in the ListView is double-clicked
        if(mouseEvent.getClickCount() == 2){
            // Check if the selected item pertains to movies
            if(cBoxSelection.getSelectionModel().getSelectedItem().toString() == "Movie"){
                if(!guiWindowManager.isMovieInformationControllerOpen())
                {
                MovieInformationController movieController = new MovieInformationController();
                String selectedItemMovie = (String) searchUserMovieList.getSelectionModel().getSelectedItem();
                movieController.openMovieInformation(selectedItemMovie);
                guiWindowManager.setMovieInformationControllerOpen(true);
                searchMoviesTxtField.setText("");
                    searchUserMovieList.setVisible(false);
                }
            }
            else {
                // If the selected item pertains to users
                MyRatedMoviesController myratedView = new MyRatedMoviesController();
                String selectedItemUser = (String) searchUserMovieList.getSelectionModel().getSelectedItem();
                myratedView.openRatedListOfSelectedUser(selectedItemUser);
                guiWindowManager.setMyRatedMoviesOpen(true);
                searchMoviesTxtField.setText("");
                searchUserMovieList.setVisible(false);
            }

        }
    }
    public void onKeyReleased(KeyEvent keyEvent) {
        String searchTerm = searchMoviesTxtField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            searchUserMovieList.setVisible(false);
        }
        else {
            if(cBoxSelection.getSelectionModel().getSelectedItem().toString() == "Movie"){
                searchUserMovieList.setVisible(true);
                ObservableList<String> filteredList = movieFilteredNameList.filtered(movie -> movie.toLowerCase().contains(searchTerm));
                searchUserMovieList.setItems(filteredList);
            }
            else{
                searchUserMovieList.setVisible(true);
                ObservableList<String> filteredList = userNameList.filtered(user -> user.toLowerCase().contains(searchTerm));
                searchUserMovieList.setItems(filteredList);
            }

        }
    }

    public void closeOnClick(ActionEvent actionEvent) {

        if(!guiWindowManager.isAccountSettingOpen() && !guiWindowManager.isMovieInformationControllerOpen() && !guiWindowManager.isMyRatedMoviesOpen()){
            Stage stage = (Stage) btnCLOSE.getScene().getWindow();
            stage.close();
        }

    }

    public void onMainMovieLbClick(MouseEvent mouseEvent) throws SQLException, IOException {
        MovieInformationController movieInfo = new MovieInformationController();
        movieInfo.openMovieInformation(lblMainMovie.getText());
    }

    public void onScroll(ScrollEvent scrollEvent) throws SQLException, IOException {
        int row = gridPane.getRowCount();
        gridPane.getRowConstraints().add(new RowConstraints());
        gridPane.getRowConstraints().add(new RowConstraints());
        if(scrollMovie.getVvalue() > 0.99) {
            for (int r = row; r < gridPane.getRowCount(); r++) {
                for (int col = 0; col < 6; col++) {
                    Pane cellPane = createCellPane();
                    gridPane.add(cellPane, col, r);
                }
            }
            scrollMovie.setVvalue(0.80);
        }
    }

    public void cBoxSelectionOnItemSwitch(ActionEvent actionEvent) {
        searchUserMovieList.setVisible(false);
        if(Objects.equals(cBoxSelection.getSelectionModel().getSelectedItem().toString(), "Movie")){
            searchMoviesTxtField.setPromptText("Search for Movies");
            searchMoviesTxtField.setText("");
        }
        else{
            searchMoviesTxtField.setPromptText("Search for Users");
            searchMoviesTxtField.setText("");
        }


    }
}

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

public class mainMoviePage2Controller {
    public Button btnLogOut;
    public Button btnCLOSE;
    public TextField searchMoviesTxtField;
    public Button btnMyMovies;
    public Button btnMyAccount;
    public Label lblMainMovie;
    public ListView movieListView;
    public ScrollPane scrollMovie;
    @FXML
    private GridPane gridPane;
    DatabaseQuery query = new DatabaseQuery();
    Connection connection;
    GUIWindowManager guiWindowManager;
    AccountSettings accountSettings;
    private final ObservableList<String> movieNameList = FXCollections.observableArrayList();
    private ObservableList<String> movieFilteredNameList = FXCollections.observableArrayList();
    private final ObservableList<String> movieRatingList = FXCollections.observableArrayList();

    private final ObservableList<String> moviePosterPath = FXCollections.observableArrayList();

    UserInformation userInfo = UserInformation.getInstance();

    public void initialize() throws IOException, SQLException {
        movieListView.setVisible(false);

        connection = DatabaseConnection.connect();

        query.getMovieName(connection,movieNameList);
        //copy movieNameList to other List
        movieFilteredNameList.addAll(movieNameList);

        //get the movierated size to store it in the userinformation class
        HashMap<String, Integer> movieRatings = query.getMovieRatingsForUser(connection, userInfo.getID());
        userInfo.setMoviesRated(movieRatings.keySet().size());

        query.getRatings(connection,movieRatingList);
        query.getPosterPath(connection,moviePosterPath);
        connection.close();

        for (int row = 0; row < 10; row++) {
            if(row > gridPane.getRowConstraints().size()-1){
                gridPane.getRowConstraints().add(new RowConstraints());
            }
            for (int col = 0; col < 6; col++) {
                Pane cellPane = createCellPane();
                gridPane.add(cellPane, col, row);
            }
        }

    }

    private Pane createCellPane() throws IOException, SQLException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("./mainMovieView/moviePane.fxml"));
        load.load();

        Pane pane = (Pane)load.getNamespace().get("paneId");
        Label lbl = (Label)load.getNamespace().get("movieLbl");
        ImageView image = (ImageView)load.getNamespace().get("posterImage");

        Image poster = new Image(moviePosterPath.get(0));
        image.setImage(poster);
        moviePosterPath.remove(0);


        lbl.setText(movieNameList.get(0).toString());
        movieNameList.remove(0);

        Label star = (Label) load.getNamespace().get("starlbl");

        String ratingValueAsString = movieRatingList.get(0);
        double ratingValue = Double.parseDouble(ratingValueAsString);

        String formattedRating = String.format("%.2f", ratingValue);
        star.setText(formattedRating);
        movieRatingList.remove(0);

        lbl.cursorProperty().set(Cursor.cursor("HAND"));

        lbl.setOnMouseClicked(mouseEvent ->{
             MovieInformationController movieInfo = new MovieInformationController();
            try {
                guiWindowManager = GUIWindowManager.getInstance();
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
        guiWindowManager = GUIWindowManager.getInstance();
        if(!guiWindowManager.isMyRatedMoviesOpen()){
            guiWindowManager.setMyRatedMoviesOpen(true);
            MyRatedMoviesController myRatedMoviesController = new MyRatedMoviesController();

            myRatedMoviesController.openRatedLists();
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
            movieListView.setVisible(false);
        }
        else {
            movieListView.setVisible(true);
            ObservableList<String> filteredList = movieFilteredNameList.filtered(movie -> movie.toLowerCase().contains(searchTerm));
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

    public void onMainMovieLbClick(MouseEvent mouseEvent) throws SQLException, IOException {
        MovieInformationController movieInfo = new MovieInformationController();
        movieInfo.openMovieInformation(lblMainMovie.getText());
    }

    public void onScroll(ScrollEvent scrollEvent) throws SQLException, IOException {
        int row = gridPane.getRowCount(); //10
        gridPane.getRowConstraints().add(new RowConstraints()); //10+1
        if(scrollMovie.getVvalue() > 0.99) {
            for (int r = row; r < gridPane.getRowCount(); r++) {
                for (int col = 0; col < 6; col++) {
                    Pane cellPane = createCellPane();
                    gridPane.add(cellPane, col, r);
                }
            }
            scrollMovie.setVvalue(0.90);
        }
    }
}

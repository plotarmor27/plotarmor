import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MyRatedMoviesController {

    public Button btnBack;
    public Label lbHelloUser;
    public ListView listVMyRatedMovies;
    public Button btnSortRating;
    public Label lblSureDeleting;
    UserInformation userInfo = UserInformation.getInstance();
    ObservableList<Pane> ratedMovies = FXCollections.observableArrayList();

    public void initialize() throws IOException, SQLException {
        lbHelloUser.setText("Hello : " + userInfo.getUsername());
        DatabaseQuery query = new DatabaseQuery();
        Connection connection = DatabaseConnection.connect();

        HashMap<String, Integer> movieRatings = query.getMovieRatingsForUser(connection, userInfo.getID());
        userInfo.setMoviesRated(movieRatings.keySet().size());
        int movieRated = 0;

        for (String movieName : movieRatings.keySet()) {
                movieRated = movieRatings.get(movieName);
                addRatedMoviesToList(movieName,movieRated);
            }
    }
    public void addRatedMoviesToList(String movieName, int movieRated) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("./mainMovieView/moviePane.fxml"));
        load.load();

        Pane pane = (Pane)load.getNamespace().get("movieRatedPane");
        Label lbl = (Label)load.getNamespace().get("movieRatedName");
        SVGPath starOne = (SVGPath) load.getNamespace().get("starOne");
        SVGPath starTwo = (SVGPath) load.getNamespace().get("starTwo");
        SVGPath starThree = (SVGPath) load.getNamespace().get("starThree");
        SVGPath starFour = (SVGPath) load.getNamespace().get("starFour");
        SVGPath starFive = (SVGPath) load.getNamespace().get("starFive");
        Button deleteRatedMovie = (Button) load.getNamespace().get("btnDeleteRatedMovie");

        deleteRatedMovie.setOnMouseClicked(e ->{
                    if(e.getClickCount() == 2){
                        lblSureDeleting.setVisible(false);

                        Connection connection = DatabaseConnection.connect();
                        DatabaseQuery query = new DatabaseQuery();
                        query.deleteUserRating(connection,userInfo.getID(),lbl.getText());
                        userInfo.setMoviesRated(userInfo.getMoviesRated()-1);
                        listVMyRatedMovies.getItems().remove(pane);
                        ratedMovies.remove(pane);
                        listVMyRatedMovies.refresh();
                    }
                    else{
                        lblSureDeleting.setVisible(true);
                        lblSureDeleting.setText("Are you sure, you want to delete: " +lbl.getText()+" ?");
                    }

        });

        switch (movieRated) {
            case 1 ->{ starTwo.setVisible(false);
                starThree.setVisible(false);
                starFour.setVisible(false);
                starFive.setVisible(false);
        }

            case 2 -> { starThree.setVisible(false);
                starFour.setVisible(false);
                starFive.setVisible(false);
            }

            case 3 ->  {  starFour.setVisible(false);
                starFive.setVisible(false);}
            case 4 ->  starFive.setVisible(false);
            case 5 -> movieRated = 5;
        }

        lbl.setText(movieName);
        Label lblRating = (Label)load.getNamespace().get("lblRatingStoring");
        lblRating.setText(String.valueOf(movieRated));
        ratedMovies.add(pane);
        listVMyRatedMovies.getItems().add(pane);
    }
    public void goBack(ActionEvent actionEvent) {
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        guiWindowManager.setMyRatedMoviesOpen(false);
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();


    }
    public void openRatedLists() throws IOException {
        MyRatedMovies myRatedMovies = new MyRatedMovies();
        myRatedMovies.openMyRatedMoviesView();
    }
    public void sortRatinOnClick(ActionEvent actionEvent) {
        // Create a comparator for RatedMoviePane based on the rating
        Comparator<Pane> ratingComparator = Comparator.comparingInt(this::getRatingFromPane).reversed();

        // Sort the list of Panes
        ratedMovies.sort(ratingComparator);

        // Update the ListView
        listVMyRatedMovies.setItems(FXCollections.observableArrayList(ratedMovies));

    }

    private int getRatingFromPane(Pane pane) {
        Label ratingLabel = findRatingLabel(pane);
        if (ratingLabel != null) {
            try {
                return Integer.parseInt(ratingLabel.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    private Label findRatingLabel(Pane pane) {
        ObservableList<Node> children = pane.getChildren();
        for (Node child : children) {
            if (child instanceof Label && "lblRatingStoring".equals(child.getId())) {
                return (Label) child;
            }
        }
        return null;
    }
}

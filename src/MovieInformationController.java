import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * The MovieInformationController class is the controller for the MovieInformation.fxml file.
 * It handles user interactions and updates the UI components in the movie information window.
 *
 * Features include:
 * - Closing the movie information window upon button click.
 * - Handling mouse events for star ratings (filling stars with color on hover).
 * - Displaying movie details such as name, description, duration, release date, and genre.
 * - Handling star rating clicks and printing the selected number of stars.
 *
 * The class uses methods to set various UI elements based on movie details retrieved from the database.
 * Proper exception handling is implemented for potential SQLExceptions during database operations.
 */
public class MovieInformationController {
    public ImageView imageMoviePoster;
    public Text txtGenre;

    public SVGPath starOne;
    public SVGPath starTwo;
    public SVGPath starThree;
    public SVGPath starFour;
    public SVGPath starFive;

    public Button btnClose;
    public Text txtPublicationDate;
    public Text txtDuration;
    @FXML
    public Label lblMovieName;

    public Label txtMovieSummary;
    public ImageView imageBackground;
    public Label lblaverageVoting;
    public Label lblSuccessFailedRating;


    public void closeOnClick(ActionEvent actionEvent) {
        GUIWindowManager guiWindowManager = GUIWindowManager.getInstance();
        guiWindowManager.setMovieInformationControllerOpen(false);
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void svgFillColor(Color color, SVGPath... svg){
        for(SVGPath svgpath : svg){
            svgpath.setFill(color);
        }
    }
    public void starsWhenMouseEntered(MouseEvent mouseEvent) {
        SVGPath svg = (SVGPath) mouseEvent.getSource();
        switch (svg.getId()) {
            case "starOne" -> {
                svgFillColor(Color.YELLOW,starOne);
                svgFillColor(Color.WHITE,starTwo,starThree,starFour,starFive);
            }
            case "starTwo" -> {  svgFillColor(Color.YELLOW,starOne,starTwo);
                svgFillColor(Color.WHITE,starThree,starFour,starFive);
            }
            case "starThree" -> {  svgFillColor(Color.YELLOW,starOne,starTwo,starThree);
                svgFillColor(Color.WHITE,starFour,starFive); }
            case "starFour" -> {        svgFillColor(Color.YELLOW,starOne,starTwo,starThree,starFour);
                svgFillColor(Color.WHITE,starFive); }
            case "starFive" -> {  svgFillColor(Color.YELLOW,starOne,starTwo,starThree,starFour,starFive);}
        }
    }

    public void starsWhenMouseExit(MouseEvent mouseEvent) {
        svgFillColor(Color.WHITE,starOne,starTwo,starThree,starFour,starFive);
    }
    public void openMovieInformation(String movieName) throws IOException, SQLException {
        MovieInformation movieInfo = new MovieInformation();
        movieInfo.openMovieInformation(movieName);
    }
    public void setLblMovieName(String movieName){
        this.lblMovieName.setText(movieName);
    }
    public void setTxtMovieDescription(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){
            String overview = "Summary: \n";
             overview += query.getMovieDescribtion(connection,movieName);
            this.txtMovieSummary.setMaxWidth(500);
            this.txtMovieSummary.setWrapText(true);
            this.txtMovieSummary.setText(overview);
        }
        connection.close();
    }
    public void setDuration(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){

            String duration = query.getMovieDuration(connection,movieName);

            this.txtDuration.setText("Duration: " +duration + " minutes");
        }
        connection.close();
    }
    public void setReleasedate(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){

            String releasedate = query.getReleasedate(connection,movieName);

            this.txtPublicationDate.setText("Release Date: " +releasedate);
        }
        connection.close();
    }
    public void setGenre(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){

            String genre = query.getGenre(connection,movieName);

            this.txtGenre.setText("Genre: " +genre);
        }
        connection.close();
    }

    public void setPoster(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){

            String posterPath = query.getPoster(connection,movieName);
            Image poster = new Image(posterPath);
            this.imageMoviePoster.setImage(poster);
        }
        connection.close();
    }
    public void setBackgroundPoster(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){

            String background_path = query.getBackgroundPath(connection,movieName);
            Image backgroundImage = new Image(background_path);
            this.imageBackground.setImage(backgroundImage);
        }
        connection.close();
    }

    public void setAverageVoting(String movieName) throws SQLException {
        Connection connection = DatabaseConnection.connect();
        DatabaseQuery query = new DatabaseQuery();
        if(connection != null){

            String averageVoting = query.getAverageVoting(connection,movieName);

            this.lblaverageVoting.setText("Average Voting: " + averageVoting);
        }
        connection.close();
    }
    public void starsClicked(MouseEvent mouseEvent) {
        DatabaseQuery query = new DatabaseQuery();
        SVGPath s = (SVGPath)mouseEvent.getSource();
        Connection connection = DatabaseConnection.connect();
        int movieRated = 0;
        switch (s.getId()) {
            case "starOne" -> movieRated = 1;
            case "starTwo" -> movieRated = 2;
            case "starThree" -> movieRated = 3;
            case "starFour" -> movieRated = 4;
            case "starFive" -> movieRated = 5;
        }
        UserInformation userInfo = UserInformation.getInstance();
        //Insert rating from user to db
        boolean successfullyRated = query.insertNewRatingToDb(connection,userInfo.getID(),userInfo.getUsername(),this.lblMovieName.getText(),movieRated);

        if(successfullyRated){

            lblSuccessFailedRating.setVisible(true);
            lblSuccessFailedRating.setTextFill(Color.GREEN);
            userInfo.setMoviesRated(userInfo.getMoviesRated()+1);
            lblSuccessFailedRating.setText("Successfully rated!");
        }
        else{
            //Update rating from user in db
            boolean successfullyUpdated = query.updateRatingInDb(connection,userInfo.getID(),this.lblMovieName.getText(),movieRated);
            lblSuccessFailedRating.setVisible(true);
            lblSuccessFailedRating.setTextFill(Color.YELLOWGREEN);
            if(successfullyUpdated){
                lblSuccessFailedRating.setText("Successfully updated movie!");

            }
         }
    }
}

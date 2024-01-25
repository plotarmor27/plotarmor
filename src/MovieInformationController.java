import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
 * Controller class for displaying detailed information about a movie.
 * Handles user interactions for rating the movie and adding notes.
 */
public class MovieInformationController {
    public ImageView imageMoviePoster,imageBackground;
    public Text txtGenre,txtPublicationDate,txtDuration;
    public SVGPath starOne,starTwo,starThree,starFour,starFive;
    public Button btnClose;
    @FXML
    public Label lblMovieName,txtMovieSummary,lblaverageVoting,lblSuccessFailedRating;
    public CheckBox rBtnAddNote;

    public TextArea txtFieldNotes;
    Connection connection = DatabaseConnection.connect();
    UserInformation userInfo = UserInformation.getInstance();
    DatabaseQuery query = new DatabaseQuery();

    /**
     * Initializes the controller. Adjusts the window size and loads movie notes.
     */
    public void initialize() {
        Platform.runLater(() -> {
                    Stage stage = (Stage) btnClose.getScene().getWindow();
                    stage.setHeight(433);
                    txtFieldNotes.setVisible(false);
                    loadMovieNotes();
                });
        rBtnAddNote.selectedProperty().addListener((observable, oldValue, newValue) -> {
            resizeWindow(newValue);
        });
        txtFieldNotes.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(txtFieldNotes.getText().isEmpty()){
                    query.updateNotesInDb(connection, userInfo.getID(), this.lblMovieName.getText(), "");
                } else {
                    query.updateNotesInDb(connection, userInfo.getID(), this.lblMovieName.getText(), txtFieldNotes.getText());
                }
            }
        });
    }



     // Closes the movie information window.
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


        if(connection != null){
            String overview = "Summary: \n";
             overview += query.getMovieDescribtion(connection,movieName);
            this.txtMovieSummary.setMaxWidth(500);
            this.txtMovieSummary.setWrapText(true);
            this.txtMovieSummary.setText(overview);
        }
    }
    public void setDuration(String movieName) throws SQLException {

        if(connection != null){
            String duration = query.getMovieDuration(connection,movieName);
            this.txtDuration.setText("Duration: " +duration + " minutes");
        }

    }
    public void setReleasedate(String movieName) throws SQLException {


        if(connection != null){

            String releasedate = query.getReleasedate(connection,movieName);

            this.txtPublicationDate.setText("Release Date: " +releasedate);
        }

    }
    public void setGenre(String movieName) throws SQLException {


        if(connection != null){

            String genre = query.getGenre(connection,movieName);

            this.txtGenre.setText("Genre: " +genre);
        }

    }
    public void setPoster(String movieName) throws SQLException {

        if(connection != null){

            String posterPath = query.getPoster(connection,movieName);
            Image poster = new Image(posterPath);
            this.imageMoviePoster.setImage(poster);
        }
    }
    public void setBackgroundPoster(String movieName) throws SQLException {
        if(connection != null){
            String background_path = query.getBackgroundPath(connection,movieName);
            Image backgroundImage = new Image(background_path);
            this.imageBackground.setImage(backgroundImage);
        }
    }

    public void setAverageVoting(String movieName) throws SQLException {
        if(connection != null){
            String averageVoting = query.getAverageVoting(connection,movieName);
            this.lblaverageVoting.setText("Average Voting: " + averageVoting);
        }
    }

 // Handles the event when stars are clicked for rating the movie
    public void starsClicked(MouseEvent mouseEvent) throws SQLException {
        SVGPath s = (SVGPath)mouseEvent.getSource();
        int movieRated = 0;
        switch (s.getId()) {
            case "starOne" -> movieRated = 1;
            case "starTwo" -> movieRated = 2;
            case "starThree" -> movieRated = 3;
            case "starFour" -> movieRated = 4;
            case "starFive" -> movieRated = 5;
        }
        //Insert rating from user to db
        boolean successfullyRated = query.insertNewRatingToDb(connection,userInfo.getID(),userInfo.getUsername(),this.lblMovieName.getText(),movieRated);
        if(successfullyRated){
            lblSuccessFailedRating.setVisible(true);
            lblSuccessFailedRating.setTextFill(Color.GREEN);
            lblSuccessFailedRating.setText("Successfully rated!");
        }
        else
        {
            //Update rating from user in db
            boolean successfullyUpdated = query.updateRatingInDb(connection,userInfo.getID(),this.lblMovieName.getText(),movieRated);
            lblSuccessFailedRating.setVisible(true);
            lblSuccessFailedRating.setTextFill(Color.YELLOWGREEN);
            if(successfullyUpdated){
                lblSuccessFailedRating.setText("Successfully updated movie!");

            }
         }
    }

    public void addNoteOnChecked(ActionEvent actionEvent) {
    }
        // Resizes the window based on whether the "Add Note" checkbox is checked.
    private void resizeWindow(boolean isChecked) {
        Stage stage = (Stage) rBtnAddNote.getScene().getWindow();
        if (isChecked) {
            stage.setHeight(547);
            txtFieldNotes.setVisible(true);
        } else {
            stage.setHeight(433);
            txtFieldNotes.setVisible(false);
        }
    }
        // Loads movie notes from the database and displays them in the text area.
    private void loadMovieNotes() {
        try {
            String notes = query.getMovieNotesForUser(connection, userInfo.getID(), lblMovieName.getText());
            Platform.runLater(() -> txtFieldNotes.setText(notes));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


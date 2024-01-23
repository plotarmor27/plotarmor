import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
/**
 * The MovieInformation class is responsible for displaying detailed information about a movie.
 * It opens a new window (Stage) that shows various details, such as the movie name, description,
 * duration, release date, and genre.
 *
 * Features include:
 * - Loading the movie information FXML file.
 * - Configuring the style and properties of the movie information window.
 * - Using the TitleBarController to manage the custom title bar of the window.
 * - Setting movie details using the MovieInformationController after loading the FXML.
 *
 * The class provides a method, openMovieInformation, which is called to display information
 * about a specific movie by passing its name. The method fetches details from the database
 * and updates the UI components accordingly.
 */
public class MovieInformation {
    Stage movieInformation = new Stage();;
    TitleBarController titleBarController = new TitleBarController();

    public void openMovieInformation(String movieName) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewMovieInfo/viewMovieInformation.fxml"));
        Parent movieInfo = loader.load();
        Scene scene = new Scene(movieInfo);
        scene.getStylesheets().add("/viewMovieInfo/notes.css");
        movieInformation.getIcons().add(new Image("login/rustung.png"));
        movieInformation.initStyle(StageStyle.UNDECORATED);
        movieInformation.setResizable(false);
        movieInformation.setScene(scene);
        titleBarController.controllTitleBar(movieInfo,movieInformation);
        // Set the movie name after loading FXML
            MovieInformationController controller = loader.getController();
            controller.setLblMovieName(movieName);
        controller.setTxtMovieDescription(movieName);
        controller.setDuration(movieName);
        controller.setReleasedate(movieName);
        controller.setGenre(movieName);
        controller.setBackgroundPoster(movieName);
        controller.setAverageVoting(movieName);
        controller.setPoster(movieName);
         movieInformation.show();


    }


}

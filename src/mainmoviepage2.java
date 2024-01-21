import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The MainMoviePage class represents the main page of the PlotArmor application,
 * where users can browse and interact with a list of movies. It includes features
 * such as searching for movies, viewing movie details, accessing user-specific pages,
 * and logging out.
 *
 * Features include:
 * - Displaying a list of movies in a ListView.
 * - Searching and filtering movies based on user input.
 * - Opening detailed information about a selected movie.
 * - Navigating to the user's rated movies and account settings.
 * - Logging out from the application.
 *
 * The class uses the DatabaseQuery class for interacting with the database, and it also
 * incorporates a TitleBarController for managing the custom title bar of the main window.
 */

public class mainmoviepage2 {
    Stage mainMovieStage = new Stage();
    TitleBarController titleBarController = new TitleBarController();
    public void openMainMovieView() throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMovieView/mainmoviepage2.fxml"));
        Parent mainMoviePage = loader.load();
        Scene scene = new Scene(mainMoviePage);
        mainMovieStage.setResizable(false);
        mainMovieStage.getIcons().add(new Image("login/rustung.png"));

        mainMovieStage.initStyle(StageStyle.UNDECORATED);
        titleBarController.controllTitleBar(mainMoviePage,mainMovieStage);
        mainMovieStage.setScene(scene);
        mainMovieStage.show();

    }
}


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The MyRatedMovies class represents a controller for displaying a user's rated movies.
 * It implements the Initializable interface to initialize the controller during FXML loading.
 * It provides methods to open the My Rated Movies view, handle the "Go Back" button click,
 * and initialize user-specific information.
 *
 * Features include:
 * - Opening the My Rated Movies view.
 * - Handling "Go Back" button clicks to close the view.
 * - Initializing user-specific information during controller initialization.
 *
 * Usage:
 * - Create an instance of the MyRatedMovies class.
 * - Call the `openMyRatedMoviesView` method to display the My Rated Movies view.
 * - Implement functionality for handling "Go Back" button clicks and initializing user-specific information.
 */
public class MyRatedMovies {


    TitleBarController titleBarController = new TitleBarController();
    Stage myRatedMoviesStage = new Stage();


    public void openMyRatedMoviesView() throws IOException {
        myRatedMoviesStage.setTitle("PlotArmor - MainPage");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewMyRatedMovies/myRatedMovieView.fxml"));
        Parent myRated =  loader.load();
        myRatedMoviesStage.getIcons().add(new Image("login/rustung.png"));
        Scene scene = new Scene(myRated);
        myRatedMoviesStage.initStyle(StageStyle.UNDECORATED);
        myRatedMoviesStage.setResizable(false);
        myRatedMoviesStage.setScene(scene);
        titleBarController.controllTitleBar(myRated,myRatedMoviesStage);
        MyRatedMoviesController controller = loader.getController();
        controller.openOwnRatedList();
        myRatedMoviesStage.show();

    }


    public void openRatedListOfUser(String selectedItemUser) throws IOException {
        myRatedMoviesStage.setTitle("PlotArmor - MainPage");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewMyRatedMovies/myRatedMovieView.fxml"));
        Parent myRated =  loader.load();
        myRatedMoviesStage.getIcons().add(new Image("login/rustung.png"));
        Scene scene = new Scene(myRated);
        myRatedMoviesStage.initStyle(StageStyle.UNDECORATED);
        myRatedMoviesStage.setResizable(false);
        myRatedMoviesStage.setScene(scene);
        titleBarController.controllTitleBar(myRated,myRatedMoviesStage);
        MyRatedMoviesController controller = loader.getController();

        controller.openUsernameList(selectedItemUser);
        myRatedMoviesStage.show();
    }
}

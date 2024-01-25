import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
// Class representing the view for the "My Rated Movies"
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

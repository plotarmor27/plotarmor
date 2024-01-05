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



public class mainmoviepage2 {
    Stage mainMovieStage = new Stage();
    TitleBarController titleBarController = new TitleBarController();
    public void openMainMovieView() throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("./mainMovieView/mainmoviepage2.fxml"));
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


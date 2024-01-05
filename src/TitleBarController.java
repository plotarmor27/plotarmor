import javafx.scene.Parent;
import javafx.stage.Stage;

public class TitleBarController  {

    public void controllTitleBar(Parent pResource, Stage stage){
        double[] x = {0};
        double[] y = {0};
        pResource.setOnMousePressed(event -> {
            x[0] = event.getSceneX();
            y[0] = event.getSceneY();

        });
        pResource.setOnMouseDragged(event -> {
            if (y[0] < 40) {
                stage.setX(event.getScreenX() - x[0]);
                stage.setY(event.getScreenY() - y[0]);
            }
        });
    }
}
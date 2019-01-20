package mapRenderer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import mapRenderer.mapCreator.MainWindowMapCreating;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MapStartMenu menu = new MapStartMenu();
        menu.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

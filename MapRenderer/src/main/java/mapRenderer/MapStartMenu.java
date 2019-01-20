package mapRenderer;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mapRenderer.mapCreator.MainWindowMapCreating;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class MapStartMenu extends Stage {
    private Button simBtn = new Button("Symulator");
    private Button creatorBtn = new Button("Kreator");
    private VBox mainBox = new VBox(10, simBtn, creatorBtn);
    private JsonData jsonData;
    public MapStartMenu(){
        super();
        build();
        setEvents();

    }

    private void setEvents() {
        simBtn.setOnAction(e -> showSimulator());
        creatorBtn.setOnAction(e -> showCreator());
    }

    private void showCreator() {
        MainWindowMapCreating window = new MainWindowMapCreating();
        window.show();
        close();
    }

    private void showSimulator() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(this);
        if(file != null && file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                jsonData = mapper.readValue(file, JsonData.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
            new MainWindow(jsonData).show();
            close();
        }
    }

    private void build() {
        creatorBtn.setMaxWidth(Double.MAX_VALUE);
        simBtn.setMaxWidth(Double.MAX_VALUE);
        simBtn.setStyle("-fx-background-radius: 0; -fx-background-color: #88cc99; -fx-text-fill: #444;");
        creatorBtn.setStyle("-fx-background-radius: 0; -fx-background-color: #444; -fx-text-fill: #fff;");
        mainBox.setPadding(new Insets(20));
        Scene scene = new Scene(mainBox);
        setScene(scene);
    }
}
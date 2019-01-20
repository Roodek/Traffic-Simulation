package mapRenderer.mapCreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mapRenderer.JsonData;
import mapRenderer.Street;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class StreetsController extends Stage {
    private ComboBox<Direction> directionComboBox = new ComboBox<>();
    private TextField textField = new TextField();
    private TextField speedField = new TextField();
    private Button addStreetBtn = new Button("Dodaj");
    private Button saveStreetsBtn = new Button("Zapisz");
    private Button loadStreetsBtn = new Button("Wczytaj");
    private TableView<Street> streetsTable = new TableView<>();
    private TableColumn<Street, String> nameColumn = new TableColumn<>("Nazwa");
    private TableColumn<Street,Double> speedColumn = new TableColumn<>("dozwolona prędkość");
    private TableColumn<Street, Direction> directionColumn = new TableColumn<>("Kierunek");
    private TableColumn<Street, Boolean> generatorColumn = new TableColumn<>("Punkt startowy");
//    private ListView<Street> streetsView = new ListView<>();
    private VBox controlBox = new VBox(new HBox(textField, directionComboBox),new HBox(speedField), new HBox(5, addStreetBtn, loadStreetsBtn), streetsTable, saveStreetsBtn);
    private ObjectMapper mapper = new ObjectMapper();
    private MainWindowMapCreating mainWindow;
    private HashMap<Street, Circle> generatorPoints = new HashMap<>();

    StreetsController(MainWindowMapCreating mainWindowMapCreating) {
        super();
        this.mainWindow = mainWindowMapCreating;
        build();
        setEvents();
    }

    private void forceDoubleValue(){
    speedField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            if (!t1.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                speedField.setText(s);
            }
        }
    });
    }

    private void setEvents() {
        loadStreetsBtn.setOnAction(e -> loadStreetsFromFile());
        saveStreetsBtn.setOnAction(e -> saveStreetsToFile());
        addStreetBtn.setOnAction(e -> addStreet());
        streetsTable.setOnMouseClicked(e -> selectStreet());

    }

    private void selectStreet() {
        Street street = streetsTable.getSelectionModel().getSelectedItem();
        if(street != null) {
            mainWindow.setCurrentStreet(street);
            mainWindow.setTitle(street.toString());
        }
    }

    private void addStreet() {

        Street street = new Street(textField.getText(), directionComboBox.getValue(), speedField.getText());
        textField.clear();
        speedField.clear();
        mainWindow.setCurrentStreet(street);
        mainWindow.setTitle(street.toString());
        streetsTable.getItems().add(street);
        streetsTable.getSelectionModel().select(street);
        street.generatorProperty().addListener((obs, old, val) -> {
            if(val) {
                drawGeneratorPoint(street);
            } else {
                Circle c = generatorPoints.get(street);
                if(c != null) {
                    mainWindow.getCanvasPane().getChildren().remove(c);
                }
            }
        });
    }

    private void saveStreetsToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showSaveDialog(this);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        try {
            JsonData data = new JsonData();
            data.setStreets(streetsTable.getItems());
            data.setCrossroads(mainWindow.getCrossroadsController().getCrossroads());
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void loadStreetsFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(this);
        streetsTable.getItems().clear();
        if(file != null && file.exists()){
            try {
                JsonData data = mapper.readValue(file, JsonData.class);
                List<Street> streets = data.getStreets();

                mainWindow.getCrossroadsController().addCrossroads(data.getCrossroads());
                streetsTable.getItems().setAll(streets);
                streets.forEach(s -> {
                    if (s.isGenerator()) {
                        drawGeneratorPoint(s);
                    }
                    s.generatorProperty().addListener((obs, old, val) -> {
                        if (val) {
                            drawGeneratorPoint(s);
                        } else {
                            Circle c = generatorPoints.get(s);
                            if (c != null) {
                                mainWindow.getCanvasPane().getChildren().remove(c);
                            }
                        }
                    });
                    s.getCoords().forEach(c -> {
                        Circle circle = new Circle(3);
                        circle.setUserData(s);
                        circle.setFill(Color.GREEN);
                        circle.setCenterX(c.getX());
                        circle.setCenterY(c.getY());
                        mainWindow.getCanvasPane().getChildren().add(circle);
                    });
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void drawGeneratorPoint(Street s) {
        s.getCoords().stream().findFirst().ifPresent(c -> {
            Circle circle = new Circle(15);
            circle.setUserData(s);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(3);
            circle.setFill(Color.CORAL);
            circle.setCenterX(c.getX());
            circle.setCenterY(c.getY());
            mainWindow.getCanvasPane().getChildren().add(circle);
            generatorPoints.put(s, circle);
        });
    }

    private void build() {
        buildTable();
        setAlwaysOnTop(true);
        setX(50);
        setY(50);
        Scene s = new Scene(controlBox);
        setScene(s);
        directionComboBox.getItems().addAll(Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN);
        directionComboBox.getSelectionModel().select(0);
    }

    private void buildTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        generatorColumn.setCellValueFactory(new PropertyValueFactory<>("generator"));
        generatorColumn.setCellFactory(col -> new TableCell<>(){
            private CheckBox checkBox = new CheckBox();
            @Override
            public void updateItem(Boolean item, boolean empty){
                if(item == null || empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                    checkBox.setOnAction(e -> getTableRow().getItem().setGenerator(checkBox.isSelected()));
                }
            }
        });
        streetsTable.getColumns().setAll(Arrays.asList(nameColumn, directionColumn, generatorColumn));
    }

    public ObservableList<Street> getStreets() {
        return streetsTable.getItems();
    }
}
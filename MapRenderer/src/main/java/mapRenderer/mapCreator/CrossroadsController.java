package mapRenderer.mapCreator;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mapRenderer.Crossroad;
import mapRenderer.Street;
import mapRenderer.utils.Coord;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CrossroadsController extends Stage {
    private Button clearFirstBtn = new Button("X");
    private Button clearSecondBtn = new Button("X");
    private Button clearThirdBtn = new Button("X");
    private Button clearFourthBtn = new Button("X");
    private ComboBox<Street> firstStreetBox = new ComboBox<>();
    private ComboBox<Street> secondStreetBox = new ComboBox<>();
    private ComboBox<Street> thirdStreetBox = new ComboBox<>();
    private ComboBox<Street> fourthStreetBox = new ComboBox<>();
    private TableView<Crossroad> crossroadTable = new TableView<>();
    private TableColumn<Crossroad, List<Street>> streetsColumn = new TableColumn<>("Ulice");
    private TableColumn<Crossroad, Coord> coordColumn = new TableColumn<>("Współrzędne");
    private Button addCrossroadBtn = new Button("Dodaj skrzyżowanie");
    private VBox content = new VBox(
            10,
            new HBox(firstStreetBox, clearFirstBtn),
            new HBox(secondStreetBox, clearSecondBtn),
            new HBox(thirdStreetBox, clearThirdBtn),
            new HBox(fourthStreetBox, clearFourthBtn),
            addCrossroadBtn,
            crossroadTable)
            ;
    private StreetsController streetsController;
    private MainWindowMapCreating mainWindow;
    private List<ComboBox<Street>> comboBoxes = new LinkedList<>();

    public CrossroadsController(StreetsController streetsController, MainWindowMapCreating mainWindow) {
        super();
        this.streetsController = streetsController;
        this.mainWindow = mainWindow;
        build(streetsController);
        show();

    }

    public void addCrossroad(Coord coord) {
        Rectangle rectangle = new Rectangle(16,16);
        rectangle.setFill(Color.RED);
        rectangle.setX(coord.getX());
        rectangle.setY(coord.getY());
        mainWindow.getCanvasPane().getChildren().add(rectangle);
        Crossroad crossroad = new Crossroad();
        crossroad.getStreets().addAll(comboBoxes.stream().map(ComboBoxBase::getValue)
                .filter(Objects::nonNull).collect(Collectors.toCollection(LinkedList::new)));
        crossroad.setCoord(coord);
        crossroadTable.getItems().add(crossroad);
    }
    public void addCrossroad(Crossroad crossroad) {
        Rectangle rectangle = new Rectangle(16,16);
        rectangle.setFill(Color.RED);
        rectangle.setX(crossroad.getCoord().getX());
        rectangle.setY(crossroad.getCoord().getY());
        mainWindow.getCanvasPane().getChildren().add(rectangle);
        crossroadTable.getItems().add(crossroad);

    }

    private void build(StreetsController streetsController) {
        comboBoxes.addAll(Arrays.asList(firstStreetBox, secondStreetBox, thirdStreetBox, fourthStreetBox));
        buildTable();
        setAlwaysOnTop(true);
        Bindings.bindContent(firstStreetBox.getItems(), streetsController.getStreets());
        Bindings.bindContent(secondStreetBox.getItems(), streetsController.getStreets());
        Bindings.bindContent(thirdStreetBox.getItems(), streetsController.getStreets());
        Bindings.bindContent(fourthStreetBox.getItems(), streetsController.getStreets());
        Scene scene = new Scene(content);
        setScene(scene);
        setEvents();
        firstStreetBox.setMaxWidth(Double.MAX_VALUE);
        secondStreetBox.setMaxWidth(Double.MAX_VALUE);
        thirdStreetBox.setMaxWidth(Double.MAX_VALUE);
        fourthStreetBox.setMaxWidth(Double.MAX_VALUE);
    }

    private void buildTable() {
        coordColumn.setCellValueFactory(new PropertyValueFactory<>("coord"));
        streetsColumn.setCellValueFactory(new PropertyValueFactory<>("streets"));
        crossroadTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        crossroadTable.getColumns().setAll(Arrays.asList(streetsColumn, coordColumn));
    }

    private void setEvents() {
        addCrossroadBtn.setOnAction(e -> {
            if(firstStreetBox.getValue() != null && secondStreetBox.getValue() != null) {
                mainWindow.getCanvasPane().setCursor(Cursor.CROSSHAIR);
            }
        });
        firstStreetBox.setOnAction(e -> drawHighlights());
        secondStreetBox.setOnAction(e -> drawHighlights());
        clearFirstBtn.setOnAction(e -> clearBox(firstStreetBox));
        clearSecondBtn.setOnAction(e -> clearBox(secondStreetBox));
        clearThirdBtn.setOnAction(e -> clearBox(thirdStreetBox));
        clearFourthBtn.setOnAction(e -> clearBox(fourthStreetBox));
    }

    private void clearBox(ComboBox<Street> box) {
        box.setValue(null);
    }

    private void drawHighlights() {
        GraphicsContext gc = mainWindow.getHighlightCanvas().getGraphicsContext2D();
        gc.clearRect(0, 0, mainWindow.getHighlightCanvas().getWidth(), mainWindow.getHighlightCanvas().getHeight());

        if(firstStreetBox.getValue() != null) {
            gc.setFill(Color.ORANGE);
            firstStreetBox.getValue().getCoords().forEach(c -> gc.fillOval(c.getX()-6, c.getY()-6, 12, 12));
        }
        gc.setFill(Color.BLUEVIOLET);
        if (secondStreetBox.getValue() != null) {
            secondStreetBox.getValue().getCoords().forEach(c -> gc.fillOval(c.getX()-6, c.getY()-6, 12, 12));
        }
        gc.setFill(Color.AQUAMARINE);
        if (thirdStreetBox.getValue() != null) {
            thirdStreetBox.getValue().getCoords().forEach(c -> gc.fillOval(c.getX()-6, c.getY()-6, 12, 12));
        }
        gc.setFill(Color.PLUM);
        if (fourthStreetBox.getValue() != null) {
            fourthStreetBox.getValue().getCoords().forEach(c -> gc.fillOval(c.getX()-6, c.getY()-6, 12, 12));
        }
    }

    public List<Crossroad> getCrossroads() {
        return crossroadTable.getItems();
    }

    public void addCrossroads(List<Crossroad> crossroads) {
        crossroads.forEach(this::addCrossroad);
    }
}

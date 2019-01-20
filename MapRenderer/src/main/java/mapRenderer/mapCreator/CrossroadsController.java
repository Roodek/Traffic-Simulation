package mapRenderer.mapCreator;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mapRenderer.Crossroad;
import mapRenderer.Street;
import mapRenderer.utils.Coord;

import java.util.Arrays;
import java.util.List;

public class CrossroadsController extends Stage {
    private ComboBox<Street> firstStreetBox = new ComboBox<>();
    private ComboBox<Street> secondStreetBox = new ComboBox<>();
    private TableView<Crossroad> crossroadTable = new TableView<>();
    private TableColumn<Crossroad, Street> firstColumn = new TableColumn<>("Ulica 1");
    private TableColumn<Crossroad, Street> secondColumn = new TableColumn<>("Ulica 2");
    private TableColumn<Crossroad, Coord> coordColumn = new TableColumn<>("Współrzędne");
    private Button addCrossroadBtn = new Button("Dodaj skrzyżowanie");
    private VBox content = new VBox(10, firstStreetBox, secondStreetBox, addCrossroadBtn, crossroadTable);
    private StreetsController streetsController;
    private MainWindowMapCreating mainWindow;

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
        crossroad.setFirstStreet(firstStreetBox.getValue());
        crossroad.setSecondStreet(secondStreetBox.getValue());
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
        buildTable();
        setAlwaysOnTop(true);
        Bindings.bindContent(firstStreetBox.getItems(), streetsController.getStreets());
        Bindings.bindContent(secondStreetBox.getItems(), streetsController.getStreets());
        Scene scene = new Scene(content);
        setScene(scene);
        setEvents();
        firstStreetBox.setMaxWidth(Double.MAX_VALUE);
        secondStreetBox.setMaxWidth(Double.MAX_VALUE);
    }

    private void buildTable() {
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("firstStreet"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("secondStreet"));
        coordColumn.setCellValueFactory(new PropertyValueFactory<>("coord"));
        crossroadTable.getColumns().setAll(Arrays.asList(firstColumn, secondColumn, coordColumn));
    }

    private void setEvents() {
        addCrossroadBtn.setOnAction(e -> {
            if(firstStreetBox.getValue() != null && secondStreetBox.getValue() != null) {
                mainWindow.getCanvasPane().setCursor(Cursor.CROSSHAIR);
            }
        });
        firstStreetBox.setOnAction(e -> drawHighlights());
        secondStreetBox.setOnAction(e -> drawHighlights());
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
    }

    public List<Crossroad> getCrossroads() {
        return crossroadTable.getItems();
    }

    public void addCrossroads(List<Crossroad> crossroads) {
        crossroads.forEach(this::addCrossroad);
    }
}

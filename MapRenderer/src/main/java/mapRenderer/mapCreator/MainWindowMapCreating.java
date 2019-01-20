package mapRenderer.mapCreator;

import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mapRenderer.Crossroad;
import mapRenderer.utils.Coord;
import mapRenderer.MapStartMenu;
import mapRenderer.Street;

import java.util.List;

public class MainWindowMapCreating extends Stage {
    private Canvas backgroundCanvas = new Canvas();
    private Canvas roadCanvas = new Canvas();
    private Canvas highlightCanvas = new Canvas();
    private Canvas carCanvas = new Canvas();
    private Group canvasPane = new Group(backgroundCanvas, roadCanvas, carCanvas, highlightCanvas);
    private Street currentStreet;
    private Scene scene;
    private Camera camera;
    private StreetsController streetsController;
    private CrossroadsController crossroadsController;

    public MainWindowMapCreating() {
        buildScene();
        buildCanvas();
        setWidth(1000);
        setHeight(500);
        streetsController = new StreetsController(this);

        crossroadsController = new CrossroadsController(streetsController, this);
        streetsController.show();


        setEvents();
    }

    private void setEvents() {
        canvasPane.setOnMouseDragged(this::drawCoordinates);
        canvasPane.setOnMouseClicked(e -> {
            if(canvasPane.getCursor().equals(Cursor.CROSSHAIR)) {
                crossroadsController.addCrossroad(new Coord(e.getX()-8, e.getY()-8));
                canvasPane.setCursor(Cursor.DEFAULT);
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, this::rescaleView);
        setOnCloseRequest(e -> handleClose());
    }

    private void handleClose() {
        new MapStartMenu().show();
        streetsController.close();
    }

    private void rescaleView(KeyEvent e) {
        if(e.getCode().equals(KeyCode.F1)){
            camera.setScaleX(camera.getScaleX() + 0.2);
            camera.setScaleY(camera.getScaleY() + 0.2);
        } else if(e.getCode().equals(KeyCode.F2)){
            camera.setScaleX(camera.getScaleX() - 0.2);
            camera.setScaleY(camera.getScaleY() - 0.2);
        }
    }

    private void drawCoordinates(MouseEvent e) {
        Circle circle = new Circle(3);
        circle.setFill(Color.GREEN);
        int x = (int)e.getX();
        int y = (int)e.getY();
        circle.setCenterX(x);
        circle.setCenterY(y);
        if(x > 0 && y > 0) {
            for (int xx = x - 3; xx < x + 3; xx++) {
                for (int yy = y - 3; yy < y + 3; yy++) {
                    if (xx % 6 == 0 && yy % 6 == 0) {
                        if (currentStreet != null) {
                            canvasPane.getChildren().add(circle);
                            circle.setUserData(currentStreet);
                            currentStreet.getCoords().add(new Coord(circle));
                        }
                        return;
                    }
                }
            }
        }
    }

    private void buildCanvas() {
        Image image = new Image("/mapa.png");
        backgroundCanvas.setHeight(image.getHeight());
        backgroundCanvas.setWidth(image.getWidth());
        roadCanvas.setHeight(image.getHeight());
        roadCanvas.setWidth(image.getWidth());
        carCanvas.setHeight(image.getHeight());
        carCanvas.setWidth(image.getWidth());
        highlightCanvas.setHeight(image.getHeight());
        highlightCanvas.setWidth(image.getWidth());
        GraphicsContext backgroundGC = backgroundCanvas.getGraphicsContext2D();
        backgroundGC.drawImage(image, 0, 0);

        /*GraphicsContext carGC = carCanvas.getGraphicsContext2D();
        carGC.setStroke(Color.RED);
        carGC.setFill(Color.RED);
        carGC.fillOval(1400,122, 10,10);*/

        GraphicsContext roadGC = roadCanvas.getGraphicsContext2D();
        roadGC.setStroke(Color.GREEN);
        roadGC.setFill(Color.GREEN);
        roadGC.setLineWidth(1);
    }

    private void buildScene() {
        VBox mainBox = new VBox(canvasPane);
        ScrollPane outerPane = new ScrollPane(mainBox);
        scene = new Scene(outerPane);
        setScene(scene);
        camera = new PerspectiveCamera();
        scene.setCamera(camera);
    }

    Group getCanvasPane() {
        return canvasPane;
    }

    void setCurrentStreet(Street street) {
        currentStreet = street;
    }

    public Canvas getHighlightCanvas() {
        return highlightCanvas;
    }

    public CrossroadsController getCrossroadsController() {
        return crossroadsController;
    }
}
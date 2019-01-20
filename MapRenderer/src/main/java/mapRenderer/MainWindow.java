package mapRenderer;

import javafx.application.Platform;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mapRenderer.utils.Coord;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class MainWindow extends Stage {
    private Canvas backgroundCanvas = new Canvas();
    private GraphicsContext backgroundGC = backgroundCanvas.getGraphicsContext2D();
    private Canvas roadCanvas = new Canvas();
    private GraphicsContext roadGC = roadCanvas.getGraphicsContext2D();
    private Canvas carCanvas = new Canvas();
    private GraphicsContext carGC = carCanvas.getGraphicsContext2D();
    private StackPane canvasPane = new StackPane(backgroundCanvas, roadCanvas, carCanvas);
    private VBox mainBox = new VBox(canvasPane);
    private ScrollPane outerPane = new ScrollPane(mainBox);
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private LinkedList<Future> tasks = new LinkedList<>();
    private MainWindow() {
        buildScene();
        buildCanvas();
        setEvents();
        setWidth(1000);
        setHeight(500);
    }


    //todo: artykuły o nagel-schreckenbergu np. w warunkach miejskich
    MainWindow(JsonData data) {
        this();
        build();
        run(data.getStreets());
    }


    private void buildCanvas() {
        Image image = new Image("/mapa.png");
        backgroundCanvas.setHeight(image.getHeight());
        backgroundCanvas.setWidth(image.getWidth());
        roadCanvas.setHeight(image.getHeight());
        roadCanvas.setWidth(image.getWidth());
        carCanvas.setHeight(image.getHeight());
        carCanvas.setWidth(image.getWidth());
        backgroundGC.drawImage(image, 0, 0);

        /*
        carGC.setStroke(Color.RED);
        carGC.setFill(Color.RED);
        carGC.fillOval(1400,122, 10,10);
        */
        roadGC.setStroke(Color.GREEN);
        roadGC.setFill(Color.GREEN);
        roadGC.setLineWidth(1);
    }




    private void setEvents() {
        setOnCloseRequest(e -> handleClose());
    }

    private void handleClose() {
        new MapStartMenu().show();
        executorService.shutdownNow();
        tasks.forEach(t -> t.cancel(true));
        close();
    }


    private void buildScene() {
        Scene scene = new Scene(outerPane);
        setScene(scene);
        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);
    }


    private void build() {
        roadGC.setStroke(Color.BLUE);
        roadGC.setLineWidth(3);
    }

    private void run(List<Street> streets) {

        // task zawiera 3 petle : główna, iterująca po drogach i iterująca po liscie samochodów sie na niej znajdujacych
        //1px ~= 2.5 m
        //todo: zaktualizować przelicznik prędkości(odleglosci miedzy pojazdami i do konca drogi)
        tasks.add(
                executorService.submit(() -> {
                    int spawnFlag = 5;//ile samochodow jest wygenerowanych
                    int iterations = 0;

                    while(true){
                        System.out.println("iteration: "+iterations);
                        for(Street street:streets){
                            if(street.isGenerator() && spawnFlag > 0 && iterations%30 == 0 ){
                                System.out.println("spawned");
                                street.generateCar(spawnFlag);
                                spawnFlag -= 1;
                            }

                            LinkedList<Coord> coords = new LinkedList<>(street.getCoords());

                            for(Vehicle vehicle: street.getVehiclesOnRoad()){
                                int index = coords.indexOf(vehicle.getPosition());
                                int nextIndex = index + (int)Coord.kmh2ms(vehicle.getSpeed());
                                if(nextIndex >= coords.size()){
                                    nextIndex = coords.size()-1;
                                }
                                System.out.println("coords from: "+index+" coords to: "+nextIndex+"id: "+vehicle.id+ "distance from next car: "+ vehicle.getNextVehicleDistance());
                                vehicle.setPosition(coords.get(nextIndex));

                                drawCircle(vehicle.getPosition());

                                if(vehicle.getPosition().equals(coords.getLast())){
                                    //todo obsługa dojechania do konca drogi(skrzyzowania)
                                }

                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignoredEx) {
                            return;
                        }
                        carGC.clearRect(0, 0, carCanvas.getWidth(), carCanvas.getHeight());
                        iterations+=1;
                    }
                })
        );




    }

    public void drawCircle(Coord coord){

        carGC.setStroke(Color.RED);
        carGC.setFill(Color.RED);
        carGC.fillOval(1400,122, 10,10);
        carGC.fillOval(coord.getX(), coord.getY(), 8, 8);
    }

}
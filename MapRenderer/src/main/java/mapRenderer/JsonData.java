package mapRenderer;

import java.util.LinkedList;
import java.util.List;

public class JsonData {
    private List<Street> streets = new LinkedList<>();
    private List<Crossroad> crossroads = new LinkedList<>();

    public List<Street> getStreets() {
        return streets;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public List<Crossroad> getCrossroads() {
        return crossroads;
    }

    public void setCrossroads(List<Crossroad> crossroads) {
        this.crossroads = crossroads;
    }
}

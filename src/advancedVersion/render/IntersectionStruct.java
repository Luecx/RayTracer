package advancedVersion.render;

import advancedVersion.entities.Element;

public class IntersectionStruct {

    private Element element;
    private double distance;

    public IntersectionStruct(Element element, double distance) {
        this.element = element;
        this.distance = distance;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

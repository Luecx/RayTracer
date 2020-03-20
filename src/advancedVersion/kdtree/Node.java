package advancedVersion.kdtree;

import advancedVersion.entities.Element;
import advancedVersion.entities.Triangle;
import advancedVersion.loading.Loader;
import advancedVersion.maths.Tools;
import advancedVersion.render.IntersectionStruct;
import advancedVersion.render.Ray;
import advancedVersion.render.Renderer;
import advancedVersion.scene.Light;
import advancedVersion.scene.Scene;
import advancedVersion.visual.Frame;
import core.vector.Vector3d;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

public class Node {

    private Vector3d min = new Vector3d(), max = new Vector3d(); //spans the min-corner, max-corner

    private Node subChild1, subChild2;
    private List<? extends Element> elementList;

    public Node(List<? extends Element> elementList){
        boundary(elementList, min, max);
        this.elementList = elementList;
        build();
    }

    private void boundary(List<? extends Element> elementList, Vector3d min, Vector3d max){
        elementList.get(0).getCenter().loadTo(min);
        elementList.get(0).getCenter().loadTo(max);
        for (Element e : elementList) {
            
            Vector3d eMin = e.min();
            Vector3d eMax = e.max();
            
            if (eMin.getX() < min.getX()) {
                min.setX(eMin.getX());
            }
            if (eMax.getX() > max.getX()) {
                max.setX(eMax.getX());
            }

            if (eMin.getY() < min.getY()) {
                min.setY(eMin.getY());
            }
            if (eMax.getY() > max.getY()) {
                max.setY(eMax.getY());
            }

            if (eMin.getZ() < min.getZ()) {
                min.setZ(eMin.getZ());
            }
            if (eMax.getZ() > max.getZ()) {
                max.setZ(eMax.getZ());
            }
        }
    }

    private void build(){

        if(elementList.size() <= 3){
            subChild1 = null;
            subChild2 = null;
        }else{
            Vector3d size = max.sub(min);
            int index = 0;
            for(int i = 1; i < 3; i++){
                if(size.getY() > size.getValue(index)){
                    index = i;
                }
            }
            int finalIndex = index;
            elementList.sort(Comparator.comparingDouble(o -> o.getCenter().getValue(finalIndex)));


            List<? extends Element> elements1 = elementList.subList(0, elementList.size()/2);
            List<? extends Element> elements2 = elementList.subList(elementList.size()/2, elementList.size());

            subChild1 = new Node(elements1);
            subChild2 = new Node(elements2);
        }


    }



    public IntersectionStruct intersection(Ray r){
        if(subChild1 != null && subChild2 != null){
            double i1 = AABB_intersection(subChild1.min, subChild1.max, r);
            double i2 = AABB_intersection(subChild2.min, subChild2.max, r);


            if(Double.isNaN(i1) && !Double.isNaN(i2)){
                return subChild2.intersection(r);
            }
            else if(!Double.isNaN(i1) && Double.isNaN(i2)){
                return subChild1.intersection(r);
            }
            else if(Double.isNaN(i1) && Double.isNaN(i2)){
                return null;
            }

            IntersectionStruct d1 = subChild1.intersection(r);
            IntersectionStruct d2 = subChild2.intersection(r);
            if(d1 == null && d2 == null) return null;
            if(d1 == null) return d2;
            if(d2 == null) return d1;
            if(d1.getDistance() <= d2.getDistance()) return d1;
            return d2;

//            else if(i1 < i2){
//                IntersectionStruct d = subChild1.intersection(r);
//                if(d == null){
//                    return subChild2.intersection(r);
//                }else{
//                    return d;
//                }
//            }
//            else{
//                IntersectionStruct d = subChild2.intersection(r);
//                if(d == null){
//                    return subChild1.intersection(r);
//                }else{
//                    return d;
//                }
//            }
        }else{
            double t_min = Double.POSITIVE_INFINITY;
            Element elem = null;
            for(Element element:elementList){
                double inter = element.intersection(r);
                if(!Double.isNaN(inter) && inter < t_min){
                    t_min = inter;
                    elem = element;
                }
            }
            if(t_min == Double.POSITIVE_INFINITY) return null;
            return new IntersectionStruct(elem, t_min);
        }

    }

    public static double max(double... vals){
        double max = vals[0];
        for(int i = 1; i < vals.length; i++){
            max = Math.max(max, vals[i]);
        }
        return max;
    }

    public static double min(double... vals){
        double max = vals[0];
        for(int i = 1; i < vals.length; i++){
            max = Math.min(max, vals[i]);
        }
        return max;
    }

    public static double AABB_intersection(Vector3d min, Vector3d max, Ray ray){
        
        Vector3d dirfrac = new Vector3d(
                1.0f / ray.getDirection().getX(),
                1.0f / ray.getDirection().getY(),
                1.0f / ray.getDirection().getZ()
        );
        
        
        double t1 = (min.getX() - ray.getRoot().getX())*dirfrac.getX();
        double t2 = (max.getX() - ray.getRoot().getX())*dirfrac.getX();
        double t3 = (min.getY() - ray.getRoot().getY())*dirfrac.getY();
        double t4 = (max.getY() - ray.getRoot().getY())*dirfrac.getY();
        double t5 = (min.getZ() - ray.getRoot().getZ())*dirfrac.getZ();
        double t6 = (max.getZ() - ray.getRoot().getZ())*dirfrac.getZ();

        double tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        double tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

        if (tmax < 0)
        {
            return Double.NaN;
        }

        if (tmin > tmax)
        {
            return Double.NaN;
        }

        return tmin;
    }

    public void print() {
        print(0);
    }

    private void print(int tabs){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i< tabs; i++){
            builder.append("\t");
        }

        builder.append( min + " " + max);
        if(subChild1 == null || subChild2 == null){
            System.out.println(builder + "  " + elementList.size());
        }else{
            System.out.println(builder);
            subChild1.print(tabs+1);
            subChild2.print(tabs+1);
        }



    }

    public static void main(String[] args) {
        List<Triangle> dragon = Loader.loadFromFile("A:\\OneDrive\\ProgrammSpeicher\\IntelliJ\\Raytracer\\res\\models\\dragon.obj");
        List<Triangle> floor = Loader.loadFromFile("A:\\OneDrive\\ProgrammSpeicher\\IntelliJ\\Raytracer\\res\\models\\Plane.obj");



//
//        triangles.add(new Triangle(new Vector3d(0,1,0), new Vector3d(0.5,0,0), new Vector3d(1,1,0),
//                                  new Vector3d(0,1,0), new Vector3d(1,0,0), new Vector3d(0,0,1)));
//        triangles.add(new Triangle(new Vector3d(1,1,0), new Vector3d(0.5,0,0), new Vector3d(1.5,0,0)));
//        triangles.add(new Triangle(new Vector3d(1,1,0), new Vector3d(1.5,0,0), new Vector3d(2,1,0)));
//        triangles.add(new Triangle(new Vector3d(2,1,0), new Vector3d(1.5,0,0), new Vector3d(2.5,0,0)));


        Scene scene = new Scene();
        scene.getTriangles().addAll(dragon);
        scene.getTriangles().addAll(floor);

        scene.getCamera().setPosition(new Vector3d(0,1,25));
        scene.getCamera().setRotation(new Vector3d(-Math.PI/8, 0.4,0));

        scene.getLights().add(new Light(new Vector3d(10,10,10), 0));

        //System.out.println(Tools.generateRandomRaysToDisc(new Vector3d(0,0,0), new Vector3d(10,10,10), 1,1));


        BufferedImage renderedTo = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB_PRE);
        new Frame().setImage(renderedTo);

        Renderer renderer = new Renderer();
        renderer.render(scene, renderedTo, 8);

    }
}

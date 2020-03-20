package advancedVersion.render;

import advancedVersion.kdtree.Node;
import advancedVersion.maths.Tools;
import advancedVersion.scene.Camera;
import advancedVersion.scene.Light;
import advancedVersion.scene.Scene;
import core.threads.Pool;
import core.threads.PoolFunction;
import core.vector.Vector3d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer {


    private int image_width = 720;
    private int image_height = 480;
    private double h_fov = Math.PI / 2;

    private int chunkSize = 64;

    private int lightSamples = 10;
    private int lightDistanceFactor = 10;


    private Scene _scene;
    private Node            _kdTree;
    private Raster          _raster;

    private BufferedImage   _target;





    public void render(Scene scene, BufferedImage renderTo, int cores){

        _scene = scene;
        _target = renderTo;

        image_width = renderTo.getWidth();
        image_height = renderTo.getHeight();

        Camera c = scene.getCamera();

        _raster = c.createRaster(image_width, image_height, h_fov);
        _kdTree = new Node(scene.getTriangles());



        Pool pool = new Pool(cores);
        PoolFunction function = (index, core) -> {
            renderChunk(index);
        };
        pool.executeSequential(function, chunksTotal(),false);
        pool.stop();



    }

    public void renderChunk(int chunkX, int chunkY){

        int maxX = Math.min((chunkX+1) * chunkSize, image_width);
        int maxY = Math.min((chunkY+1) * chunkSize, image_height);

        for(int i = chunkX * chunkSize; i < maxX; i++){
            for(int n = chunkY * chunkSize; n < maxY; n++){
                render(i,n);
            }
        }
    }

    public void renderChunk(int index){
        renderChunk(index % chunksX(), index / chunksX());
    }

    public int chunksX(){
        return image_width / chunkSize + (image_width % chunkSize != 0 ? 1:0);
    }

    public int chunksY(){
        return image_height / chunkSize + (image_height % chunkSize != 0 ? 1:0);
    }

    public int chunksTotal() {
        return chunksX() * chunksY();
    }




    public void render(int x, int y){
        Ray ray = _raster.getRays()[x][y];
        IntersectionStruct intersection = intersectionDistance(ray);
        if(intersection == null){
            _target.setRGB(x,y,new Color(1f,1f,1f).getRGB());
        }else{
            float f = (float)Math.max(0,1f-intersection.getDistance()/40f);
            Vector3d point = ray.getRoot().add(ray.getDirection().scale(intersection.getDistance()));
            Vector3d normal = intersection.getElement().getNormal(point);
            point.self_add(normal.self_normalise().scale(-1E-6));

            float red =     (float)Math.abs(normal.getX());
            float green =   (float)Math.abs(normal.getY());
            float blue =    (float)Math.abs(normal.getZ());

            f = (float)lightIntensity(point, normal);


            _target.setRGB(x,y,new Color(f,f,f).getRGB());
        }
    }

    public double lightIntensity(Vector3d position, Vector3d normal){
        double totalMax = 0;
        double totalActual = 0;
        for(Light light:_scene.getLights()){
            ArrayList<Ray> rays = Tools.generateRandomRaysToDisc(position, light, light.getRadius(), lightSamples);
            double dist = position.sub(light).length();
            double distanceValue = Math.exp(-dist / lightDistanceFactor);

            for(Ray r:rays){

                double directionFactor = Math.abs(normal.dot(r.getDirection()));


                totalMax += directionFactor * distanceValue;

                IntersectionStruct intersectionStruct = intersectionDistance(r);


                System.out.println(intersectionStruct);
                if(intersectionStruct != null){
                    System.out.println(intersectionStruct.getDistance());
                }

                if(intersectionStruct != null && intersectionStruct.getDistance() > dist) {
                    //System.out.println(position + "  " + intersectionStruct.getDistance());
                    return 1;
                }
                else{
                    return 0;
                }

//                if(intersectionStruct == null || intersectionStruct.getDistance() > dist){
//                    totalActual += directionFactor * distanceValue;
//                }
            }
        }
        //System.out.println( totalActual/totalMax);
        return totalActual/totalMax;
    }


    public IntersectionStruct intersectionDistance(Ray ray){
//        for(Triangle t:_scene.getTriangles()){
//            if(!Double.isNaN(t.intersection(ray))){
//                return t.intersection(ray);
//            }
//        }
//        return Double.NaN;
        return _kdTree.intersection(ray);
    }

}

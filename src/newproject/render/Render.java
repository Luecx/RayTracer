package newproject.render;

import newproject.math.calculus.kdtree.KDTree;
import newproject.math.calculus.kdtree.TriangleAndVector;
import newproject.math.ray.Ray;
import newproject.math.ressource.components.Light;
import newproject.math.ressource.components.PerspectiveCamera;
import newproject.math.ressource.entities.Entity;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Luecx on 19.08.2017.
 */
public class Render {

    class RenderThread extends Thread{

        private Render render;

        private Vector2f start;
        private int size;

        public RenderThread(Render render, Vector2f start, int size) {
            super();
            this.render = render;
            this.start = start;
            this.size = size;
            this.start();
        }

        private Ray task;

        public void run() {
            try{
                while(this.isInterrupted() == false && start != null && size != 0) {
                }
                this.interrupt();
            }catch (Exception e) {
                this.interrupt();
            }
        }
    }


    public int RESOLUTION_WIDTH = 1920;
    public int RESOLUTION_HEIGHT = 1080;
    public int ALIASING_LEVEL = 1;
    public int THREAD_RENDER_SIZE = 32;

    public boolean SHADOWS = true;
    public float SHADOW_SOFTNESS = 0.3f;
    public float SHADOW_DETAIL = 3;

    public volatile ArrayList<Entity> entities = new ArrayList<>();
    public volatile ArrayList<Light> lights = new ArrayList<>();
    public volatile PerspectiveCamera camera = new PerspectiveCamera();

    private RenderThread[] renderThreads;

    private void startThreads(int amount){
        renderThreads = new RenderThread[amount];
        currentRenderIndex = 0;

        for(int i = 0; i< amount; i++) {
            renderThreads[i] = new RenderThread(this, nextRenderIndex(), THREAD_RENDER_SIZE);
        }
    }

    private int currentRenderIndex = 0;
    Vector2f nextRenderIndex(){
        Vector2f vec = new Vector2f((currentRenderIndex * THREAD_RENDER_SIZE) % (RESOLUTION_WIDTH), (currentRenderIndex * THREAD_RENDER_SIZE) / (RESOLUTION_HEIGHT));
        if(vec.x >= this.RESOLUTION_WIDTH || vec.y >= this.RESOLUTION_HEIGHT) return null;
        return vec;
    }

    public static void main(String[] args) {

        Render render = new Render();

        render.camera.setPosition(0,7,30);
        render.camera.setFOV(80);

        Entity e1 = Entity.loadFromFile("res/models/Plane.obj");
        Entity e = Entity.loadFromFile("res/models/cube.obj");
        render.entities.add(e1);
        render.entities.add(e);

        render.lights.add(new Light(20,10,10));
        render.lights.add(new Light(-20,10,10));

        RenderFrame frame = new RenderFrame();
        frame.setVisible(true);

        RenderFrame.image = render.render();

        frame.repaint();
    }

    private TriangleAndVector intersect(Ray r,KDTree[] kdTrees) {
        TriangleAndVector res = null;
        for(int i = 0; i < entities.size(); i++) {
            if(entities.get(i).intersectable(r)) {
                if(kdTrees[i] == null) {
                    kdTrees[i] = new KDTree(entities.get(i),10);
                }
                TriangleAndVector cur = kdTrees[i].intersection(r);
                if(cur != null) {
                    if(res == null){
                        res = cur;
                    }else{
                        if(Vector3f.sub(r.getOrigin(), res.getVector(), null).length() > Vector3f.sub(r.getOrigin(), cur.getVector(), null).length()){
                            res = cur;
                        }
                    }
                }

            }
        }
        return res;
    }

    public float shadowCalculations(Vector3f point, Light l, KDTree[] trees) {
        Vector3f dir = Vector3f.sub(l.getAbsolutePosition(),point,null).normalise(null);

        float per = 0;

        if(SHADOW_SOFTNESS > 0){
            Vector3f ver = new Vector3f(dir.y, -dir.x, 0);
            Vector3f hor = Vector3f.cross(dir, ver, null);
            float start = -(SHADOW_DETAIL-1)/2 * SHADOW_SOFTNESS;
            for(float i = 0; i < SHADOW_DETAIL; i ++) {
                for(float n = 0; n < SHADOW_DETAIL; n ++) {
                    Vector2f var = new Vector2f(start + i * SHADOW_SOFTNESS, start + n * SHADOW_SOFTNESS);
                    Vector3f target = new Vector3f(
                            l.getPosition().x + var.x * ver.x + var.y * hor.x,
                            l.getPosition().y + var.x * ver.y + var.y * hor.y,
                            l.getPosition().z + var.x * ver.z + var.y * hor.z);
                    dir = Vector3f.sub(target, point, null);
                    Ray r = new Ray(new Vector3f(point), dir);
                    TriangleAndVector t = intersect(r,trees);
                    if(t == null) {
                        per ++;
                    }else{
                        if(Vector3f.sub(t.getVector(), point, null).length() >  Vector3f.sub(target, point, null).length()){
                            per++;
                        }
                    }
                }
            }
        }else{
            Ray r = new Ray(new Vector3f(point), dir);
            TriangleAndVector t = intersect(r,trees);
            if(t == null) {
                return 1;
            }else{
                if(Vector3f.sub(t.getVector(), point, null).length() >  Vector3f.sub(l.getAbsolutePosition(), point, null).length()){
                   return 1;
                }
            }
        }
        return per / (SHADOW_DETAIL * SHADOW_DETAIL);
    }

    Ray getRay(int x, int y) {
        float HOR_FOV = 2 * (float)Math.tan(Math.toRadians(camera.getFOV() / 2));
        float VER_FOV = HOR_FOV * (float)RESOLUTION_HEIGHT / (float)RESOLUTION_WIDTH;

        float HOR_FOV_INC = HOR_FOV / ((float)RESOLUTION_WIDTH * (float)ALIASING_LEVEL);
        float VER_FOV_INC = VER_FOV / ((float)RESOLUTION_HEIGHT * (float)ALIASING_LEVEL);

        Vector4f dir = new Vector4f(x * HOR_FOV_INC - HOR_FOV / 2,-y * VER_FOV_INC + VER_FOV / 2,-1,0);
        Matrix4f.transform(camera.getAbsoluteTransformationMatrix(), dir, dir);
        Ray r = new Ray(camera.getAbsolutePosition(), new Vector3f(dir.x,dir.y,dir.z));
        return r;
    }

    public BufferedImage render() {




        BufferedImage rendered = new BufferedImage(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, BufferedImage.TYPE_INT_RGB);

        Vector4f[][] buffer = new Vector4f[RESOLUTION_WIDTH * ALIASING_LEVEL][RESOLUTION_HEIGHT * ALIASING_LEVEL];

        KDTree[] trees = new KDTree[entities.size()];

        for(int w = 0; w < RESOLUTION_WIDTH * ALIASING_LEVEL; w++) {
            for(int h = 0; h < RESOLUTION_HEIGHT * ALIASING_LEVEL; h ++) {
                if(h == 0) {
                    System.out.println(new String((float)(100 * w * RESOLUTION_HEIGHT * ALIASING_LEVEL + h) / ((float)RESOLUTION_WIDTH * RESOLUTION_HEIGHT * ALIASING_LEVEL* ALIASING_LEVEL)+ "     ").substring(0,4) + " %");
                }



                //################################################################################################
                //################################################################################################
                //################################################################################################



                TriangleAndVector intersect = intersect(getRay(w, h), trees);




                if(intersect != null) {
                    Vector3f light = new Vector3f();
                    for(Light l:lights) {
                        float var = Math.max(0.2f,Vector3f.dot(intersect.getTriangle().interpolateNormal(intersect.getVector()), (Vector3f)Vector3f.sub(l.getPosition(), intersect.getVector(), null).normalise()));
                        if(SHADOWS) {
                            var*=shadowCalculations(intersect.getVector(), l, trees);
                        }
                        Vector3f.add(light, (Vector3f)new Vector3f(l.getColor()).scale(var),light);
                    }

                    if(light.length() > 1) light.normalise(null);
                    light.x = Math.min(light.x, 1.0f);
                    light.y = Math.min(light.y, 1.0f);
                    light.z = Math.min(light.z, 1.0f);

                    buffer[w][h] = new Vector4f(light.x / 2 + 0.5f,light.y/ 2 + 0.5f,light.z/ 2 + 0.5f,1);
                }






                //################################################################################################
                //################################################################################################
                //################################################################################################

            }
        }

        for(int w = 0; w < buffer.length; w+= ALIASING_LEVEL) {
            for(int h = 0; h < buffer[0].length; h += ALIASING_LEVEL) {
                Vector4f total = new Vector4f();
                for(int i = 0; i< ALIASING_LEVEL; i++) {
                    for(int n = 0; n< ALIASING_LEVEL; n++) {

                        if(buffer[w + i][h + n] != null) {
                            total.add(total, buffer[w + i][h + n], total);
                        }
                    }
                }

                total.scale(1 / (float)(ALIASING_LEVEL * ALIASING_LEVEL));
                rendered.setRGB(w / ALIASING_LEVEL,h / ALIASING_LEVEL, new Color(total.x, total.y, total.z, total.w).getRGB());
            }
        }
        return rendered;
    }

}

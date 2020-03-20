package newproject.render.newrenderer;

import newproject.math.calculus.kdtree.KDTree;
import newproject.math.calculus.kdtree.TriangleAndVector;
import newproject.math.ray.Ray;
import newproject.math.ressource.components.Light;
import newproject.math.ressource.components.PerspectiveCamera;
import newproject.math.ressource.entities.Entity;
import newproject.math.ressource.entities.Material;
import newproject.math.ressource.entities.Texture;
import newproject.render.newrenderer.frames.RenderFrame;
import newproject.render.newrenderer.frames.TimeFrame;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Luecx on 22.08.2017.
 */
public abstract class Renderer {

    public int RESOLUTION_WIDTH = 1920;
    public int RESOLUTION_HEIGHT = 1080;
    public int ALIASING_LEVEL = 1;
    public int THREAD_RENDER_SIZE = 64;
    public int THREAD_AMOUNT = 8;
    public int KD_TREE_DEPTH = 25;

    public boolean SHADOWS = true;
    public float SHADOW_SOFTNESS = 0.8f;
    public float SHADOW_DETAIL = 4;

    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Light> lights = new ArrayList<>();
    public PerspectiveCamera camera = new PerspectiveCamera();

    private float HOR_FOV;
    private float VER_FOV;

    private float HOR_FOV_INC;
    private float VER_FOV_INC;

    private int chunkIndex = 0;
    private BufferedImage bufferedImage;

    private void createTrees() {
        for (Entity e : entities) {
            e.setKdTree(new KDTree(e, KD_TREE_DEPTH));
        }
    }

    private void createBasicInformation() {
        HOR_FOV = 2 * (float) Math.tan(Math.toRadians(camera.getFOV() / 2));
        VER_FOV = HOR_FOV * (float) RESOLUTION_HEIGHT / (float) RESOLUTION_WIDTH;

        HOR_FOV_INC = HOR_FOV / ((float) RESOLUTION_WIDTH * (float) ALIASING_LEVEL);
        VER_FOV_INC = VER_FOV / ((float) RESOLUTION_HEIGHT * (float) ALIASING_LEVEL);

        chunkIndex = 0;
    }

    public static void main(String[] args) {


        RenderFrame frame = new RenderFrame();
        frame.setVisible(true);

        TimeFrame time = new TimeFrame();
        time.setVisible(true);

        Renderer renderer = new Renderer() {
            @Override
            public void renderFinished() {
                time.stopTime();
            }
        };

        renderer.camera.setPosition(0, 7, 30);
        renderer.camera.setFOV(80);


        Entity e1 = Entity.loadFromFile("res/models/Plane.obj");
        Entity e = Entity.loadFromFile("res/models/stuetze.obj");

        renderer.entities.add(e1);
        renderer.entities.add(e);

        renderer.lights.add(new Light(-20, 4, 10));


        renderer.render();
        frame.image = renderer.bufferedImage;

        frame.repaint();
    }

    public void render() {
        createTrees();
        createBasicInformation();
        createBufferedImage();

        startThreads();
    }

    private RenderThread[] threads;

    private void startThreads() {
        threads = new RenderThread[THREAD_AMOUNT];
        for (int i = 0; i < THREAD_AMOUNT; i++) {
            threads[i] = new RenderThread(this, i);
        }
    }

    protected void stopThreads() {
        System.out.println(",,,,,,,,,,,");
        for (int i = 0; i < THREAD_AMOUNT; i++) {
            threads[i].interrupt();
        }
    }

    protected void threadFinished(int index) {
        boolean v = true;
        threads[index].interrupt();
        threads[index] = null;
        for (RenderThread t : threads) {
            if (t != null) {
                v = false;
            }
        }
        if (v) renderFinished();
    }

    public abstract void renderFinished();

    private void createBufferedImage() {
        this.bufferedImage = new BufferedImage(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    private Ray castRay(int w, int h) {
        Vector4f dir = new Vector4f(w * HOR_FOV_INC - HOR_FOV / 2, -h * VER_FOV_INC + VER_FOV / 2, -1, 0);
        Matrix4f.transform(camera.getAbsoluteTransformationMatrix(), dir, dir);
        Ray r = new Ray(camera.getAbsolutePosition(), new Vector3f(dir.x, dir.y, dir.z));
        return r;
    }

    private Vector3f raycast(Ray r, int depth) {
        TriangleAndVector intersect = intersect(r);
        if (intersect != null) {
            Vector3f light = this.lightCalc(intersect);
            Vector3f texture = intersect.getTriangle().interpolateTexture(intersect.getVector());

            return new Vector3f(light.x * texture.x, light.y * texture.y, light.z * texture.z);
            //Vector3f mix = intersect.getTriangle().getMaterial().getMixValues();
            //return intersect.getTriangle().interpolateTexture(intersect.getVector());
        }
        return null;
    }

    private TriangleAndVector intersect(Ray r) {
        TriangleAndVector res = null;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).intersectable(r)) {
                TriangleAndVector cur = entities.get(i).getKdTree().intersection(r);
                if (cur != null) {
                    if (res == null) {
                        res = cur;
                    } else {
                        if (Vector3f.sub(r.getOrigin(), res.getVector(), null).length() > Vector3f.sub(r.getOrigin(), cur.getVector(), null).length()) {
                            res = cur;
                        }
                    }
                }

            }
        }
        return res;
    }

    private boolean intersect(Ray r, Vector3f target) {
        TriangleAndVector res = null;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).intersectable(r)) {
                boolean v = entities.get(i).getKdTree().intersection(r, target);
                if (v) return true;
            }
        }
        return false;
    }

    public Vector2f getChunkBounds(int chunkIndex) {
        Vector2f v = new Vector2f(
                (this.chunkIndex * this.THREAD_RENDER_SIZE) % RESOLUTION_WIDTH,
                THREAD_RENDER_SIZE * (int) ((chunkIndex * this.THREAD_RENDER_SIZE) / RESOLUTION_WIDTH));
        if (v.x >= RESOLUTION_WIDTH || v.y >= RESOLUTION_HEIGHT) return null;
        return v;
    }

    public synchronized int getNextIndex() {
        return chunkIndex++;
    }

    boolean renderChunk(int chunkIndex) {
        Vector2f chunk = getChunkBounds(chunkIndex);
        if (chunk == null) return false;

        for (int i = (int) chunk.x * ALIASING_LEVEL; i < Math.min((chunk.x + THREAD_RENDER_SIZE) * ALIASING_LEVEL, RESOLUTION_WIDTH * ALIASING_LEVEL); i += ALIASING_LEVEL) {
            for (int n = (int) chunk.y * ALIASING_LEVEL; n < Math.min((chunk.y + THREAD_RENDER_SIZE) * ALIASING_LEVEL, RESOLUTION_HEIGHT * ALIASING_LEVEL); n += ALIASING_LEVEL) {

                Vector3f t = new Vector3f();

                for (int j = i; j < i + ALIASING_LEVEL; j++) {
                    for (int k = n; k < n + ALIASING_LEVEL; k++) {
                        Vector3f v = raycast(castRay(j, k), 1);
                        if (v != null) {
                            Vector3f.add(t, v, t);
                        }
                    }
                }

                t.scale(1f / (ALIASING_LEVEL * ALIASING_LEVEL));
                setImagePixel((int) (i / ALIASING_LEVEL), (int) (n / ALIASING_LEVEL), t);
            }
        }
        return true;
    }

    private void setImagePixel(int x, int y, Vector3f vec) {
        bufferedImage.setRGB(x, y, new Color(vec.x, vec.y, vec.z, 1.0f).getRGB());
    }

    private Vector3f lightCalc(TriangleAndVector intersect) {
        Vector3f light = new Vector3f();
        for (Light l : lights) {
            float var = randomOffset(0) + Vector3f.dot(intersect.getTriangle().interpolateNormal(intersect.getVector()), (Vector3f) Vector3f.sub(l.getPosition(), intersect.getVector(), null).normalise());
            if (SHADOWS) {
                var *= Math.max(0.2f, shadowCalc(intersect.getVector(), l));
            }
            Vector3f.add(light, (Vector3f) new Vector3f(l.getColor()).scale(var), light);
        }
        light.x = clamp(0, 1, light.x);
        light.y = clamp(0, 1, light.y);
        light.z = clamp(0, 1, light.z);

        return light;
    }

    private float clamp(float lower, float upper, float current) {
        return Math.min(upper, Math.max(lower, current));
    }

    private float shadowCalc(Vector3f point, Light l) {
        Vector3f dir = Vector3f.sub(l.getAbsolutePosition(), point, null).normalise(null);

        float per = 0;

        if (SHADOW_SOFTNESS > 0) {
            Vector3f ver = new Vector3f(dir.y, -dir.x, 0);
            Vector3f hor = Vector3f.cross(dir, ver, null);
            float start = -(SHADOW_DETAIL - 1) / 2 * SHADOW_SOFTNESS;
            for (float i = 0; i < SHADOW_DETAIL; i++) {
                for (float n = 0; n < SHADOW_DETAIL; n++) {
                    Vector2f var = new Vector2f(start + (i + randomOffset(1f / SHADOW_SOFTNESS)) * SHADOW_SOFTNESS, start + (n + randomOffset(1f / SHADOW_SOFTNESS)) * SHADOW_SOFTNESS);
                    Vector3f target = new Vector3f(
                            l.getPosition().x + var.x * ver.x + var.y * hor.x,
                            l.getPosition().y + var.x * ver.y + var.y * hor.y,
                            l.getPosition().z + var.x * ver.z + var.y * hor.z);
                    dir = Vector3f.sub(target, point, null);
                    Ray r = new Ray(new Vector3f(point), dir);
                    if (!intersect(r, target)) {
                        per++;
                    }
                }
            }
        } else {
            Ray r = new Ray(new Vector3f(point), dir);
            TriangleAndVector t = intersect(r);
            if (t == null) {
                return 1;
            } else {
                if (Vector3f.sub(t.getVector(), point, null).length() > Vector3f.sub(l.getAbsolutePosition(), point, null).length()) {
                    return 1;
                }
            }
        }
        return per / (SHADOW_DETAIL * SHADOW_DETAIL);
    }

    private float randomOffset(float offset) {
        return (float) (Math.random() * offset - offset / 2);
    }
}

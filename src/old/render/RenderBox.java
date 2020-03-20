package old.render;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import old.ressource.entities.Entity;
import old.ressource.entities.Triangle;
import old.ressource.entities.Vertex;
import old.ressource.maths.Maths;
import old.ressource.maths.Ray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Luecx on 18.08.2017.
 */
public class RenderBox {

    private static Vector3f dimension;
    private static Vector3f center;

    public static int subdivisions;

    private static RenderChunk[][][] chunks;

    public static void create(ArrayList<Entity> entities,int subdivisions) {
        Vector3f max = new Vector3f(-Float.MAX_VALUE,-Float.MAX_VALUE,-Float.MAX_VALUE);
        Vector3f min = new Vector3f(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
        for(Entity e:entities) {
            max.x = Math.max(max.x,e.getHitbox().getPosition().x + e.getHitbox().getDirection().length());
            max.y = Math.max(max.y,e.getHitbox().getPosition().y + e.getHitbox().getDirection().length());
            max.z = Math.max(max.z,e.getHitbox().getPosition().z + e.getHitbox().getDirection().length());

            min.x = Math.min(min.x,e.getHitbox().getPosition().x - e.getHitbox().getDirection().length());
            min.y = Math.min(min.y,e.getHitbox().getPosition().y - e.getHitbox().getDirection().length());
            min.z = Math.min(min.z,e.getHitbox().getPosition().z - e.getHitbox().getDirection().length());
        }
        dimension = new Vector3f(max.x - min.x, max.y - min.y, max.z - min.z);
        center = new Vector3f(max.x - dimension.x / 2, max.y - dimension.y / 2, max.z - dimension.z / 2);
        int realSubdiv = (int)Math.pow(2, subdivisions);
        chunks = new RenderChunk[realSubdiv][realSubdiv][realSubdiv];
        for(int i = 0; i < realSubdiv; i++) {
            for(int j = 0; j < realSubdiv;j++) {
                for(int k = 0; k < realSubdiv; k++) {
                    chunks[i][j][k] = new RenderChunk(new ArrayList<Triangle>());
                }
            }
        }
        RenderBox.subdivisions = realSubdiv;
        for(Entity e:entities) {
            for(Triangle t:e.getTriangles()) {
                t.setProcessed(false);
                Vector3f chunk1 = getChunk(t.getV1().getPosition());
                Vector3f chunk2 = getChunk(t.getV2().getPosition());
                Vector3f chunk3 = getChunk(t.getV3().getPosition());
                for(int i = (int)Math.min(Math.min(chunk1.x,chunk2.x), chunk3.x); i < Math.max(Math.max(chunk1.x,chunk2.x), chunk3.x); i++) {
                    for(int j = (int)Math.min(Math.min(chunk1.y,chunk2.y), chunk3.y); j < Math.max(Math.max(chunk1.y,chunk2.y), chunk3.y); j++) {
                        for(int k = (int)Math.min(Math.min(chunk1.z,chunk2.z), chunk3.z); k < Math.max(Math.max(chunk1.z,chunk2.z), chunk3.z); k++) {
                            chunks[i][j][k].getTriangles().add(t);
                        }
                    }  
                }
            }
        }

    }

    public static ArrayList<Triangle> possibleIntersections(Ray ray) {
        if(chunks == null) return null;
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<RenderChunk> chunks = rayChunks(ray);
        return null;
    }

    public static void main(String[] args) {
        ArrayList<Entity> entities = new ArrayList<>();
        Triangle t = new Triangle(new Vertex(new Vector3f(10,10,10), new Vector2f(), new Vector3f()),
                new Vertex(new Vector3f(-10,-10,-10), new Vector2f(), new Vector3f()),
                new Vertex(new Vector3f(10,-10,10), new Vector2f(), new Vector3f()));
        ArrayList<Triangle> triangles = new ArrayList<>();
        triangles.add(t);
        entities.add(new Entity(triangles, new Vector3f(0,0,0), new Vector3f(10,10,10)));
        RenderBox.create(entities, 4);
        Ray r = new Ray(new Vector3f(-100,0,0), new Vector3f(1,0,0));
        System.out.println(RenderBox.rayChunks(r).size());
        System.out.println(Arrays.toString(Maths.intersectRayBoundingBoxInAndOut(r, -17.3200509f,17.3200509f,-17.3200509f,17.3200509f,-17.3200509f,17.3200509f)));
        System.out.println(RenderBox.center+ "    " + RenderBox.dimension);
    }

    public static ArrayList<RenderChunk> rayChunks(Ray ray) {
        ArrayList<RenderChunk> chunks = new ArrayList<>();
        Vector3f current = getChunk(ray.getPosition());
        Ray r = ray;
        if(current == null) {
            float[] inter = Maths.intersectRayBoundingBoxInAndOut(ray,
                    center.x - dimension.x / 2,center.x + dimension.x / 2,
                    center.y - dimension.y / 2,center.y + dimension.y / 2,
                    center.z - dimension.z / 2,center.z + dimension.z / 2);
            if(inter == null || inter[0] < 0 || inter[1] < 0) return chunks;
            r = new Ray(ray.getLocation(Math.min(inter[0], inter[1]) + 0.0000001f), ray.getDirection());
        }
        current = r.getPosition();
        while(current != null) {
            chunks.add(RenderBox.chunks[(int)current.x][(int)current.y][(int)current.z]);
            float[] inter = Maths.intersectRayBoundingBoxInAndOut(ray,
                    center.x - dimension.x / 2 + current.x * dimension.x / subdivisions,center.x + dimension.x / 2 + (current.x + 1) * dimension.x / subdivisions,
                    center.y - dimension.y / 2 + current.y * dimension.y / subdivisions,center.y + dimension.y / 2 + (current.y + 1) * dimension.y / subdivisions,
                    center.z - dimension.z / 2 + current.z * dimension.z / subdivisions,center.z + dimension.z / 2 + (current.z + 1) * dimension.z / subdivisions);
            if(inter[0] > inter[1] && inter[0] > 0) {
                current = getChunk(r.getLocation(inter[0]));
            }else if(inter[1] > inter[0] && inter[1] > 0) {
                current = getChunk(r.getLocation(inter[1]));
            }else{
                break;
            }
        }
        return chunks;
    }

    public static Vector3f getChunk(Vector3f position) {
        Vector3f res = new Vector3f(subdivisions * (position.x - (center.x - dimension.x / 2)) / dimension.x,
                subdivisions * (position.y - (center.y - dimension.y / 2)) / dimension.y,
                subdivisions * (position.z - (center.z - dimension.z / 2)) / dimension.z);
        if(res.x < 0 || res.x >= subdivisions || res.y < 0 || res.y >= subdivisions || res.z < 0 || res.z >= subdivisions){
            return null;
        }
        return res;
    }



}

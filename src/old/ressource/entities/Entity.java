package old.ressource.entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import old.ressource.maths.Ray;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Luecx on 17.08.2017.
 */
public class Entity {

    private ArrayList<Triangle> triangles;
    private Ray hitbox;

    public Entity(ArrayList<Triangle> triangles, Vector3f center, Vector3f furthestPoint) {
        this.triangles = triangles;
        this.hitbox = new Ray(center, furthestPoint);
    }

    public Ray getHitbox() {
        return hitbox;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public static void main(String[] args) {
        String s = "f 1/2/3 1/2/3 1/2/3";
        System.out.println(s.substring(2,s.length()));
    }

    public static Entity loadFromFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            String s;
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector2f> texCoords = new ArrayList<>();
            ArrayList<Vector3f> normals = new ArrayList<>();

            Vector3f center = new Vector3f();

            ArrayList<Triangle> triangles = new ArrayList<>();

            while((s = reader.readLine()) != null) {
                s.replace("  ", " ");
                String[] values = s.split(" ");
                if(values[0].equals("v")){
                    vertices.add(new Vector3f(Float.parseFloat(values[1]),Float.parseFloat(values[2]),Float.parseFloat(values[3])));
                    Vector3f.add(center, vertices.get(vertices.size()-1), center);
                }
                if(values[0].equals("vt")){
                    texCoords.add(new Vector2f(Float.parseFloat(values[1]),Float.parseFloat(values[2])));
                }
                if(values[0].equals("vn")){
                    normals.add(new Vector3f(Float.parseFloat(values[1]),Float.parseFloat(values[2]),Float.parseFloat(values[3])));
                }
                if(values[0].equals("f")){
                    String h = s.substring(2,s.length());
                    h = h.replaceAll(" ", "/");
                    String[] val = h.split("/");
                    Triangle t = new Triangle(
                            new Vertex(vertices.get(Integer.parseInt(val[0])-1),texCoords.get(Integer.parseInt(val[1])-1),normals.get(Integer.parseInt(val[2])-1)),
                            new Vertex(vertices.get(Integer.parseInt(val[3])-1),texCoords.get(Integer.parseInt(val[4])-1),normals.get(Integer.parseInt(val[5])-1)),
                            new Vertex(vertices.get(Integer.parseInt(val[6])-1),texCoords.get(Integer.parseInt(val[7])-1),normals.get(Integer.parseInt(val[8])-1)));
                    triangles.add(t);
                }
            }
            center.scale(1f / vertices.size());
            Vector3f furthest = new Vector3f(center);
            for(Vector3f vec:vertices) {
                if(Vector3f.sub(center, furthest, null).length() < Vector3f.sub(center, vec, null).length()){
                    furthest = vec;
                }
            }
            return new Entity(triangles, center, furthest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){

        }
        return null;
    }

}

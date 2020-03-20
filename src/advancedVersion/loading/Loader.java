package advancedVersion.loading;

import advancedVersion.entities.Triangle;
import core.vector.Vector3d;

import java.io.*;
import java.util.ArrayList;

public class Loader {

    public static ArrayList<Triangle> loadFromFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            String s;
            ArrayList<Vector3d> vertices = new ArrayList<>();
            //ArrayList<Vector2f> texCoords = new ArrayList<>();
            ArrayList<Vector3d> normals = new ArrayList<>();

            Vector3d center = new Vector3d();

            ArrayList<Triangle> triangles = new ArrayList<>();

            while((s = reader.readLine()) != null) {
                s.replace("  ", " ");
                String[] values = s.split(" ");
                if(values[0].equals("v")){
                    vertices.add(new Vector3d(Float.parseFloat(values[1]),Float.parseFloat(values[2]),Float.parseFloat(values[3])));
                    //Vector3d.add(center, vertices.get(vertices.size()-1), center);
                }
//                if(values[0].equals("vt")){
//                    texCoords.add(new Vector2f(Float.parseFloat(values[1]),Float.parseFloat(values[2])));
//                }
                if(values[0].equals("vn")){
                    normals.add(new Vector3d(Float.parseFloat(values[1]),Float.parseFloat(values[2]),Float.parseFloat(values[3])));
                }
                if(values[0].equals("f")){
                    String h = s.substring(2,s.length());
                    h = h.replaceAll(" ", "/");
                    String[] val = h.split("/");
                    Triangle t = new Triangle(
                            vertices.get(Integer.parseInt(val[0])-1),
                            vertices.get(Integer.parseInt(val[3])-1),
                            vertices.get(Integer.parseInt(val[6])-1),
                            normals.get(Integer.parseInt(val[2])-1),
                            normals.get(Integer.parseInt(val[5])-1),
                            normals.get(Integer.parseInt(val[8])-1)
                    );

//                            new Vertex(vertices.get(Integer.parseInt(val[0])-1),texCoords.get(Integer.parseInt(val[1])-1),normals.get(Integer.parseInt(val[2])-1)),
//                            new Vertex(vertices.get(Integer.parseInt(val[3])-1),texCoords.get(Integer.parseInt(val[4])-1),normals.get(Integer.parseInt(val[5])-1)),
//                            new Vertex(vertices.get(Integer.parseInt(val[6])-1),texCoords.get(Integer.parseInt(val[7])-1),normals.get(Integer.parseInt(val[8])-1)));
                    triangles.add(t);
                }
            }

            return triangles;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){

        }
        return null;
    }

}

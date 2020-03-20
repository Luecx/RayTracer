package newproject.math.calculus.kdtree;

import newproject.math.ray.Ray;
import newproject.math.ressource.entities.Entity;
import newproject.math.ressource.entities.Triangle;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Created by Luecx on 19.08.2017.
 */
public class KDTree {

    private volatile KDNode node;

    public TriangleAndVector intersection(Ray ray) {

        ArrayList<KDNode> nodes = new ArrayList<>();
        node.intersectedBoxes(nodes, ray);

        TriangleAndVector res = null;

        for(KDNode n:nodes) {
            for(TriangleAndVector t:n.getTriangles()) {
                Vector3f intersect = t.getTriangle().intersectionPoint(ray);
                if(intersect != null) {
                    if(res == null) {
                        res = new TriangleAndVector(t.getTriangle(), intersect);
                    }else{
                        if(Vector3f.sub(ray.getOrigin(), res.getVector(), null).length() > Vector3f.sub(ray.getOrigin(), intersect, null).length()){
                            res = new TriangleAndVector(t.getTriangle(), intersect);
                        }
                    }
                }
            }
        }
        return res;
    }

    public boolean intersection(Ray ray, float maxDepth) {
        ArrayList<KDNode> nodes = new ArrayList<>();
        node.intersectedBoxes(nodes, ray);

        for(KDNode n:nodes) {
            for(TriangleAndVector t:n.getTriangles()) {
                Vector3f intersect = t.getTriangle().intersectionPoint(ray);
                if(intersect != null) {
                    if(Vector3f.sub(intersect, ray.getOrigin(), null).length() < maxDepth) return true;
                }
            }
        }

        return false;
    }

    public boolean intersection(Ray ray, Vector3f maxPoint) {
        return intersection(ray, Vector3f.sub(maxPoint, ray.getOrigin(), null).length());
    }

    public void print(){
        this.node.print();
    }


    public KDTree(Entity entity, int subdivisions) {
        this.node = new KDNode(entity.getOrigin(), new Vector3f(entity.getRadius() * 2, entity.getRadius() * 2, entity.getRadius() * 2));
        ArrayList<TriangleAndVector> triangleAndVectors = new ArrayList<>();
        for(Triangle t:entity.getTriangles()) {
            triangleAndVectors.add(new TriangleAndVector(t));
        }
        node.setTriangles(triangleAndVectors);
        node.passDown(subdivisions);
    }

    public KDNode getNode() {
        return node;
    }

}

package newproject.math.calculus.kdtree;

import newproject.math.calculus.QuickSort;
import newproject.math.primitives.AABB;
import newproject.math.ray.Ray;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Created by Luecx on 19.08.2017.
 */
public class KDNode extends AABB {

    public KDNode(Vector3f center, Vector3f dimensions) {
        super(center, dimensions);
    }

    public void passDown(int depth) {
        if(depth == 0) return;
        if(this.triangles.size() < 3) return;
        passDown();
        if(left != null) {
            left.passDown(depth-1);
        }if(right != null) {
            right.passDown(depth-1);
        }
    }

    void intersectedBoxes(ArrayList<KDNode> nodes, Ray r) {
        if(this.intersectable(r)) {
            if(this.triangles.size() > 0)nodes.add(this);
            if(left != null) {
                left.intersectedBoxes(nodes, r);
            }
            if(right != null) {
                right.intersectedBoxes(nodes, r);
            }
        }
        return;
    }

    private void passDown() {

        int divisionAxis = getDimensions().getX() > getDimensions().getY() ?
                (getDimensions().getX() > getDimensions().getZ() ? 1:3):
                (getDimensions().getY() > getDimensions().getZ() ? 2:3);

        QuickSort.quickSort(triangles, divisionAxis, 0, triangles.size() - 1);
        int mid = (triangles.size() - 1) / 2;
        float divider = QuickSort.getValueFromVector(Vector3f.add(
                triangles.get(mid).getVector(),
                (Vector3f)Vector3f.sub(triangles.get(mid + 1).getVector(), triangles.get(mid).getVector(), null).scale(0.5f),
                null), divisionAxis);

        if(divisionAxis == 1) {
            left = new KDNode(
                    new Vector3f(
                            this.getStartPoint().x + (divider - this.getStartPoint().x) / 2,
                            this.getStartPoint().y + this.getDimensions().y / 2,
                            this.getStartPoint().z + this.getDimensions().z / 2),
                    new Vector3f(divider - this.getStartPoint().x, getDimensions().y, getDimensions().z));

            right = new KDNode(
                    new Vector3f(
                            this.getStartPoint().x + this.getDimensions().x - (getDimensions().x - (divider - this.getStartPoint().x)) / 2,
                            this.getStartPoint().y + this.getDimensions().y - (getDimensions().y) / 2,
                            this.getStartPoint().z + this.getDimensions().z - (getDimensions().z) / 2),
                    new Vector3f(getDimensions().x - (divider - this.getStartPoint().x), getDimensions().y, getDimensions().z));
        }else if(divisionAxis == 2) {
            left = new KDNode(
                    new Vector3f(
                            this.getStartPoint().x + this.getDimensions().x / 2,
                            this.getStartPoint().y + (divider - this.getStartPoint().y) / 2,
                            this.getStartPoint().z + this.getDimensions().z / 2),
                    new Vector3f(this.getDimensions().x, divider - getStartPoint().y, getDimensions().z));

            right = new KDNode(
                    new Vector3f(
                            this.getStartPoint().x + this.getDimensions().x - (getDimensions().x) / 2,
                            this.getStartPoint().y + this.getDimensions().y - (getDimensions().y - (divider - this.getStartPoint().y)) / 2,
                            this.getStartPoint().z + this.getDimensions().z - (getDimensions().z) / 2),
                    new Vector3f(getDimensions().x, getDimensions().y - (divider - this.getStartPoint().y), getDimensions().z));
        }else{
            left = new KDNode(
                    new Vector3f(
                            this.getStartPoint().x + this.getDimensions().x / 2,
                            this.getStartPoint().y + this.getDimensions().y/ 2,
                            this.getStartPoint().z + (divider - this.getStartPoint().z) / 2),
                    new Vector3f(this.getDimensions().x, getDimensions().y,divider - getStartPoint().z));

            right = new KDNode(
                    new Vector3f(
                            this.getStartPoint().x + this.getDimensions().x - (getDimensions().x) / 2,
                            this.getStartPoint().y + this.getDimensions().y - (getDimensions().y) / 2,
                            this.getStartPoint().z + this.getDimensions().z - (getDimensions().z - (divider - this.getStartPoint().z)) / 2),
                    new Vector3f(getDimensions().x, getDimensions().y , getDimensions().z - (divider - this.getStartPoint().z)));

        }

        ArrayList<TriangleAndVector> ownTriangles = new ArrayList<>();

        for(TriangleAndVector t:triangles) {
            if(left.pointInside(t.getTriangle().getV1().getPosition()) && left.pointInside(t.getTriangle().getV2().getPosition()) && left.pointInside(t.getTriangle().getV3().getPosition())){
                left.triangles.add(t);
            }else if(right.pointInside(t.getTriangle().getV1().getPosition()) && right.pointInside(t.getTriangle().getV2().getPosition()) && right.pointInside(t.getTriangle().getV3().getPosition())){
                right.triangles.add(t);
            }else{
                ownTriangles.add(t);
            }
        }
        this.triangles = ownTriangles;
    }

    public void print() {
        print(0);
    }

    private void print(int spaces) {

        String sps = "";
        for(int i = 0; i < spaces; i++) {
            sps += "   ";
        }

        String s = sps+"Node [d="+spaces+"] [dim="+this.getCenter() + "  "+ this.getDimensions()+"\n"+sps+"triangles: "+triangles.size();
        System.out.println(s);
        if(left != null) {
            left.print(spaces +1 );
        }if(right != null) {
            right.print(spaces +1 );
        }

    }

    @Override
    public String toString() {

        String tris = "";

        for (TriangleAndVector s : triangles)
        {
            tris += s.toString() + "\t";
        }

        return "KDNode{\n" + super.toString() +
                ",\n  triangles=" + tris +
                ",\n  left=" + left +
                ",\n  right=" + right +
                "} " ;
    }

    private KDNode left;
    private KDNode right;
    private ArrayList<TriangleAndVector> triangles = new ArrayList<>();

    public KDNode getLeft() {
        return left;
    }

    public void setLeft(KDNode left) {
        this.left = left;
    }

    public KDNode getRight() {
        return right;
    }

    public void setRight(KDNode right) {
        this.right = right;
    }

    public ArrayList<TriangleAndVector> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<TriangleAndVector> triangles) {
        this.triangles = triangles;
    }
}


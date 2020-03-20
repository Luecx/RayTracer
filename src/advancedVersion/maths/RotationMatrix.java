package advancedVersion.maths;

import core.matrix.dense.DenseMatrix;
import core.vector.Vector3d;

public class RotationMatrix extends DenseMatrix {


    public RotationMatrix() {
        super(3, 3);
        this.self_identity();
    }

    public RotationMatrix(DenseMatrix other){
        super(other.getValues());
    }

    public static RotationMatrix rotateX(double alpha){
        RotationMatrix rx = new RotationMatrix();
        rx.setValue(0,0,1);
        rx.setValue(1, 1, Math.cos(alpha));
        rx.setValue(2, 2, Math.cos(alpha));
        rx.setValue(2, 1, Math.sin(alpha));
        rx.setValue(1,2,-Math.sin(alpha));
        return rx;
    }

    public static RotationMatrix rotateY(double alpha){
        RotationMatrix rx = new RotationMatrix();
        rx.setValue(1,1,1);
        rx.setValue(0, 0, Math.cos(alpha));
        rx.setValue(2, 2, Math.cos(alpha));
        rx.setValue(2,0,-Math.sin(alpha));
        rx.setValue(0, 2, Math.sin(alpha));
        return rx;
    }

    public static RotationMatrix rotateZ(double alpha){
        RotationMatrix rx = new RotationMatrix();
        rx.setValue(2,2,1);
        rx.setValue(0, 0, Math.cos(alpha));
        rx.setValue(1, 1, Math.cos(alpha));
        rx.setValue(1, 0, Math.sin(alpha));
        rx.setValue(0,1,-Math.sin(alpha));
        return rx;
    }

    public static RotationMatrix rotate(double alpha, double beta, double gamma){
        DenseMatrix rx = new RotationMatrix();
        rx = rotateX(alpha).mul(rx);
        rx = rotateX(beta).mul(rx);
        rx = rotateX(gamma).mul(rx);
        return new RotationMatrix(rx);
    }

    public static Vector3d rotateVectorAroundAxis(Vector3d vec, Vector3d axis, double theta){
        double x, y, z;
        double u, v, w;
        x=vec.getX();y=vec.getY();z=vec.getZ();
        u=axis.getX();v=axis.getY();w=axis.getZ();
        double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                        + x*Math.cos(theta)
                        + (-w*y + v*z)*Math.sin(theta);
        double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                        + y*Math.cos(theta)
                        + (w*x - u*z)*Math.sin(theta);
        double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                        + z*Math.cos(theta)
                        + (-v*x + u*y)*Math.sin(theta);
        return new Vector3d(xPrime, yPrime, zPrime);
    }

}

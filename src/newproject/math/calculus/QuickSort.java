package newproject.math.calculus;

import newproject.math.calculus.kdtree.TriangleAndVector;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Luecx on 19.08.2017.
 */
public class QuickSort {

    public static void quickSort(ArrayList<TriangleAndVector> arr, int values, int low, int high) {
        if (arr == null || arr.size() == 0)
            return;
        if (low >= high)
            return;
        int middle = low + (high - low) / 2;
        float pivot = getCenterValueFromTriangle(arr.get(middle), values);
        int i = low, j = high;
        while (i <= j) {
            while (getCenterValueFromTriangle(arr.get(i), values) < pivot) {
                i++;
            }
            while (getCenterValueFromTriangle(arr.get(j), values) > pivot) {
                j--;
            }
            if (i <= j) {
                TriangleAndVector temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
                i++;
                j--;
            }
        }
        if (low < j)
            quickSort(arr, values, low, j);
        if (high > i)
            quickSort(arr, values, i, high);
    }


    public static float getCenterValueFromTriangle(TriangleAndVector ar, int val){
        return val > 2?(ar.getVector()).z: (val < 2?(ar.getVector()).x:(ar.getVector()).y);
    }

    public static void quickSort(Vector3f[] arr, int values, int low, int high) {
        if (arr == null || arr.length == 0)
            return;
        if (low >= high)
            return;
        int middle = low + (high - low) / 2;
        float pivot = getValueFromVector(arr[middle], values);
        int i = low, j = high;
        while (i <= j) {
            while (getValueFromVector(arr[i], values) < pivot) {
                i++;
            }
            while (getValueFromVector(arr[j], values) > pivot) {
                j--;
            }
            if (i <= j) {
                Vector3f temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j)
            quickSort(arr, values, low, j);
        if (high > i)
            quickSort(arr, values, i, high);
    }

    public static void main(String[] args) {
        Vector3f a = new Vector3f(2,1,4);
        Vector3f b = new Vector3f(4,1,4);
        Vector3f c = new Vector3f(5,6,1);
        Vector3f d = new Vector3f(2,2,3);
       Vector3f[] ar = new Vector3f[]{a,b,c,d};
       quickSort(ar, 1, 0, ar.length-1);
       System.out.println(Arrays.toString(ar));
    }

    public static float getValueFromVector(Vector3f vec, int val) {
        return val > 2?vec.z: (val < 2?vec.x:vec.y);
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (arr == null || arr.length == 0)
            return;
        if (low >= high)
            return;
        int middle = low + (high - low) / 2;
        int pivot = arr[middle];
        int i = low, j = high;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j)
            quickSort(arr, low, j);
        if (high > i)
            quickSort(arr, i, high);
    }
}
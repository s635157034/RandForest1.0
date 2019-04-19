package MyUtils;

import Cart.ProperSubsetCombination;

import java.util.*;

/**
 * Created by sun on 19-4-10.
 */
public class utils {
    public static boolean isContain(double a, double[] b) {
        for (int i = 0; i < b.length; i++) {
            if (a == b[i])
                return true;
        }
        return false;
    }

    public static boolean isContain(int a, int[] b) {
        for (int i = 0; i < b.length; i++) {
            if (a == b[i])
                return true;
        }
        return false;
    }

    public static double[] getRemainingThreshold(int n,double[] threshold){
        ArrayList<Double> tmp = new ArrayList<>();
        for (double i = 1; i <= n; i++) {
            if(!isContain(i,threshold)){
                tmp.add(i);
            }
        }
        Double[] a=tmp.toArray(new Double[]{});
        double[] b=new double[tmp.size()];
        for (int j = 0; j < a.length; j++) {
            b[j]=a[j].doubleValue();
        }
        return b;
    }

    public static int[] convertDoubleToInt(double[] array){
        int[] result=new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i]=(int)array[i];
        }
        return result;
    }

    /**
     * 翻转数组
     * @param arr
     */
    public static double[][] flip(double[][] arr) {
        double[][] tmp=new double[arr[0].length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                tmp[j][i] = arr[i][j];
            }
        }
        return tmp;
    }

    /**
     * 二维数组按列交换
     * @param arr
     * @param a
     * @param b
     */
    public static void swapArray(double[][] arr,int a,int b){
        double tmp;
        for (int i = 0; i < arr.length; i++) {
            tmp=arr[i][a];
            arr[i][a]=arr[i][b];
            arr[i][b]=tmp;
        }
    }

    public static void printArray(double[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+"\t");
            }
            System.out.println();
        }
    }


    /**
     * 从1-n个二划分
     * @param n 离散值的个数
     * @return 返回二划分的集合
     */
    public static Set<Set<Integer>> combineSubset(int n) {
        Set<Integer> set = new HashSet<>();
        Set<Set<Integer>> result = new HashSet<Set<Integer>>();
        for (int i = 1; i <= n; i++) {
            set.add(i);
        }
        if (n % 2 == 0) {
            for (int i = 1; i < n / 2; i++) {
                result.addAll(ProperSubsetCombination.getProperSubset(i, set));
            }
            Set<Set<Integer>> tmp = ProperSubsetCombination.getProperSubset(n / 2, set);
            Iterator<Set<Integer>> it = tmp.iterator();
            for (int i = 0; i < tmp.size() / 2; i++) {
                result.add(it.next());
            }
        } else {
            for (int i = 1; i <= n / 2; i++) {
                result.addAll(ProperSubsetCombination.getProperSubset(i, set));
            }
        }
        return result;
    }

}

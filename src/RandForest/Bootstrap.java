package RandForest;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by sun on 19-4-17.
 */
public class Bootstrap {

    public static void main(String[] args) {
        double[][] input=new double[5][];
        input[0]= new double[]{0, 1, 2, 3, 0};
        input[1]= new double[]{0, 1, 2, 3, 0};
        input[2]= new double[]{0, 1, 2, 3, 0};
        input[3]= new double[]{0, 1, 2, 3, 1};
        input[4]= new double[]{0, 1, 2, 3, 1};
        input=bootStrap(input,2,3);
        MyUtils.utils.printArray(input);

    }

    public static double[][] bootStrap(double[][] data,int dataSize,int attributeSize){
        data=chooseData(data,bagging(data.length,dataSize));
        data=MyUtils.utils.flip(data);
        data=chooseData(data,randomAttributtes(data.length,attributeSize));
        return data;
    }

    public static int[] bagging(int input,int size){
        Random random=new Random(System.nanoTime());
        int[] result=new int[size];
        for (int i = 0; i < size; i++) {
            result[i]=random.nextInt(input);
        }
        return result;
    }


    public static int[] randomAttributtes(int input,int size){
        Random random=new Random(System.nanoTime());
        int[] result=new int[size];
        HashSet<Integer> set=new HashSet<>();
        while (set.size()<size){
            set.add(random.nextInt(input));
        }
        Integer[] tmp=set.toArray(new Integer[size]);
        for (int i = 0; i < size; i++) {
            result[i]=tmp[i];
        }
        return result;
    }

    public static double[][] chooseData(double[][] data,int[] nums){
        double[][] result=new double[nums.length][];
        int a=0;
        for (int i = 0; i < data.length; i++) {
            if(MyUtils.utils.isContain(i,nums)){
                result[a]=data[i];
                a++;
            }
        }
        return result;
    }

}

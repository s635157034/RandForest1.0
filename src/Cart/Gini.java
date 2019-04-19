package Cart;

import MyUtils.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sun on 19-4-9.
 */
public class Gini {
    /**
     * @param input 全部数据
     * @param inputClass 数据的类型（离散=值的个数,连续=0）
     * @param classId 分类属性的列号
     * @param start 数据的起点（包括）
     * @param end 数据的终点（包括）
     * @param exception 已经分类的数据序号
     * @return
     */
    public static GiniResult findMinGiniAttribute(double[][] input,int[] inputClass,int start,int end,int classId,int[] exception){
        int minAttributeId=-1;
        double minGini=1;
        double[] threshold=null;
        for (int i = 0; i < input.length; i++) {
            //不需要的属性排除，例如编号，分类，和已经计算过的属性
            if(utils.isContain(i,exception)){
                continue;
            }
            else {//连续值的gini系数计算
                if(inputClass[i]==CartTree.continuous){
                    //将连续值排序，为后面计算阈值（分裂点）做准备
                    int length=end-start+1;
                    double[] sorted=new double[length];
                    System.arraycopy(input[i],start,sorted,0,length);
                    Arrays.sort(sorted);
                    //计算所有可能的阈值
                    for (int j = 0; j < length-1; j++) {
                        double tmp=((sorted[j]+sorted[j+1])/2);
                        double tmpGini=calculateGini(input[i],input[classId],tmp,start,end);
                        if(tmpGini<minGini){
                            minGini=tmpGini;
                            minAttributeId=i;
                            threshold= new double[]{tmp};
                        }
                    }

                }else {//离散值的gini系数计算
                    Set<Set<Integer>> sets=utils.combineSubset(inputClass[i]);
                    Iterator<Set<Integer>> it=sets.iterator();
                    while (it.hasNext()){
                        Set<Integer> tmp=it.next();
                        Integer[] a=tmp.toArray(new Integer[]{});
                        double[] b=new double[tmp.size()];
                        for (int j = 0; j < a.length; j++) {
                            b[j]=a[j].doubleValue();
                        }
                        double tmpGini=calculateGini(input[i],input[classId],b,start,end);
                        if(tmpGini<minGini){
                            minGini=tmpGini;
                            minAttributeId=i;
                            threshold= b;
                        }
                    }

                }
            }
        }
        if(minAttributeId!=-1)
            return new GiniResult(minAttributeId,inputClass[minAttributeId],threshold,minGini);
        else return null;
    }


    /**
     * @param input 输入需要计算的连续属性数组
     * @param threshold 属性的阈值
     * @param type 类别
     * @return 返回Gini系数
     */
    public static double calculateGini(double[] input,double[] type,double threshold,int start,int end){
        int leftTrue=0,leftFalse=0;
        int rightTrue=0,rightFalse=0;
        double leftGini=1,rightGini=1;
        for (int i = start; i <= end; i++) {
            if(input[i]<=threshold){
                if(type[i]==0)
                    leftFalse++;
                else
                    leftTrue++;
            }else {
                if(type[i]==0)
                    rightFalse++;
                else
                    rightTrue++;
            }
        }
        double left=leftTrue+leftFalse;
        if(left==0){
            leftGini=0;
        }else
            leftGini=1-Math.pow((leftTrue/left),2)-Math.pow((leftFalse/left),2);

        double right=rightTrue+rightFalse;
        if(right==0){
            rightGini=0;
        }else
            rightGini=1-Math.pow((rightTrue/right),2)-Math.pow((rightFalse/right),2);
        double gini=left/(left+right)*leftGini+right/(left+right)*rightGini;
        return gini;
    }


    /**
     * @param input 输入需要计算的离散属性数组
     * @param threshold 属性的阈值分组
     * @param type 类别
     * @return
     */
    public static double calculateGini(double[] input,double[] type,double[] threshold,int start,int end){
        int leftTrue=0,leftFalse=0;
        int rightTrue=0,rightFalse=0;
        double leftGini=1,rightGini=1;
        for (int i = start; i <= end; i++) {
            if (utils.isContain(input[i],threshold)) {
                if(type[i]==0)
                    leftFalse++;
                else
                    leftTrue++;
            }else {
                if(type[i]==0)
                    rightFalse++;
                else
                    rightTrue++;
            }
        }
        double left=leftTrue+leftFalse;
        if(left==0){
            leftGini=0;
        }else
            leftGini=1-Math.pow((leftTrue/left),2)-Math.pow((leftFalse/left),2);

        double right=rightTrue+rightFalse;
        if(right==0){
            rightGini=0;
        }else
            rightGini=1-Math.pow((rightTrue/right),2)-Math.pow((rightFalse/right),2);
        double gini=left/(left+right)*leftGini+right/(left+right)*rightGini;
        return gini;
    }

    public static double calculateTotalGini(double[] type,int start,int end){
        int a=0,b=0;
        double size=end-start+1;
        for (int i = start; i <= end ; i++) {
            if(type[i]==0){
                a++;
            }else {
                b++;
            }
        }
        return 1-Math.pow(a/size,2)-Math.pow(b/size,2);
    }
}

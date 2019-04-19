package Cart;

import MyUtils.utils;
import com.csvreader.CsvReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sun on 19-4-9.
 */
public class main {
    public static void mainb(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        String inputPath="/media/work/毕业设计/Java/RandForest/data/fangchan.csv";
        FileReader fileReader=new FileReader(inputPath);
        CsvReader csvReader=new CsvReader(fileReader);
        ArrayList<String[]> list=new ArrayList<>();
        while (csvReader.readRecord()){
            list.add(csvReader.getRawRecord().split(","));
        }
        int row=list.size();
        int col=list.get(0).length;
        double[][] input=new double[row][col];
        for (int i = 0; i < row; i++) {
            String[] strs=list.get(i);
            for (int j = 0; j < col; j++) {
                input[i][j]=Double.valueOf(strs[j]);
            }
        }
        System.out.println("程序运行时间：" + (System.currentTimeMillis() - startTime) + "ms");
        int[] inputClass=new int[col];
        input= utils.flip(input);
        CartTree cartTree=new CartTree();
        cartTree.data=input;
        cartTree.dataClass=inputClass;
        cartTree.dataClassId=col-1;
        //cartTree.maxDepth=2;
        cartTree.buildCartTree(0,row-1,new int[]{0,col-1});

        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
    }



    public static void main(String[] args) {
        double[][] input=new double[5][];
        input[0]= new double[]{0, 1, 2, 3, 0};
        input[1]= new double[]{0, 1, 2, 3, 0};
        input[2]= new double[]{0, 1, 2, 3, 0};
        input[3]= new double[]{0, 1, 2, 3, 1};
        input[4]= new double[]{0, 1, 2, 3, 1};
        int[] inputClass=new int[]{0,2,0,0,2};
        input=utils.flip(input);
        utils.printArray(input);
//        System.out.println(Gini.calculateTotalGini(input[4],0,4));


        CartTree cartTree=new CartTree();
        cartTree.data=input;
        cartTree.dataClass=inputClass;
        cartTree.dataClassId=4;
        int[] exception=new int[]{4};
        cartTree.buildCartTree(0,4,exception);
        Collections.sort(cartTree.cartTreeNodes);
        System.out.println(cartTree.toString());

/*        CartTree cartTree=new CartTree();
        cartTree.data=input;
        cartTree.dataClass=inputClass;
        utils.printArray(input);
        System.out.println();
        int a=cartTree.sortData(0,0,5,new double[]{0,1});
        utils.printArray(input);
        System.out.println(a);


        utils.flip(input);
        Gini gini=new Gini();
        GiniResult giniResult=gini.findMinGiniAttribute(input,inputClass,0,4,4,new int[0]);
        System.out.println(giniResult);


        utils.swapArray(input,2,3);
        System.out.println(Arrays.toString(input[0]));
        System.out.println(Arrays.toString(input[1]));
        System.out.println(Arrays.toString(input[2]));
        System.out.println(Arrays.toString(input[3]));
        System.out.println(Arrays.toString(input[4]));*/

    }
}

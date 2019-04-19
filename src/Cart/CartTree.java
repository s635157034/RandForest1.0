package Cart;

import MyUtils.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sun on 19-4-10.
 */
public class CartTree {
    static final int continuous = 0;//连续值类别为0
    double minGini = 0;
    int maxDepth = -1;
    double[][] data;
    int[] dataClass;//连续值=0，离散值等于取值个数
    int dataClassId;
    List<CartTreeNode> cartTreeNodes = new ArrayList<>();

    public CartTree() {
    }

    public CartTree(double[][] data, int[] dataClass, int dataClassId) {
        this.data = data;
        this.dataClass = dataClass;
        this.dataClassId = dataClassId;
    }

    public CartTree(double maxGini, double[][] data, int[] dataClass, int dataClassId) {
        this.minGini = maxGini;
        this.data = data;
        this.dataClass = dataClass;
        this.dataClassId = dataClassId;
    }

    public CartTree(int maxDepth, double[][] data, int[] dataClass, int dataClassId) {
        this.maxDepth = maxDepth;
        this.data = data;
        this.dataClass = dataClass;
        this.dataClassId = dataClassId;
    }

    public CartTree(double maxGini, int maxDepth, double[][] data, int[] dataClass, int dataClassId) {
        this.minGini = maxGini;
        this.maxDepth = maxDepth;
        this.data = data;
        this.dataClass = dataClass;
        this.dataClassId = dataClassId;
    }

    public void buildCartTree(int start, int end, int[] exception) {
        cartTreeNodes.add(new CartTreeNodeRoot(start,end,Gini.calculateTotalGini(data[dataClassId],start,end)));
        buildCartTree(start, end, exception, 1);
        buildClassifer();
    }

    public int[] buildCartTree(int start, int end, int[] exception, int depth) {
        int middle = -1;
        int[] childResult = new int[]{-1,-1};
        GiniResult result = Gini.findMinGiniAttribute(data, dataClass, start, end, dataClassId, exception);
        //属性已经分完或者超过最大深度
        if (result != null && (maxDepth==-1 || depth<=maxDepth)) {
            //如果最佳分裂的属性是连续值
            if (result.getAttriClass() == continuous) {
                middle = sortData(result.getAttriId(), start, end, result.getThreshold()[0]);
            } else {//如果是离散值
                middle = sortData(result.getAttriId(), start, end, result.getThreshold());
            }
            //没有分裂
            if (middle == -1) {
                cartTreeNodes.get(cartTreeNodes.size() - 1).setLeaf(true);
                return new int[]{-1, -1};
            } else {
                CartTreeNode cartTreeNodeL = null;
                CartTreeNode cartTreeNodeR = null;
                //将已经分裂的属性加入到例外数组中
                int[] newException = new int[exception.length + 1];
                System.arraycopy(exception, 0, newException, 0, exception.length);
                newException[exception.length] = result.getAttriId();

                double leftGini = Gini.calculateTotalGini(data[dataClassId], start, middle);
                double rightGini = Gini.calculateTotalGini(data[dataClassId], middle + 1, end);
                if (result.getAttriClass() == continuous) {
                    cartTreeNodeL = new CartTreeNodeLess(result.getAttriId(), result.getThreshold()[0], start, middle, depth, leftGini);
                    cartTreeNodeR = new CartTreeNodeMore(result.getAttriId(), result.getThreshold()[0], middle + 1, end, depth, rightGini);
                } else {
                    cartTreeNodeL = new CartTreeNodeDiscrete(result.getAttriId(), result.getThreshold(), start, middle, depth, leftGini);
                    cartTreeNodeR = new CartTreeNodeDiscrete(result.getAttriId(), utils.getRemainingThreshold(dataClass[result.getAttriId()],
                            result.getThreshold()), middle + 1, end, depth, rightGini);
                }
                cartTreeNodes.add(cartTreeNodeL);
                cartTreeNodes.add(cartTreeNodeR);
                childResult[0] = cartTreeNodes.indexOf(cartTreeNodeL);
                childResult[1] = cartTreeNodes.indexOf(cartTreeNodeR);
                if (leftGini > minGini) {
                    int[] child = buildCartTree(start, middle, newException, depth + 1);
                    cartTreeNodeL.setLeftChild(child[0]);
                    cartTreeNodeL.setRightChild(child[1]);
                } else {
                    cartTreeNodeL.setLeaf(true);
                }
                if (rightGini > minGini) {
                    int[] child = buildCartTree(middle + 1, end, newException, depth + 1);
                    cartTreeNodeR.setLeftChild(child[0]);
                    cartTreeNodeR.setRightChild(child[1]);
                } else {
                    cartTreeNodeR.setLeaf(true);
                }
            }
        }else {
            cartTreeNodes.get(cartTreeNodes.size() - 1).setLeaf(true);
            return new int[]{-1, -1};
        }
        return childResult;
    }

    public void buildClassifer() {
        Iterator<CartTreeNode> it = cartTreeNodes.iterator();
        CartTreeNode tmp;
        while (it.hasNext()) {
            tmp = it.next();
            if (tmp.isLeaf()) {
                int t = 0, f = 0;
                for (int i = tmp.getStart(); i <= tmp.getEnd(); i++) {
                    if (data[dataClassId][i] == 1) {
                        t++;
                    } else {
                        f++;
                    }
                }
                if (t >= f) {
                    tmp.setNodeClass(1);
                } else {
                    tmp.setNodeClass(0);
                }
            }
        }
    }

    public int verify(double[] data) {
        for (int i = 0; i < cartTreeNodes.size(); ) {
            CartTreeNode node=cartTreeNodes.get(i);
            if(node.isContain(data[node.getAttriId()])){
                if(node.isLeaf()){
                    return node.getNodeClass();
                }
                CartTreeNode tmp=cartTreeNodes.get(node.getLeftChild());
                if(tmp.isContain(data[tmp.getAttriId()])){
                    i=node.getLeftChild();
                }else {
                    i=node.getRightChild();
                }
            }else {
                i++;
            }
        }
        return -1;
    }

    /**
     * @param attrId    分裂属性的id
     * @param start     数组的起始位置
     * @param end       数组的终止位置
     * @param threshold 阈值
     * @return 返回分界点
     */
    public int sortData(int attrId, int start, int end, double threshold) {
        int left = start, right = end;
        int countL = 0, countR = 0;
        while (left < right) {
            while (data[attrId][left] <= threshold && left < right) {
                left++;
                countL++;
            }
            while (data[attrId][right] > threshold && left < right) {
                right--;
                countR++;
            }
            if (left < right) {
                utils.swapArray(data, left, right);
            }
        }
        if (data[attrId][left] <= threshold) {
            countL++;
        } else {
            countR++;
        }
        //分成两部分时
        if (countL > 0 && countR > 0)
            return left - 1;
        else//只有一部分，说明到根节点不会在继续分类
            return -1;
    }

    /**
     * @param attrId    分裂属性的id
     * @param start     数组的起始位置
     * @param end       数组的终止位置
     * @param threshold 阈值
     * @return 返回分界点
     */
    public int sortData(int attrId, int start, int end, double[] threshold) {
        int left = start, right = end;
        int countL = 0, countR = 0;
        while (left < right) {
            while (utils.isContain(data[attrId][left], threshold) && left < right) {
                left++;
                countL++;
            }
            while (!utils.isContain(data[attrId][right], threshold) && left < right) {
                right--;
                countR++;
            }
            if (left < right) {
                utils.swapArray(data, left, right);
            }
        }
        if (utils.isContain(data[attrId][left], threshold)) {
            countL++;
        } else {
            countR++;
        }
        //分成两部分时
        if (countL > 0 && countR > 0)
            return left - 1;
        else//只有一部分，说明到根节点不会在继续分类
            return -1;
    }

}

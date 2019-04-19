package Cart;

/**
 * Created by sun on 19-4-10.
 */
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
public class ProperSubsetCombination {

    private static Integer[] array;
    private static BitSet startBitSet; // 比特集合起始状态
    private static BitSet endBitSet; // 比特集合终止状态，用来控制循环
    private static Set<Set<Integer>> properSubset; // 真子集集合

    /**
     * 计算得到一个集合的非空真子集集合
     *
     * @param n
     *            真子集的大小
     * @param itemSet
     *            一个频繁项集元素
     * @return 非空真子集集合
     */
    public static Set<Set<Integer>> getProperSubset(int n, Set<Integer> itemSet) {
        Integer[] array = new Integer[itemSet.size()];
        ProperSubsetCombination.array = itemSet.toArray(array);
        properSubset = new HashSet<Set<Integer>>();
        startBitSet = new BitSet();
        endBitSet = new BitSet();

        // 初始化startBitSet，左侧占满1
        for (int i = 0; i < n; i++) {
            startBitSet.set(i, true);
        }

        // 初始化endBit，右侧占满1
        for (int i = array.length - 1; i >= array.length - n; i--) {
            endBitSet.set(i, true);
        }

        // 根据起始startBitSet，将一个组合加入到真子集集合中
        get(startBitSet);

        while (!startBitSet.equals(endBitSet)) {
            int zeroCount = 0; // 统计遇到10后，左边0的个数
            int oneCount = 0; // 统计遇到10后，左边1的个数
            int pos = 0; // 记录当前遇到10的索引位置

            // 遍历startBitSet来确定10出现的位置
            for (int i = 0; i < array.length; i++) {
                if (!startBitSet.get(i)) {
                    zeroCount++;
                }
                if (startBitSet.get(i) && !startBitSet.get(i + 1)) {
                    pos = i;
                    oneCount = i - zeroCount;
                    // 将10变为01
                    startBitSet.set(i, false);
                    startBitSet.set(i + 1, true);
                    break;
                }
            }
            // 将遇到10后，左侧的1全部移动到最左侧
            int counter = Math.min(zeroCount, oneCount);
            int startIndex = 0;
            int endIndex = 0;
            if (pos > 1 && counter > 0) {
                pos--;
                endIndex = pos;
                for (int i = 0; i < counter; i++) {
                    startBitSet.set(startIndex, true);
                    startBitSet.set(endIndex, false);
                    startIndex = i + 1;
                    pos--;
                    if (pos > 0) {
                        endIndex = pos;
                    }
                }
            }
            get(startBitSet);
        }
        return properSubset;
    }

    /**
     * 根据一次移位操作得到的startBitSet，得到一个真子集
     *
     * @param bitSet
     */
    private static void get(BitSet bitSet) {
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            if (bitSet.get(i)) {
                set.add(array[i]);
            }
        }
        properSubset.add(set);
    }
}
package Cart;

import java.util.Arrays;

/**
 * Created by sun on 19-4-9.
 */
public class GiniResult {
    private int attriId=-1;
    private int attriClass;
    private double[] threshold;
    private double gini;

    public GiniResult(int attriId, int attriClass, double[] threshold, double gini) {
        this.attriId = attriId;
        this.attriClass = attriClass;
        this.threshold = threshold;
        this.gini = gini;
    }

    public int getAttriId() {
        return attriId;
    }

    public void setAttriId(int attriNum) {
        this.attriId = attriNum;
    }

    public int getAttriClass() {
        return attriClass;
    }

    public void setAttriClass(int attriClass) {
        this.attriClass = attriClass;
    }

    public double[] getThreshold() {
        return threshold;
    }

    public void setThreshold(double[] threshold) {
        this.threshold = threshold;
    }

    public double getGini() {
        return gini;
    }

    public void setGini(double gini) {
        this.gini = gini;
    }

    @Override
    public String toString() {
        return "GiniResult{" +
                "attriNum=" + attriId +
                ", attriClass=" + attriClass +
                ", threshold=" + Arrays.toString(threshold) +
                ", gini=" + gini +
                '}';
    }
}

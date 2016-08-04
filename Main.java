package com.aesean.shadow;

/**
 * Main
 *
 * @author xl
 * @version V1.0
 * @since 16/8/3
 */
public class Main {
    public static final double ACCURATE_RESULT = 90 + 25f * 0.5f * Math.atan(0.75f) - 25f * Math.PI;

    public static void main(String[] args) {
        ShadowArea shadowArea = new ShadowArea(0, 20, 0, 10);
        shadowArea.setAccurateResult(ACCURATE_RESULT);
        shadowArea.addCondition(new LineArea(0.5, 0, LineArea.TYPE_BOTTOM));
        shadowArea.addCondition(new LineArea(5, LineArea.TYPE_RIGHT));
        shadowArea.addCondition(new CircleArea(5, 5, 5, CircleArea.TYPE_OUT));
        shadowArea.addCondition(new CircleArea(15, 5, 5, CircleArea.TYPE_OUT));
        shadowArea.setMaxCount(100000000);
        shadowArea.runByMultiThread();
    }
}

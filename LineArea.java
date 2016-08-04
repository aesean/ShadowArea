package com.aesean.shadow;

/**
 * LineArea
 *
 * @author xl
 * @version V1.0
 * @since 16/8/3
 */
public class LineArea implements Area {

    public static final int TYPE_LEFT = 0x1000;
    public static final int TYPE_TOP = 0x0100;
    public static final int TYPE_RIGHT = 0x0010;
    public static final int TYPE_BOTTOM = 0x0001;

    private static final int TYPE_LINE = 0;
    private static final int TYPE_LINE_X = 1;

    private double mK;
    private double mB;
    private int mType;
    private int mLineType;

    public LineArea(double x0, double y0, double x1, double y1, int type) {
        mK = (y1 - y0) / (x1 - x0);
        mB = y0 - mK * x0;
        mType = type;
    }

    public LineArea(double value, int type) {
        mB = value;
        mLineType = TYPE_LINE_X;
        mType = type;
    }

    public LineArea(double k, double b, int type) {
        mK = k;
        mB = b;
        mType = type;
    }

    @Override
    public boolean contain(double x, double y) {
        switch (mType) {
            case TYPE_LEFT:
                if (mLineType == TYPE_LINE_X) {
                    return x < mB;
                } else {
                    return x < (y - mB) / mK;
                }
            case TYPE_TOP:
                return y > mK * x + mB;
            case TYPE_RIGHT:
                if (mLineType == TYPE_LINE_X) {
                    return x > mB;
                } else {
                    return x > (y - mB) / mK;
                }
            case TYPE_BOTTOM:
                return y < mK * x + mB;
            default:
                throw new RuntimeException("未知的Type类型,type=" + mType);
        }
    }
}

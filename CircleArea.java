package com.aesean.shadow;

/**
 * CircleArea
 *
 * @author xl
 * @version V1.0
 * @since 16/8/3
 */
public class CircleArea implements Area {

    public static final int TYPE_INNER = 0x01;
    public static final int TYPE_OUT = 0x10;

    private double mCircleX;
    private double mCircleY;
    private double mRadius;
    private int mType;

    public CircleArea(double circleX, double circleY, double radius, int type) {
        mCircleX = circleX;
        mCircleY = circleY;
        mRadius = radius;
        mType = type;
    }

    @Override
    public boolean contain(double x, double y) {
        // 计算传入的点到圆心距离,计算是否在范围内
        switch (mType) {
            case TYPE_INNER:
                return ((x - mCircleX) * (x - mCircleX) + (y - mCircleY) * (y - mCircleY)) < mRadius * mRadius;
            case TYPE_OUT:
                return ((x - mCircleX) * (x - mCircleX) + (y - mCircleY) * (y - mCircleY)) > mRadius * mRadius;
            default:
                throw new RuntimeException("未知的Type类型,type=" + mType);
        }
    }
}

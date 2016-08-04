package com.aesean.shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ShadowArea
 *
 * @author xl
 * @version V1.0
 * @since 16/8/4
 */
public class ShadowArea implements Runnable {
    private int mMaxCount = 10000;
    private double mStartX;
    private double mEndX;
    private double mStartY;
    private double mEndY;

    private double mAccurateResult;

    private boolean mAccurateResultAvailable = false;

    public double getAccurateResult() {
        return mAccurateResult;
    }

    public void setAccurateResult(double accurateResult) {
        mAccurateResult = accurateResult;
        mAccurateResultAvailable = true;
    }

    private long mStartTime = -1;

    private int mHitTimes = 0;

    private List<Area> mAreaList;

    public int getMaxCount() {
        return mMaxCount;
    }

    public void setMaxCount(int maxCount) {
        mMaxCount = maxCount;
    }

    public ShadowArea(double width, double height) {
        this(0, width, 0, height);
    }

    public ShadowArea(double startX, double endX, double startY, double endY) {
        mStartX = startX;
        mEndX = endX;
        mStartY = startY;
        mEndY = endY;
        mAreaList = new ArrayList<>();
    }

    /**
     * 这里的条件是叠加取交集关系,比如添加条件x>10,又添加了一个条件x>20,最后会取交集x>20
     *
     * @param area 区域
     * @return this
     */
    public ShadowArea addCondition(Area area) {
        mAreaList.add(area);
        return this;
    }

    private int mSeedPrefix = 1;

    public int getSeedPrefix() {
        return mSeedPrefix;
    }

    public ShadowArea setSeedPrefix(int seedPrefix) {
        mSeedPrefix = seedPrefix;
        return this;
    }

    private synchronized void initTimer() {
        if (mStartTime == -1) {
            mStartTime = System.currentTimeMillis();
        }
    }

    private synchronized int initSeedPrefix() {
        mSeedPrefix++;
        return mSeedPrefix;
    }

    /**
     * 并发多线程,取CPU核心数作为并发线程数
     */
    public void runByMultiThread() {
        runByMultiThread(Runtime.getRuntime().availableProcessors());
    }

    public void runByMultiThread(int threadCount) {
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            threadPool.execute(this);
        }
    }

    /**
     * 支持并发,已经处理了线程安全问题
     */
    @Override
    public void run() {
        initTimer();
        int seedPrefix = initSeedPrefix();
        int prefix = seedPrefix * mMaxCount;
        Random random = new Random();
        int times = 0;
        int hitTimes = 0;
        for (int i = 0; i < mMaxCount; i++) {
            boolean hit = true;
            times++;
            random.setSeed(prefix + times);
            double x = mStartX + random.nextDouble() * mEndX;
            double y = mStartY + random.nextDouble() * mEndY;
            for (Area area : mAreaList) {
                if (!area.contain(x, y)) {
                    hit = false;
                    break;
                }
            }
            if (hit) {
                hitTimes++;
            }
        }

        addHitTimes(hitTimes);
        addAllTimes(times);
        printResult();
    }

    private synchronized void addHitTimes(int value) {
        mHitTimes += value;
    }

    private synchronized void addAllTimes(int value) {
        mAllTimes += value;
    }

    public void printResult() {
        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println("****************华丽的分割线****************");
        System.out.println("计算耗时:" + (end - mStartTime) + "毫秒");
        System.out.println("一共打点:" + this.getAllTimes() + "个");
        System.out.println("命中:" + this.getHitTimes());
        double area = this.getShadowArea();
        System.out.println("面积:" + area);
        if (mAccurateResultAvailable) {
            System.out.println("精确度:" + (1 - Math.abs(area - mAccurateResult) / mAccurateResult) * 100 + "%");
        }
    }

    private int mAllTimes = 0;

    public int getAllTimes() {
        return mAllTimes;
    }

    public double getShadowArea() {
        return mHitTimes * 1f * (mEndX - mStartX) * (mEndY - mStartY) / getAllTimes();
    }

    public int getHitTimes() {
        return mHitTimes;
    }
}

package com.mtopgul;

public class LoadTest {
    private static final int ITERATIONS = Integer.MAX_VALUE; // 100 Milyon

    public static void main(String[] args) {
        System.out.println("--- Full Benchmark ---");
        long startTime = System.nanoTime();
        for (int i = 0; i < 25; i++) {
//            test01();
            test02();
        }
        long endTime = System.nanoTime();
        System.out.println("Benchmark Test:   " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void test01() {
        Message2 msg = new Message2();
        msg.setLabel(1);
        msg.setLatitude(500000L);
        msg.setLongitude(250000L);
        msg.setGridOrigin(10);

        byte[] testData;

        // 1. Manuel / Performanslı Decode Testi
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            testData = msg.encode();
            msg.decode(testData);
            if (msg.getLabel() == -1) System.out.print("");
        }
        long endTime = System.nanoTime();
        System.out.println("Test01:   " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void test02() {
        Message1 msg = new Message1();
        msg.setLabel(1);
        msg.setLatitude(500000L);
        msg.setLongitude(250000L);
        msg.setGridOrigin(10);

        byte[] testData;

        // 1. Manuel / Performanslı Decode Testi
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            testData = msg.encode();
            msg.decode(testData);
            if (msg.getLabel() == -1) System.out.print("");
        }
        long endTime = System.nanoTime();
        System.out.println("Test02:   " + (endTime - startTime) / 1_000_000 + " ms");
    }
}

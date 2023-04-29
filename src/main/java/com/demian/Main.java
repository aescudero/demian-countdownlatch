package com.demian;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        conventionalWay();
        streamWay();
        countDownLatchWay();
    }

    public static void streamWay() {
        int result = Stream.of(1, 2, 3, 4, 5).parallel().map(i -> 2 * i).reduce(0, Integer::sum);
        System.out.println("result streamWay: " + result);
    }

    public static void conventionalWay() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> firstOutput = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            firstOutput.add(2 * input.get(i));
        }

        int sum = 0;
        for (int i = 0; i < firstOutput.size(); i++) {
            sum += firstOutput.get(i);
        }
        System.out.println("result conventionalWay: " + sum);
    }

    public static void countDownLatchWay() throws InterruptedException {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        final CountDownLatch cdl = new CountDownLatch(list.size());
        final List<Integer> multiplierList = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        for (Integer value : list) {
            threads.add(new Thread(new Multiplier(cdl, value, multiplierList)));
        }
        threads.forEach(Thread::start);
        cdl.await();

        Integer sum = 0;
        for (Integer i : multiplierList) {
            sum += i;
        }

        System.out.println("result CountDownLatchWay: " + sum);
    }

    public static class Multiplier implements Runnable {
        private final CountDownLatch cdl;
        private final int value;
        private final List<Integer> multiplierList;

        public Multiplier(CountDownLatch cdl, int value, List<Integer> multiplierList) {
            this.cdl = cdl;
            this.value = value;
            this.multiplierList = multiplierList;
        }

        @Override
        public void run() {

            multiplierList.add(this.value * 2);
            this.cdl.countDown();
        }
    }

}

package com.centratl;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发编程java.util.concurrent(JUC)
 * AQS(AbstractQueuedSynchronizer)
 */
@Slf4j
public class JUCAQSDemo {

    private final static int threadCount = 20;

    /**
     * CountDownLatch用来控制一个线程等待多个线程，维护一个计数器cnt，
     * 每次调用countDown()会让计数器的值减一， 减到零时，
     * 那些因为调用await()方法而在等待的线程会被唤醒
     */

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    test(threadNum);
                } catch (Exception e) {
                    log.error("exception", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        log.info("finish");
        exec.shutdown();
    }

    private static void test(int threadNum) throws Exception {
        Thread.sleep(100);
        log.info("{}", threadNum);
        Thread.sleep(100);
    }
}
package com.centratl;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 并发编程java.util.concurrent(JUC)
 * AQS(AbstractQueuedSynchronizer)
 */
@Slf4j
public class JUCAQSSemaphore {
    /**
     * Semaphore就是操作系统中的信号量，可以控制对互斥资源的访问线程数
     * 以下代码模拟了对某个服务的并发请求，每次只能有3个客户端同时访问，请求总数为 20。
     */
    private final static int threadCount = 20;

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire(); // 每次获取一个许可
                    test(threadNum);
                    //semaphore.release(); // 每次释放一个许可
                } catch (Exception e) {
                    log.error("exception", e);
                } finally {
                    semaphore.release();// 每次释放一个许可
                }
            });
        }
        executorService.shutdown();
    }

    private static void test(int threadNum) throws Exception {
        log.info("{}", threadNum);
        Thread.sleep(1000);
    }
}
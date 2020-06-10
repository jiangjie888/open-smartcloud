package com.centratl;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发编程java.util.concurrent(JUC)
 * AQS(AbstractQueuedSynchronizer)
 */
public class JUCAQSCycliBarrier {
    /**
     *CyclicBarrier用来控制多个线程相互等待，只有当多个线程都到达时，这些线程才会继续执行，
     * 和CountDownLatch相似，都是通过维护计数器实现的，但他的计数器是递增的。每次执行await（）
     * 方法后，计数器会加1，直到计数器的值和设置的值相同，等待的所有线程才会继续执行，和CountDownLatch
     * 的另一个区别是，CyclicBarrier的计数器可以循环使用，所以才叫他循环屏障
     */
    public static void main(String[] args){
        final int totalThread=10;
        CyclicBarrier cyclicBarrier =new CyclicBarrier(totalThread);
        ExecutorService executorService=Executors.newCachedThreadPool();
        for (int i = 0; i < totalThread; i++) {
            executorService.execute(()->{
                System.out.println("before..*");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.print("after   ");
            });
        }
        executorService.shutdown();
    }
}
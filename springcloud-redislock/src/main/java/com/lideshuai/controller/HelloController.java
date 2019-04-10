package com.lideshuai.controller;

import com.lideshuai.DistributedLockHandler;
import com.lideshuai.Lock;
import com.lideshuai.config.AquiredLockWorker;
import com.lideshuai.config.DistributedLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CountDownLatch;


@RestController
public class HelloController {

    @Autowired
    private DistributedLockHandler distributedLockHandler;

    @Autowired
    private DistributedLocker distributedLocker;


    @RequestMapping("index")
    public String index(){
        Lock lock = new Lock("lynn","min");
        if (distributedLockHandler.tryLock(lock)){
            try {
                //为了演示锁的效果，这里睡眠5000毫秒
                System.out.println("执行方法");

                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        distributedLockHandler.releaseLock(lock);

        return "hello world!";
    }

    @RequestMapping("index1")
    public String index1() throws Exception {

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);

        for (int i = 0; i < 5 ; i++) {
            new Thread(new Worker(startSignal,doneSignal)).start();
        }

        startSignal.countDown(); // let all threads proceed
        doneSignal.await();

//        distributedLocker.lock("test", new AquiredLockWorker<Object>() {
//            @Override
//            public Object invokeAfterLockAquire() throws Exception {
//                try {
//                    System.out.println("执行方法！");
//                    Thread.sleep(50000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        });

        return "hello world!";
    }


    class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }


        @Override
        public void run() {
            try {
                startSignal.await();
                System.out.println("=================进入");
                distributedLocker.lock("test",new AquiredLockWorker<Object>() {
                    @Override
                    public Object invokeAfterLockAquire() {
                        doTask();
                        return null;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        void doTask() {
            System.out.println(Thread.currentThread().getName() + " start");
            Random random = new Random();
            int _int = random.nextInt(200);
            System.out.println(Thread.currentThread().getName() + " sleep " + _int + "millis");
            try {
                Thread.sleep(_int);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end");
            doneSignal.countDown();
        }

    }

}



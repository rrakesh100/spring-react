package com.educative.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Interview {


  //using completable future.
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Use completablefuture.runasync and wait for the future to complete
        //module1()

        //spawn k threads which will perform some operation and wait until complete
       // module2();

    }

    private static void module2() throws ExecutionException, InterruptedException {

        List<CompletableFuture> futureList = new ArrayList<CompletableFuture>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i=0;i<10;i++) {
            CompletableFuture future = new CompletableFuture();
            futureList.add(future);
            executorService.submit(new MyRunnable(future));
        }


        executorService.shutdown();

      for(CompletableFuture future : futureList) {
          System.out.println(future.get());
      }

        System.out.println("Done done done");

    }

    private static class MyRunnable implements Runnable {

        private CompletableFuture completableFuture;

        MyRunnable(CompletableFuture completableFuture) {
            this.completableFuture=completableFuture;
        }
        @Override
        public void run() {
            try {
                Random rand = new Random();
                int sleeptime = rand.nextInt(10000);
                System.out.println(sleeptime);
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            completableFuture.complete("Thread complete - - " + Thread.currentThread().getName());
        }
    }


    static void module1() throws ExecutionException, InterruptedException {
        System.out.println("Received request to fetch orders and catalogues");

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        OrdersService orders = new OrdersService();
        CatalogService catalog = new CatalogService();


        //runAsync is to supply runnable where we are not interested in returning, supplyAsync is used if we want the thread to return something
        CompletableFuture<String> ordersFuture = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return   "hi";
        });

        CompletableFuture catalogFuture = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread name = " + Thread.currentThread().getName() + "---" + catalog.getCatalogs());
            return  catalog.getCatalogs();

        });

        System.out.println("Waiting now...");

        CompletableFuture newFuture = ordersFuture
                .thenApply((s)->{
                    System.out.println("jjjjj"+ s);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return  catalog.getCatalogs();

                });

        System.out.println("thread name = "+ Thread.currentThread().getName()+ newFuture.get());

        System.out.println(ordersFuture.get());
        ordersFuture.thenApply(list -> {
            //  List returnList = (List) list;
            //  returnList.add("567");
            //  System.out.println(returnList);
            // return returnList;
            System.out.println("hi");
            return "done";
        });
        System.out.println("Orders done ...");
        catalogFuture.complete("nono");
        System.out.println(catalogFuture.get());
        System.out.println("Catalogs done ...");
    }





    private static class OrdersService {

        public List<String> getOrders() {
            return Arrays.asList("123", "234");
        }
    }

    private static class CatalogService {

        public List<String> getCatalogs() {
            return Arrays.asList("abc", "xyz");
        }
    }
}

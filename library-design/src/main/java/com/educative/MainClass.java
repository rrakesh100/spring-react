package com.educative;

import org.apache.commons.lang3.RandomUtils;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import static java.util.Collections.reverseOrder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainClass {

    private String mem;

    private static class B {
        int id = 2;
    }

    private static class Obj<T> {
        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

        List<T> list ;
    }

    private static class A extends B {

        private List<Integer> vertices;
        private Obj[] arraY;
        int id=3;
    }
    public static void main(String[] args) throws Exception {
       // SpringApplication.run(MainClass.class, args);
        B b = new A();
        System.out.println( b.id);
        ((A) b).vertices = new ArrayList<>();
        ((A) b).vertices.add(1);
        ((A) b).vertices.add(2);

        List<String> m = new ArrayList<String>();
        m.add("Alala");
        m.add("Basda");
        Obj o1 = new Obj();
        o1.setList(m);

        ((A) b).arraY = new Obj[5];
        ((A) b).arraY[0] = o1;

        List<Integer> m2 = new ArrayList<Integer>();
        m2.add(1);
        m2.add(3);
        Obj o2 = new Obj();
        o2.setList(m2);

        ((A) b).arraY[1] = o2;

//        ((A) b).arraY = (List<Integer>[]) Array.newInstance(List.class , 5);
//        ((A) b).arraY[0]= ((A) b).vertices;
//        ((A) b).arraY[1]= m;
        System.out.println(((A) b).arraY[0].getList());
        System.out.println(((A) b).arraY[1].getList());
        System.out.println(((A) b).vertices);



        Map<String, Integer> suggestions = new HashMap<>();
        suggestions.put("asd", 1);
        suggestions.put("feer", 3);
        suggestions.put("rddsdsd",2);
        suggestions.put("dum",4);

        Map result = new LinkedHashMap();

        List<Map.Entry> all = suggestions.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        System.out.println(all);


       result = suggestions.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(
               Collectors.toMap(entry -> entry.getKey(), Map.Entry::getValue ,(x, y)-> {throw new AssertionError();},
                       LinkedHashMap::new));

        System.out.println(result);

        //one way of reading a file
        try (BufferedReader buf = new BufferedReader(new FileReader("/Users/rrampall/work/rapid/a"))){
            String line = buf.readLine();
            while(line!=null) {
                System.out.println(line);
                line = buf.readLine();

            }
        }

        //another way of reading a file

        List<String> allLines = Files.readAllLines(Paths.get("/Users/rrampall/work/rapid/a"));
        for(String line : allLines)
            System.out.println(line);

        MainClass mc = new MainClass();
        System.out.println(mc.getClass().newInstance());

        //prompt to enter a value and read

//        Scanner sc = new Scanner(System.in);
//        String first = sc.nextLine();
//
//        String second = sc.nextLine();
//
//        System.out.println(first +   second);


        //Class is also a class. Instances of class Class represent the classes and interfaces in running java app
        System.out.println(String.class);
        Class clasz = Class.forName("java.lang.String");
        String s = (String) clasz.newInstance();
        System.out.println("===" + s);


     //   lockModule();


       // scanner();
        cyclicBarrier();

       // Geek g1 = new Geek(); cannot invoke because no default constructor found
       int i = 12/0;

    }

    private static void scanner() {
        Scanner sc = new Scanner(System.in);
        String first = sc.next();

        System.out.println(first);

        String second = sc.next();

        System.out.println(second);
    }


    private static void cyclicBarrier() {

        CyclicBarrier barrier = new CyclicBarrier(2, ()->{
            System.out.println("barrier runnable");
        });
        Thread t1 = new Thread(new Task(barrier));

        Thread t2 = new Thread(new Task(barrier));


        t1.start();
        t2.start();
    }

    public static class Task implements Runnable {

        private CyclicBarrier barrier;

        public Task(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            int sleeptime = RandomUtils.nextInt()/100000;

            System.out.println("Thread name = " + Thread.currentThread().getName() + "Sleeping for " + sleeptime);
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("Thread name wokenup  = " + Thread.currentThread().getName());
        }
    }


    public class MyThread extends Thread {

        @Override
        public void run() {

        }
    }

    public class MyLock {

        boolean inUse = true;
        String heldBy = "";
        public  synchronized void lock() throws InterruptedException {
            while (inUse && !Thread.currentThread().getName().equals(heldBy)){
                wait();
            }
            inUse=true;
            heldBy=Thread.currentThread().getName();
        }

        public synchronized void unlock() {

        }
    }

    static class Geek
    {
        int num;
        String name;

        // this would be invoked while an object
        // of that class is created.
        Geek(String name)
        {
            System.out.println("Constructor called");
        }

        Geek(String name, String num) {
            this(name);

        }
    }

//    private static void lockModule() {
//        RLock l1 = new RLock();
//
//        WLock l2 = new WLock();
//
//
//        Thread t1 = new Thread(()->{
//            System.out.println("thread1");
//            try {
//                if(!l1.isLocked()) {
//                    l1.acquireLock();
//                    Thread.sleep(3000);
//                    System.out.println("locking for l2 - " + System.currentTimeMillis());
//                    l2.acquireLock();
//                    System.out.println("acquired lock from l2 - " + System.currentTimeMillis());
//                    System.out.println("lock state of l2 is  " + l2.isLocked());
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        Thread t2 = new Thread(()->{
//            System.out.println("thread2");
//            try {
//                if(!l2.isLocked()) {
//                    l2.acquireLock();
//                    Thread.sleep(10000);
//                    l2.releaseLock();
////                    System.out.println("locking for l1 - " + System.currentTimeMillis());
////                    // l1.acquireLock();
////                    System.out.println("lock state of l1 is " + l1.isLocked());
//
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        t1.start();
//        t2.start();
//
//    }
//
//
//    private static  class RLock {
//        private boolean locked;
//        private String lockedBy; //use this field to be reentrant
//
//        public synchronized void  acquireLock() throws InterruptedException {
//            while(locked && lockedBy != Thread.currentThread().getName())
//                this.wait();
//
//            this.locked=true;
//            this.lockedBy=Thread.currentThread().getName();
//        }
//
//        public synchronized void  releaseLock() {
//            this.locked = false;
//            this.notifyAll();
//        }
//
//        public synchronized boolean isLocked() {
//            return  locked;
//        }
//    }
//
//    private static  class WLock {
//        private boolean locked;
//        private String lockedBy;
//
//        public synchronized void  acquireLock() throws InterruptedException {
//                while(locked && lockedBy != Thread.currentThread().getName())
//                    this.wait();
//
//                this.locked = true;
//        }
//
//        public synchronized void  releaseLock() {
//            this.locked = false;
//            this.notifyAll();
//        }
//
//        public synchronized boolean isLocked() {
//            return  locked;
//        }
//    }
}

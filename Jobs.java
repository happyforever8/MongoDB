Job data structure:
        - ID field.
        - run() method
Job queue data structure:
        - euqueue(job) add the job to queue
         - runJobs() run all the jobs in queue
Follow up: can we run jobs a‍‍‍‍‍‍‍‍‌‍‌‌‍‌‌‌‌‌sync?
What if job fails, how to handle that?
What if job has sub-job needs to add to queue?

  import java.util.LinkedList;
import java.util.Queue;

// Job interface with an ID field and a run() method
interface Job {
    int getID();
    void run();
}

// Sample Job implementation
class SampleJob implements Job {
    private int id;

    public SampleJob(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void run() {
        System.out.println("Running Job ID: " + id);
        // Your job logic here
    }
}

// JobQueue class to enqueue and run jobs
class JobQueue {
    private Queue<Job> queue;

    public JobQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(Job job) {
        queue.offer(job);
    }

    public void runJobs() {
        while (!queue.isEmpty()) {
            Job job = queue.poll();
            job.run();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Create a job queue
        JobQueue jobQueue = new JobQueue();

        // Enqueue some jobs
        jobQueue.enqueue(new SampleJob(1));
        jobQueue.enqueue(new SampleJob(2));
        jobQueue.enqueue(new SampleJob(3));

        // Run all the jobs in the queue
        jobQueue.runJobs();
    }
}
Running Jobs Asynchronously:

To run jobs asynchronously, we can use Java's ExecutorService and Future to manage the execution of jobs. Here's an updated version of the JobQueue class:

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class JobQueue {
    private Queue<Job> queue;
    private ExecutorService executorService;

    public JobQueue() {
        queue = new LinkedList<>();
        executorService = Executors.newFixedThreadPool(5); // Adjust the pool size as needed
    }

    public void enqueue(Job job) {
        queue.offer(job);
    }

    public void runJobs() {
        while (!queue.isEmpty()) {
            Job job = queue.poll();
            Future<?> future = executorService.submit(() -> {
                try {
                    job.run();
                } catch (Exception e) {
                    // Handle job failure here
                    System.err.println("Job failed: " + job.getID());
                }
            });
        }
        executorService.shutdown();
    }
}


Sub-Jobs and Adding to Queue:

If a job needs to add sub-jobs to the queue, you can modify the run() method of the Job interface to include logic for adding sub-jobs. For example:

class SampleJob implements Job {
    private int id;

    public SampleJob(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void run() {
        System.out.println("Running Job ID: " + id);
        // Your job logic here

        // Example: Add sub-jobs to the queue
        for (int i = 0; i < 3; i++) {
            enqueue(new SampleJob(id * 10 + i));
        }
    }
}
By using an ExecutorService for asynchronous execution, handling exceptions, and allowing jobs
  to add sub-jobs to the queue, you have a more robust job management system that 
  can handle a variety of scenarios in a flexible manner. Adjust the thread pool 
  size and error handling as needed for your use case.


  在 Java 中，ExecutorService 和 Future 通常一起使用来异步执行任务并能够获取任务的结果。ExecutorService 提供了一个框架来异步地执行任务，而 Future 提供了一种检查任务是否完成并获取其结果的方式。

使用 ExecutorService 和 Future
创建 ExecutorService：首先，创建一个 ExecutorService 实例。这可以通过 Executors 工具类来完成。
  例如，Executors.newFixedThreadPool(nThreads) 创建一个固定数量线程的线程池。

提交任务：使用 ExecutorService 提交任务（Callable 或 Runnable）。
  提交后，会返回一个 Future 对象。Callable 任务能返回结果，而 Runnable 不返回。

获取结果：通过调用 Future 对象的 get() 方法来检索 Callable 任务的结果。该方法会阻塞，直到任务完成并返回结果。

关闭 ExecutorService：最后，一旦任务完成，应该关闭 ExecutorService 来释放资源。

  import java.util.concurrent.*;

public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交 Callable 任务，并获取 Future 对象
        Future<Integer> futureTask = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // 执行一些计算
                TimeUnit.SECONDS.sleep(2);
                return 123;
            }
        });

        // 当任务还在运行时，可以执行其他操作

        // 使用 futureTask.get() 获取结果，此方法会阻塞直到任务完成
        Integer result = futureTask.get();
        System.out.println("Result of FutureTask: " + result);

        // 最后，一定要关闭 ExecutorService
        executor.shutdown();
    }
}


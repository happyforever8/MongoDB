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

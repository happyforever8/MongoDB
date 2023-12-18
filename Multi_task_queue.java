实现一个多线程任务队列（Task Queue）的目标是让多个工作线程能够并发地执行任务。为了实现这一点，
可以使用 Java 中的 ExecutorService，这是一个高级工具，专门用于处理线程池和任务执行。以下是一个基本的多线程任务队列的实现示例：

===============首先，定义一个简单的任务类（比如 Runnable）:
public class Task implements Runnable {
    private final int taskId;

    public Task(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Executing task " + taskId + " by " + Thread.currentThread().getName());
        // 这里可以添加实际的任务逻辑
    }
}

===============然后，创建一个任务队列和工作线程池：
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskQueue {
    private final ExecutorService executor;

    public TaskQueue(int numberOfThreads) {
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void submitTask(Task task) {
        executor.execute(task);
    }

    public void shutdown() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        TaskQueue queue = new TaskQueue(4); // 创建一个具有4个工作线程的队列

        // 提交任务到队列
        for (int i = 0; i < 10; i++) {
            queue.submitTask(new Task(i));
        }

        queue.shutdown(); // 完成任务后关闭线程池
    }
}

TaskQueue 类使用 ExecutorService 来管理一个固定大小的线程池。
submitTask 方法将 Task 实例提交给 ExecutorService 以异步执行。
shutdown 方法优雅地关闭线程池，首先禁止提交新任务，然后等待已提交的任务完成。
这个例子中，Task 类实现了 Runnable 接口，代表一个可以在线程中执行的任务。
  TaskQueue 类管理一个线程池，并且提供了方法来提交任务和关闭线程池。这个实现是多线程安全的，
因为 ExecutorService 内部处理了所有与线程管理和任务调度相关的同步细节。

  Java 中的 ExecutorService 是一个关键的并发工具，用于管理线程池以及异步地执行任务。
  它是 java.util.concurrent 包的一部分，提供了一种将任务的提交与每个任务的运行方式（例如，在哪个线程上运行、任务排队策略等）解耦的方法。ExecutorService 是 Executor 接口的一个扩展，添加了一些用于生命周期管理的方法。

主要特点
任务执行: ExecutorService 可以执行 Runnable 和 Callable 接口的实现，这两种接口分别代表没有返回值和有返回值的任务。

线程池管理: 它管理一个线程池，自动为提交的任务分配线程，并处理线程的生命周期。

生命周期管理: 提供方法来关闭服务，包括优雅关闭（等待任务完成）和立即关闭（中断所有正在执行的任务）。

结果检索: 对于提交的 Callable 任务，ExecutorService 可以返回一个 Future 对象，用于检查任务是否完成，并检索其计算结果。

  核心方法
submit(Runnable task): 提交一个 Runnable 任务以执行，并返回一个表示该任务的 Future。
submit(Callable<T> task): 提交一个返回值的 Callable 任务以执行，并返回一个 Future，用于获取计算结果。
invokeAll(Collection<? extends Callable<T>> tasks): 执行给定的任务集合，当所有任务完成时，返回保持任务状态和结果的 Future 列表。
invokeAny(Collection<? extends Callable<T>> tasks): 执行给定的任务集合，返回任一成功完成的任务的结果。
shutdown(): 优雅地关闭 ExecutorService，不再接受新任务，但已提交的任务会继续执行。
shutdownNow(): 尝试立即停止所有正在执行的任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
使用示例
  import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

public class ExecutorServiceExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交一个Runnable任务
        executor.submit(() -> {
            System.out.println("Running a runnable task.");
        });

        // 提交一个Callable任务并获取结果
        Future<String> future = executor.submit(() -> {
            return "Result of the callable task.";
        });
        System.out.println(future.get()); // 获取并打印Callable任务的结果

        // 关闭ExecutorService
        executor.shutdown();
    }
}
在这个例子中，一个固定大小的线程池被创建来运行任务。submit 方法用于提交任务，shutdown 用于关闭线程池。对于 Callable 任务，我们可以通过 Future 对象检索执行结果。

ConnectionPool
  
Atlas team 下的
要求实现  ConnectionPool的class，能够重复使用connection
Each request to get a connection should take one from the pool or create a new one if the pool is empty. The pool must be thread-safe
然后给了一个connection的class
class Connection {
    public Connection() {}
    // Must be called once before any read/write calls.
    // This is an expensive operation.
    public void open() {}
    public String read() {
        return null;
    }
    public void write(String data) {}
    // After close is called, read/write may not be called.
    public‍‍‍‍‍‍‍‍‌‍‌‌‍‌‌‌‌‌ void close() {}
}

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

class ConnectionPool {
    private ConcurrentLinkedQueue<Connection> pool;
    private AtomicInteger createdConnectionsCount;
    private final int MAX_CONNECTIONS;

    public ConnectionPool(int maxConnections) {
        this.pool = new ConcurrentLinkedQueue<>();
        this.createdConnectionsCount = new AtomicInteger(0);
        this.MAX_CONNECTIONS = maxConnections;
    }

    public Connection getConnection() {
        Connection conn;

        // 从池中尝试获取连接
        conn = pool.poll();
        if (conn != null) {
            return conn;
        }

        // 如果池为空，检查是否可以创建新连接
        if (createdConnectionsCount.get() < MAX_CONNECTIONS) {
            if (createdConnectionsCount.incrementAndGet() <= MAX_CONNECTIONS) {
                conn = new Connection();
                conn.open(); // 昂贵的操作，仅在创建新连接时调用
                return conn;
            } else {
                createdConnectionsCount.decrementAndGet();
            }
        }

        // 如果无法创建新连接，等待其他线程归还连接
        while ((conn = pool.poll()) == null) {
            try {
                Thread.sleep(100); // 简单的等待策略
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for a connection", e);
            }
        }

        return conn;
    }

  使用 ConcurrentLinkedQueue 来管理 Connection 对象，确保线程安全。
使用 AtomicInteger 来追踪已创建的连接数量，确保不超过最大连接数。
当连接池为空且未达到最大连接数时，创建新的 Connection 对象，并调用它的 open() 方法。
当连接池为空且已达到最大连接数时，线程将等待直到有连接被归还。
这种实现确保了线程安全，同时通过延迟初始化和适当的等待策略，减少了对昂贵 open() 操作的调用次数。

1. 使用线程安全的集合：对于连接池本身，使用线程安全的集合（如 ConcurrentLinkedQueue）可以避免对整个池加锁。这样，线程可以并发地从池中获取和返回连接，而不需要额外的同步。
2. 双重检查锁定（Double-Checked Locking）：在创建新连接时，使用双重检查锁定模式。首先检查是否需要创建新连接，
  如果需要，再进行同步。这样，在大多数情况下（即连接池中有可用连接时），可以避免加锁。



      Connection conn = pool.poll();
    if (conn == null) {
        synchronized (this) {
            if (createdConnectionsCount.get() < MAX_CONNECTIONS) {
                if (createdConnectionsCount.incrementAndGet() <= MAX_CONNECTIONS) {
                    conn = new Connection();
                    conn.open(); // 昂贵的操作，仅在创建新连接时调用
                    return conn;
                } else {
                    createdConnectionsCount.decrementAndGet();
                }
            }
        }
    }
  
  

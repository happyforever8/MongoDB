import java.util.LinkedHashMap;
import java.util.Map;

get and put operations, which mostly have average-case O(1) time complexity, but

public class LinearTimeHashMap<K, V> {
    private LinkedHashMap<K, V> map;

    public LinearTimeHashMap() {
        map = new LinkedHashMap<>();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public static void main(String[] args) {
        LinearTimeHashMap<String, Integer> map = new LinearTimeHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        System.out.println("Value for 'one': " + map.get("one")); // 1
        System.out.println("Value for 'two': " + map.get("two")); // 2
        System.out.println("Value for 'four': " + map.get("four")); // null

        map.remove("two");
        System.out.println("Value for 'two' after removal: " + map.get("two")); // null
    }
}


===============================================================
hashmap 实现

  import java.util.ArrayList;
import java.util.List;

class Entry<K, V> {
    K key;
    V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public class HashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private List<Entry<K, V>>[] buckets;
    private int capacity;

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    public HashMap(int capacity) {
        this.capacity = capacity;
        buckets = new ArrayList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));
    }

    public V get(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                return;
            }
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        System.out.println("Value for 'one': " + map.get("one")); // 1
        System.out.println("Value for 'two': " + map.get("two")); // 2
        System.out.println("Value for 'four': " + map.get("four")); // null

        map.remove("two");
        System.out.println("Value for 'two' after removal: " + map.get("two")); // null
    }
}

========================
  rehash
当HashMap中的元素数量达到负载因子（load factor）的限制时，触发扩容操作。负载因子是元素数量与桶（buckets）数量的比率。

创建一个新的桶数组，通常是当前桶数量的两倍（或其他倍数），然后将所有已存在的键值对重新分配到新的桶中。这个过程称为rehashing。

更新HashMap的内部状态，包括桶数组的大小、负载因子和已存储的元素数量

  import java.util.ArrayList;
import java.util.List;

class Entry<K, V> {
    K key;
    V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public class HashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75; // 负载因子
    private List<Entry<K, V>>[] buckets;
    private int capacity;
    private int size;

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    public HashMap(int capacity) {
        this.capacity = capacity;
        buckets = new ArrayList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2; // 扩容为当前容量的两倍
        List<Entry<K, V>>[] newBuckets = new ArrayList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new ArrayList<>();
        }

        // 重新分配键值对到新桶中
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = Math.abs(entry.key.hashCode()) % newCapacity;
                newBuckets[newIndex].add(entry);
            }
        }

        // 更新内部状态
        buckets = newBuckets;
        capacity = newCapacity;
    }

    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize(); // 触发扩容
        }

        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
    }

       public V get(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        // 添加多个键值对，当负载因子超过阈值时会自动扩容
    }
}
在处理哈希冲突时，通常有几种常见的方法，包括使用链地址法（Chaining）和开放地址法（Open Addressing）。对于每种方法，可以选择不同的数据结构来保存冲突的键值对。同时，哈希算法的选择也会影响冲突的频率和处理方法。
    链地址法（Chaining）：
        数据结构：每个哈希桶是一个链表或其他容器（例如，红黑树）。冲突的键值对被存储在同一个桶内。
        处理冲突：当发生冲突时，将新的键值对附加到桶中。查找时，需要在桶内进行线性搜索或使用更高效的数据结构（如红黑树）来查找特定键。
        适用场景：适用于处理频繁的冲突，允许桶内存储多个键值对的情况。
    
    开放地址法（Open Addressing）：
        数据结构：哈希表中的每个位置都是一个键值对。冲突的键值对尝试存储在不同的位置，直到找到一个可用的位置。
        处理冲突：当发生冲突时，通过探测序列（如线性探测、二次探测、双重散列等）来找到下一个可用的位置。查找时也需要按照相同的探测序列来查找。
        适用场景：适用于处理较少冲突的情况，对存储空间要求较高的情况。
        哈希算法的取舍：

均匀性：选择一个良好的哈希算法是关键，以确保键被均匀分布到哈希桶中，从而降低冲突的概率。
碰撞减少策略：在选择哈希算法时，可以考虑使用一些碰撞减少策略，例如哈希链表的长度阈值，以及对哈希键进行哈希两次（双重哈希）等。
哈希函数的散列性能：哈希函数的性能也很重要，应选择能够快速计算哈希值的算法，以提高插入和查询性能。
总之，处理哈希冲突的方法、数据结构选择和哈希算法的取舍取决于具体的应用需求和数据特征。在设计自己的HashMap时，需要综合考虑这些因素，并根据实际情况做出选择。同时，也可以根据使用的编程语言和标准库提供的HashMap实现，以避免自己实现的复杂性。

红黑树使用的是**链地址法（Chaining）**来处理哈希冲突，具体来说，它使用链表或树结构来存储具有相同哈希值的键值对。这是因为红黑树是一种自平衡的二叉搜索树，可以有效地管理具有相同哈希值的键值对，确保这些键值对在树中保持平衡，从而提供高效的查找、插入和删除操作。
当多个键映射到同一个哈希桶时，红黑树可以作为链地址法的一种变种来使用。对于同一个哈希桶内的键值对，红黑树保持了它们的有序性，并提供了O(log n)级别的插入、查找和删除操作，这是非常高效的。

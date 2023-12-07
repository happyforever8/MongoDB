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

    // 其他方法（get、remove等）的实现与之前的示例相似

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        // 添加多个键值对，当负载因子超过阈值时会自动扩容
    }
}


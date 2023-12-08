import java.util.HashMap;
import java.util.Map;


LinkedHashMap 使用了双向链表来维护插入顺序，链表中的节点数量也等于键值对的数量，因此链表的空间复杂度也是 O(n)。
综合起来，LinkedHashMap 的总体空间复杂度是 O(n)。

  需要注意的是，这个简单的实现并没有包括 LRU（最近最少使用）缓存淘汰策略，
  如果需要支持 LRU 策略，还需要在 removeTail 方法中实现节点的移除和在 get 操作中处理节点的移动。

  
class Node<K, V> {
    K key;
    V value;
    Node<K, V> prev;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public class LinkedHashMap<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private Node<K, V> head;
    private Node<K, V> tail;

    public LinkedHashMap(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addToHead(newNode);

            if (map.size() > capacity) {
                Node<K, V> removed = removeTail();
                map.remove(removed.key);
            }
        }
    }

    public V get(K key) {
        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            moveToHead(node);
            return node.value;
        }
        return null;
    }

    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    private Node<K, V> removeTail() {
        Node<K, V> removed = tail.prev;
        removeNode(removed);
        return removed;
    }
}

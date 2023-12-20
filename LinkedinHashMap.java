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
==========lru===============

  //class LRUCache {

// linkedinhashmap is the ordering of the elements. 
// LinkedHashMap<Integer, Integer> mCache;
//Time complexity for get() and put() operations is Big O(1).
// int mCapacity = 0;
// public LRUCache(int capacity) {
//     mCache = new LinkedHashMap<>();
//     mCapacity = capacity;
// }
// public int get(int key) {
//     if (mCache.containsKey(key)) {
//         int value = mCache.get(key);
//         mCache.remove(key);
//         mCache.put(key, value);
//         return mCache.get(key);
//     }
//     return -1;
// }
// public void put(int key, int value) {
//     if (mCapacity == 0) {
//         return;
//     }
//     if (mCache.containsKey(key)) {
//         mCache.remove(key);
//     } else {
//         if (mCache.size() >= mCapacity) {
//             Entry<Integer, Integer> entry = mCache.entrySet().iterator().next();
//             mCache.remove(entry.getKey());
//         }
//     }
//     mCache.put(key, value);
// }


    class LRUCache {
    int capacity;
    Map<Integer, DoubleLinkedList> map;
    DoubleLinkedList head;
    DoubleLinkedList tail;
    int count;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        count = 0;
        
        map = new HashMap<>();
        head = new DoubleLinkedList(0, 0);
        tail = new DoubleLinkedList(0, 0);
        
        head.next = tail;
        head.prev = null;
        tail.next = null;
        tail.prev = head;
    }
    
    public int get(int key) {
        if (!map.containsKey(key)){
            return -1;
        }
        
        DoubleLinkedList node = map.get(key);
        
        int result = node.value;
        remove(node);
        addHead(node);
        return result;
    }
    public void remove(DoubleLinkedList node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    public void addHead(DoubleLinkedList node){
        DoubleLinkedList next = head.next;
        
        node.next = next;
        next.prev = node;
        
        node.prev = head;
        head.next = node;
    }
    public void put(int key, int value) {
        DoubleLinkedList node = new DoubleLinkedList(key, value);
        
        if (map.containsKey(key)){
            remove(map.get(key)); // should remove first
            map.put(key, node);
            
            addHead(node);
        } else {
            
            if (count < capacity){
                map.put(key, node);
                addHead(node);
                count++;
            } else {
                DoubleLinkedList last = tail.prev;
                
                map.remove(last.key);
                remove(last); // remeber to remove last one
                addHead(node);
                map.put(key, node);
            }
        }
    }
    class DoubleLinkedList{
        int key;
        int value;
        DoubleLinkedList prev;
        DoubleLinkedList next;
        
        public DoubleLinkedList(int key, int value){
            this.key = key;
            this.value = value;
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

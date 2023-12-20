实现含有snapId 的 数据结构, 要求实现以下功能 insert, delete

  /**  * Implement a data structure with functionality similar to a hash table. In our custom data structure, 
  Insert & delete operation will return a snapID and we pass on the snapID for lookup. 
  *  * For instance say DS is our custom data structure  *  * Operation  * Result  
  * DS.insert(k1,v1) -> snap1  * DS.insert(k2,v2) -> snap2 
  * DS.delete(k1) -> snap3  * DS.insert(k1,v3) ->  snap4 
  * DS.hasKey(k1,snap1) -> True 
  * DS.hasKey(k1,snap2) -> True
  * DS.hasKey(k1,snap3) -> False 
  * DS.hasKey(k1,snap4) -> true  *  * 
  * DS.getVal(k1, snap1) -> v1  
  * DS.getVal(k1, snap4) -> v3  *  */
  
    public class SnapMap {

  //  new HashMap<>(base) 创建了 base 映射的一个浅拷贝。
  // 这意味着每个快照 (snapVersion) 都保存了当时 base 映射的一个独立副本。
  // 所以，当你检索特定版本的快照时，你能够得到在那个特定时间点 base 映射的状态。
        private static final String DELETED = "deleted";
        static int snapVersion = 0;
        static Map < Integer, Map < String, String >> snap2KeyValue = new HashMap < > ();
        static Map < String, String > base = new HashMap < > ();
        public static Integer insert(String key, String value) {
            snapVersion++;
            base.put(key, value);
            snap2KeyValue.put(snapVersion, new HashMap < > (base));
            return snapVersion;
        }
        public static Integer delete(String key) {
            snapVersion++;
            base.remove(key);
            snap2KeyValue.put(snapVersion, new HashMap < > (base));
            return snapVersion;
        }
        public static boolean hasKey(String key, int snapVersion) {
            return snap2KeyValue.containsKey(snapVersion) && snap2KeyValue.get(snapVersion).containsKey(key);
        }
        public static String getValue(String key, int snapVersion) {
            if (hasKey(key, snapVersion)) {
                return snap2KeyValue.get(snapVersion).get(key);
            }
            return null;
        }
        public static void main(String[] args) {
            int v1 = SnapMap.insert("k1", "v1");
            int v2 = SnapMap.insert("k2", "v2");
            int v3 = SnapMap.delete("k1");
            int v4 = SnapMap.insert("k1", "v3");
            System.out.println(SnapMap.hasKey("k1", v1));
            System.out.println(SnapMap.hasKey("k1", v2));
            System.out.println(SnapMap.hasKey("k1", v3));
            System.out.println(SnapMap.hasKey("k1", v4));
            System.out.println(SnapMap.getValue("k1", v1));
            System.out.println(SnapMap.getValue("k1", v4));
        }
    }
}

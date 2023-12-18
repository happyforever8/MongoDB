User
Create a data structure to contain objects of the following structure:
Examples of JSON language
“foo”
{“a”: “foo”, “b”: “bar”}
{“b”: [“foo”, “bar”]}
{“a”: {“b”: [{“c”: “foo”, “d”: {}, “e”: “bar”, “f”: {“g”: “baz”}}]}}
{“a”: [{“b”: “foo”}, {“c”: “bar”}, {“b”: “baz”}]}
[“val1”, “val2”, {“a”: “foo”}]
Implement a method to resolve a path (represented as a list of strings) to a list of all values found at that path (returned as a list of objects).
Examples of path traversal  “foo” and “a” -> nothing
{“a”: “foo”} and “a” -> return “foo”
{a: {b: “foo”}} and “a” -> return {b: “foo”}
{“a”: {“b”: [{“c”: “foo”, “d”: {}, “e”: “bar”}]}} and the path [“a”,”b”, “c”] should yield “foo”
{“a”: {“b”: [{“c”: “foo”, “d”: {}, “e”: “bar”}, {“c”: “baz”}]}} and the path [“a”,”b”, “c”] should yield “foo”, and “baz”
{a: [{b: [{c: "foo"}, {c: "bar"} ]}, {b: [{c: "blah"}, {c: ["baz"]]}]}]} and the path ["a", "b", "c"] should produce: ["foo", "bar", "blah", ["baz"]]
Note that we're‍‍‍‍‍‍‍‍‌‍‌‌‍‌‌‌‌‌ not returning "baz" by itself here.

  import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonPathResolver {

  // worst case time， same as space
  // In the worst case, if the path has p elements and at each level, the JSON object is a list with n elements, 
  // the time complexity can reach O(n^p) because the method explores every path in the JSON structure

    // Method to resolve a path in a JSON-like object
    public static List<Object> resolvePath(Object json, List<String> path) {
        List<Object> result = new ArrayList<>();
        resolvePathRecursive(json, path, 0, result);
        return result;
    }

    // Recursive helper method
    private static void resolvePathRecursive(Object current, List<String> path, int index, List<Object> result) {
        if (index == path.size()) {
            result.add(current);
            return;
        }

        if (current instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) current;
            Object next = map.get(path.get(index));
            if (next != null) {
                resolvePathRecursive(next, path, index + 1, result);
            }
        } else if (current instanceof List) {
            List<?> list = (List<?>) current;
            for (Object item : list) {
                resolvePathRecursive(item, path, index, result);
            }
        }
    }

    public static void main(String[] args) {
        // Example usage
        Map<String, Object> json = Map.of(
                "a", Map.of(
                        "b", List.of(
                                Map.of("c", "foo", "d", Map.of(), "e", "bar"),
                                Map.of("c", "baz")
                        )
                )
        );

        List<String> path = List.of("a", "b", "c");
        List<Object> values = resolvePath(json, path);

        System.out.println(values); // Should print: [foo, baz]
    }
}

// update Json
//给一个 original json,  一个delta json， 把delta json 里的field overwrite 在 
//original json 上，包括添加新的key value pair，update 已有key 的 value。


//The worst-case scenario is when every key in the delta JSON leads to a nested JSON object. 
// If the delta JSON has d keys and the maximum depth of nested objects is m, 
// the worst-case time complexity can be approximated to O(d * n^m),
//   where n is the average size of nested JSON objects.

// space is 
// The space complexity is influenced by the recursive call stack,
//   which can grow up to the maximum depth of the nested JSON objects, O(m).
import java.util.Map;

public class JsonUpdater {

    public static void update(Map<String, Object> originalJson, Map<String, Object> deltaJson) {
        for (String key : deltaJson.keySet()) {
            Object deltaValue = deltaJson.get(key);
            if (originalJson.containsKey(key)) {
                Object originalValue = originalJson.get(key);
                if (originalValue instanceof Map && deltaValue instanceof Map) {
                    // Recursive update if both are JSON objects
                    update((Map<String, Object>) originalValue, (Map<String, Object>) deltaValue);
                } else {
                    // Overwrite the value
                    originalJson.put(key, deltaValue);
                }
            } else {
                // Add new key-value pair
                originalJson.put(key, deltaValue);
            }
        }
    }

    public static void main(String[] args) {
        // Example usage
        Map<String, Object> originalJson = Map.of(
                "a", Map.of("b", "foo"),
                "c", "bar"
        );

        Map<String, Object> deltaJson = Map.of(
                "a", Map.of("b", "baz"),
                "d", "new value"
        );

        update(originalJson, deltaJson);
        System.out.println(originalJson); // Should print the updated JSON
    }
}


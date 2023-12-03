import java.util.*;

public class DocumentSearch {

    private Map<String, Set<String>> index;

    public DocumentSearch() {
        index = new HashMap<>();
    }

    public void add(String doc) {
        String[] words = doc.split("\\s+");
        for (String word : words) {
            word = word.toLowerCase();
            index.computeIfAbsent(word, k -> new HashSet<>()).add(doc);
        }
    }

    public List<String> search(String word) {
        word = word.toLowerCase();
        Set<String> docs = index.get(word);
        return docs != null ? new ArrayList<>(docs) : new ArrayList<>();
    }

    public void delete(String doc) {
        String[] words = doc.split("\\s+");
        for (String word : words) {
            word = word.toLowerCase();
            Set<String> docs = index.get(word);
            if (docs != null) {
                docs.remove(doc);
                if (docs.isEmpty()) {
                    index.remove(word);
                }
            }
        }
    }

    public List<String> advancedSearch(List<String> words, String operator) {
        Set<String> result = new HashSet<>();
        if (operator.equalsIgnoreCase("AND")) {
            for (String word : words) {
                word = word.toLowerCase();
                if (result.isEmpty()) {
                    // Initialize result with the first word's documents
                    Set<String> docs = index.get(word);
                    if (docs != null) {
                        result.addAll(docs);
                    } else {
                        // If the first word is not found, there can be no intersection
                        return new ArrayList<>();
                    }
                } else {
                    // Intersect result with the next word's documents
                    result.retainAll(index.get(word));
                }
            }
        } else if (operator.equalsIgnoreCase("OR")) {
            for (String word : words) {
                word = word.toLowerCase();
                Set<String> docs = index.get(word);
                if (docs != null) {
                    result.addAll(docs);
                }
            }
        }
        return new ArrayList<>(result);
    }

    // 示例用法
    public static void main(String[] args) {
        DocumentSearch searchEngine = new DocumentSearch();
        searchEngine.add("Pizza delivery service");
        searchEngine.add("Pizza is delicious");

        System.out.println(searchEngine.search("pizza")); // 返回包含 "pizza" 的所有文档

        searchEngine.delete("Pizza is delicious");
        System.out.println(searchEngine.search("pizza")); // 删除后再次返回包含 "pizza" 的所有文档

        List<String> words = Arrays.asList("pizza", "delivery");
        System.out.println(searchEngine.advancedSearch(words, "AND")); // 高级搜索 "pizza" 和 "delivery"
    }
}

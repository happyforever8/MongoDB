

class Solution {
    public int[] intersection(int[] arr1, int[] arr2) {
		Set<Integer> se=new HashSet<>();
		for(int i=0;i<arr1.length;i++){
			se.add(arr1[i]);
		}
		List<Integer> li=new ArrayList<>();
		for(int i=0;i<arr2.length;i++){
			int a=arr2[i];
			if(se.contains(a)){
				li.add(a);
				se.remove(a);
			}
		}
		int[] ans=new int[li.size()];
		for(int i=0;i<ans.length;i++){
			ans[i]=li.get(i);
		}
		return ans;
	}
}

using stream
  Streams, in general, do not store intermediate results in memory unless explicitly collected into a data structure like a list or se

      public static List<Integer> findIntersection(List<Integer> list1, List<Integer> list2) {
        return list1.stream()
                .distinct()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }
Streams, in general, do not store intermediate results in memory unless explicitly collected into a data structure like a list or set.
  
Intermediate stream operations, such as filter and map, do not create new collections but return new streams that are linked to the original stream. 
  Therefore, they do not consume extra memory for intermediate results.
  
Terminal operations, like collect or toList, may consume additional memory depending on the type of collection used to store the result.
Operations like forEach or reduce do not create new collections and have constant space complexity.

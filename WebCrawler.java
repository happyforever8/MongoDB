onsite code 2：实习爬虫 - 主要是爬取网页中的contact，用了linkedList, 
  面试官跟进要求实现在短时间内爬取更多contact，经提示改用 ‍‍‍‍‍‍‍‍‌‍‌‌‍‌‌‌‌‌priorityQueue，把contact多的页面往前排

  public class WebCrawler {
    List < Contact > collectContactInformation(String url) {
        Set < Page > set = new HashSet < > ();
        Queue < Page > queue = new LinkedList < > ();
        Page page = downloadPageByAddress(url);
        queue.offer(page);
        set.add(page);
        List < Contact > result = new ArrayList < > ();
        while (!queue.isEmpty()) {
            Page current = queue.poll();
            for (String tempUrl: current.getUrls()) {
                Page tempPage = downloadPageByAddress(tempUrl);
                if (!set.contains(tempPage)) {
                    queue.offer(tempPage);
                    set.add(tempPage);
                }
            }
            result.addAll(current.getContactInformation());
        }
        return result;
    } // This method is ALREADY implemented:    
       Page downloadPageByAddress(String url) {       
      // ... fetch the page from the Internet        
         return new Page();     }     
  
  class Page {         
    // This method is ALREADY implemented:        
    List<String> getUrls() {            
      // returns urls on the page            
      return new ArrayList<>();        
    }         
    
    // This method is ALREADY implemented:       
    
    List<Contact> getContactInformation() {           
      // returns the contacts found on the page            
      return new ArrayList<>();         }        
    //...     }     
    class Contact {      }  
  }

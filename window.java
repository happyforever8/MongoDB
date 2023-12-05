1. sumWindow
  写一个class，constructor接受一个长度为N的integer array, 一个长度n，n <= N。这个class有一个function，叫next。每call一次next，就返回长度为n的window里面的值的sum，然后window向右移动一位。
比如: arr=[1, 2, 3, 4, 5], n = 2。第一次next，返回1 + 2 = 3。第二次next, 返回2 + 3 = 5. 以此类推

2. prodWindow
  同上。不过是从sum换成了product。这时候需要注意0的问题
3. countWindow
  同上。不过是count这个window多少个item (也就是n)
4. avgWindow
  同上。不过返回average。这时候面试官要求重用上面的window class。所以就是 sumWindow / countWindow
  
class Window {
    private int[] arr;
    private int length;
    private int index;

    private int sum;
    private int prod;
    private int zeroCount;

    public Window(int[] arr, int n) {
        this.arr = arr;
        this.length = n;
        assert this.length < this.arr.length;

        this.index = 0;
        this.sum = 0;
        this.prod = 1;
        this.zeroCount = 0;

        // Initialize the first window
        for (int i = 0; i < this.length; i++) {
            this.sum += this.arr[i];
            if (this.arr[i] == 0) {
                this.zeroCount++;
            } else {
                this.prod *= this.arr[i];
            }
        }
    }

    public void next() {
        if (this.index + this.length < this.arr.length) {
            // Remove the element leaving the window
            int leaving = this.arr[this.index];
            this.sum -= leaving;
            if (leaving == 0) {
                this.zeroCount--;
            } else {
                this.prod /= leaving;
            }

            // Add the element entering the window
            int entering = this.arr[this.index + this.length];
            this.sum += entering;
            if (entering == 0) {
                this.zeroCount++;
            } else {
                this.prod *= entering;
            }

            this.index++;
        }
    }

    public int sum1() {
        return this.sum;
    }

    public int prod1() {
        return this.zeroCount > 0 ? 0 : this.prod;
    }

    public int count1() {
        return Math.min(this.arr.length - this.index, this.length);
    }

    public double avg1() {
        int count = this.count1();
        return count <= 0 ? 0 : ((double) this.sum) / count;
    }
}

public class TestWindow {
    public static void main(String[] args) {
        // Test cases can be added here
    }
}

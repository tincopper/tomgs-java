# 解题思路总结：
- 对于数组类型
  对于有序的优先考虑滑动窗口
  对于数值类型判断是否有重复、奇/偶数个的，优先考虑异或运算，然后hash表
  对于数组判断其中两个数的和之类的要想到双指针，但是双指针的前提需要考虑数组是否有序，如果没有序，可以先考虑排序。

- 对于字符串
  优先想到字符只有26个字母，定义一个数组用于存放26个字母int[] arr = new int[26];
  然后运用charAt[i] - 'a'的方式去定位在26个字母的位置的方法。
  对于判断字符中是否有重复的字符比用hash表更有效。
  
- 对于成对成对出现的，先进后出的场景优先考虑栈
- 对于遍历有时候顺着不行可以考虑反着来

# 资料总结
[位运算的奇巧淫技](https://cshihong.github.io/2018/12/31/%E4%BD%8D%E8%BF%90%E7%AE%97%E7%9A%84%E5%A5%87%E5%B7%A7%E6%B7%AB%E6%8A%80/)


# 解法类型汇总
## 滑动窗口
一般用于求解子串问题，配合左右指针，一前一后进行窗口的扩容和缩放。
```java
/* 滑动窗口算法框架 */
void slidingWindow(String s) {
    // 用合适的数据结构记录窗口中的数据
    HashMap<Character, Integer> window = new HashMap<>();

    int left = 0, right = 0;
    while (right < s.length()) {
        // c 是将移入窗口的字符
        char c = s.charAt(right);
        window.put(c, window.getOrDefault(c, 0) + 1);
        // 增大窗口
        right++;
        // 进行窗口内数据的一系列更新
        ...

        /*** debug 输出的位置 ***/
        // 注意在最终的解法代码中不要 print
        // 因为 IO 操作很耗时，可能导致超时
        System.out.printf("window: [%d, %d)\n", left, right);
        /********************/

        // 判断左侧窗口是否要收缩
        while (left < right && window needs shrink) {
            // d 是将移出窗口的字符
            char d = s.charAt(left);
            window.put(d, window.get(d) - 1);
            // 缩小窗口
            left++;
            // 进行窗口内数据的一系列更新
            ...
        }
    }
}
```
滑动窗口算法的思路：
1、我们在字符串 S 中使用双指针中的左右指针技巧，初始化 left = right = 0，把索引左闭右开区间 [left, right) 称为一个「窗口」。
2、我们先不断地增加 right 指针扩大窗口 [left, right)，直到窗口中的字符串符合要求（包含了 T 中的所有字符）。
3、此时，我们停止增加 right，转而不断增加 left 指针缩小窗口 [left, right)，直到窗口中的字符串不再符合要求（不包含 T 中的所有字符了）。同时，每次增加 left，我们都要更新一轮结果。
4、重复第 2 和第 3 步，直到 right 到达字符串 S 的尽头。

LeetCode 题目：
- LC03LengthOfLongestSubstring
- LC76MinWindowSubstring

## 双指针
用于解决子串、链表、数组、字符串问题，一前一后进行指针的移动。

对于链表类双指针，先考虑使用虚拟节点的方式定义一个虚拟链表；
```java
// 定义一个链表头为head，p用于遍历链表的指针
ListNode head = new ListNode(-1), p = head;
```
LeetCode 题目：
- LC21MergeTwoLists
- LC86SplitList
- 

### 左右指针
一般用于解决数组问题，左右两端相向移动，反转数组，回文串的判断，二分搜索等等。
判断回文串，通过左右指针往两边向中心收拢。
```java
public boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;
    while (left < right) {
        if (s.charAt(left) != s.charAt(right)) {
            return false;
        }
        left++;
        right--;
    }

    return true;
}
```
如果是获取回文串字符，则需要中心往外边扩散获取回文串。那么需要先判断是否为回文串，然后找起中点，再往两边扩散，直到左右两边不相等，则表示为最长的回文串。
```java
// 在 s 中寻找以 s[l] 和 s[r] 为中心的最长回文串
private String findPalindrome(String s, int l, int r) {
    // 往两边展开
    while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
        l--;
        r++;
    }
    return s.substring(l + 1, r);
}
```

### 快慢指针
1. 解决链表问题，归并排序找中点，链表成环判定
2. 数组类问题一般用于找两个数的和、原地移动数组的值等等

- LC344ReverseString
- LC27RemoveElement
- LC83DeleteDuplicates
- 

## 二分搜索

## 哈希表

## 异或运算
关于异或的判断奇数还是偶数的性质
异或可以简单理解为无进位加法，比如2的二进制为10，1的二进制为01，1^2的结果为3，二进制为11.
那么通过这个性质可以用来判断奇数还是偶数。
具体内容：
比如一个偶数为6，它的二进制为110；1的二进制为001，那么6^1的结果为7，二进制为111，是不是所有偶数都有这个性质呢？是的，因为偶数的二进制的最后一位一定是0，而且1的二进制的最后一位一定是1，所以所有的偶数异或1的结果都是原来的数字加上1的结果。
比如一个奇数为11，它的二进制为1011，1的二进制为0001，那么11^1的结果为10，二进制为1010.是不是所有的奇数都有这个性质呢？是的，因为奇数的二进制的最后一位一定是1，而且1的二进制的最后一位一定是1，又因为异或运算可以理解为无进位加法，所以所有的奇数异或1的结果都是原来的数字减去1的结果。

## 动态规划

## 前缀和
前缀和主要适用的场景是原始数组不会被修改的情况下，频繁查询某个区间的累加和。

- LC303NumArray
- LC304NumMatrix

## 差分数组
差分数组的主要适用场景是频繁对原始数组的某个区间的元素进行增减。
比如说，我给你输入一个数组 nums，然后又要求给区间 nums[2..6] 全部加 1，再给 nums[3..9] 全部减 3，再给 nums[0..4] 全部加 2，再给...，然后问你，最后 nums 数组的值是什么？
```java
// 差分数组工具类
class Difference {
    // 差分数组
    private int[] diff;
    
    /* 输入一个初始数组，区间操作将在这个数组上进行 */
    public Difference(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 根据初始数组构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    /* 给闭区间 [i, j] 增加 val（可以是负数）*/
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }

    /* 返回结果数组 */
    public int[] result() {
        int[] res = new int[diff.length];
        // 根据差分数组构造结果数组
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        return res;
    }
}
```
- LC1094CarPooling
- LC1109CorporateFlightBookings
- LC370RangeAddition


package com.tomgs.algorithm.base;

import org.junit.Test;

/**
 * @author tangzhongyuan
 * @since 2019-08-07 18:15
 **/
public class BaseTest {

    /**
     * 判断一个数是不是2的幂
     */
    @Test
    public void test0() {
        isPowerOfTwo(3);
        isPowerOfTwo(4);
    }

    private void isPowerOfTwo(int scale) {
        if ((scale & (scale - 1)) != 0) {
            System.out.println(scale + " data type scale not a power of two");
        } else {
            System.out.println(scale + " data type scale is a power of two");
        }
    }

    /**
     * 获取一个整数值的二进制值
     * 如输入int i = 1 输出 0000 0001
     */
    @Test
    public void test1() {
        int i = 6;
        for (int j = 0; j < 32; j++) {
            /*
             * 0x80000000对应的二进制位 1000 0000 0000 0000 0000 0000 0000 0000
             *
             * 没有无符号左移，因为和左移是一样的
             * 注意的是>>>运算的优先级要高于&运算
             */
            int t = (i & 0x80000000 >>> j) >>> (31 -j);
            System.out.print(t);
        }
    }

    @Test
    public void test2() {
        String string = Integer.toBinaryString(~1);
        System.out.println(string);
        System.out.println(~1);

        string = Integer.toBinaryString(-1);
        System.out.println(string);
    }

    /**
     * 汉明距离</br>
     * 求两个整数的二进制中对应位置不同数字的个数
     * input: 1， 4  output: 2
     *
     * 1 : 0 0 0 1
     * 4 : 0 1 0 0
     *
     * https://m.toutiaocdn.com/group/6823987167295963660
     *
     * 采用异或运算的方式，异或之后之前不同的位置现在则为1，所以只需要统计异或之后数值中1的个数即可。
     */
    @Test
    public void test3() {
        int count = hammingDistance(1, 4);
        System.out.println(count);
        count = hammingDistance2(1, 4);
        System.out.println(count);
        count = hammingDistance3(1, 4);
        System.out.println(count);

    }

    // 内置函数实现
    public int hammingDistance(int x, int y) {
        return Integer.bitCount(x ^ y);
    }


    /*
        位移实现
        为了计算等于 1 的位数，可以将每个位移动到最左侧或最右侧，然后检查该位是否为 1。
        更准确的说，应该进行逻辑移位，移入零替换丢弃的位。
        使用最右侧要方便些，只需要对最右侧的数使用取模运算（i % 2）或者 AND 操作（i & 1）即可判断是否为1
        时间复杂度：O(1)，在 Python 和 Java 中 Integer 的大小是固定的，处理时间也是固定的。32 位整数需要 32 次迭代。
        空间复杂度：O(1)，使用恒定大小的空间。
     */
    public int hammingDistance2(int x, int y) {
        int xor = x ^ y;
        int distance = 0;
        while (xor != 0) {
            if ((xor & 1) == 1) {
                distance += 1;
            }
            xor = xor >> 1;
        }
        return distance;
    }

    /*
      布赖恩·克尼根算法
      方法二是逐位移动，逐位比较边缘位置是否为 1。寻找一种更快的方法找出等于 1 的位数。
    是否可以像人类直观的计数比特为 1 的位数，跳过两个 1 之间的 0。例如：10001000。
    上面例子中，遇到最右边的 1 后，如果可以跳过中间的 0，直接跳到下一个 1，效率会高很多。
    这是布赖恩·克尼根位计数算法的基本思想。该算法使用特定比特位和算术运算移除等于 1 的最右比特位。
    当我们在 number 和 number-1 上做 AND 位运算时，原数字 number 的最右边等于 1 的比特会被移除。
        x = 1 0 0 0 1 0 0 0
    x - 1 = 1 0 0 0 0 1 1 1
 x -1 & x = 1 0 0 0 0 0 0 0

    基于以上思路，通过 2 次迭代就可以知道 10001000 中 1 的位数，而不需要 8 次。

    注意：该算法发布在 1988 年 《C 语言编程第二版》的练习中（由 Brian W. Kernighan 和 Dennis M. Ritchie 编写），
    但是 Donald Knuth 在 2006 年 4 月 19 日指出，该方法第一次是由 Peter Wegner 在 1960 年的 CACM3 上出版。顺便说一句，可以在上述书籍中找到更多位操作的技巧。

    复杂度分析
    时间复杂度：O(1)。

    与移位方法相似，由于整数的位数恒定，因此具有恒定的时间复杂度。
    但是该方法需要的迭代操作更少。
    空间复杂度：O(1)。与输入无关，使用恒定大小的空间。
     */
    public int hammingDistance3(int x, int y) {
        int xor = x ^ y;
        int distance = 0;
        while (xor != 0) {
            distance += 1;
            xor = xor & (xor - 1);
        }
        return distance;
    }

    @Test
    public void test4() {
        final int i = 0x7F;
        final int i1 = ~i;
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toBinaryString(i1));

        int n = 104;

        final int i2 = n & i1;
        System.out.println(Integer.toBinaryString(n));
        System.out.println(Integer.toBinaryString(i2));

        int n1 = 300;
        // 取低8位
        System.out.println(Integer.toBinaryString((byte)n1));
        final int i3 = n1 & i1;
        System.out.println(Integer.toBinaryString(n1));
        System.out.println(Integer.toBinaryString(i3));
    }

    private final byte[] i32buf = new byte[]{};
    private void writeVarint32(int n) {
        int idx = 0;
        while (true) {
            // 如果小于128的数，直接取低8位
            if ((n & ~0x7F) == 0) {
                i32buf[idx++] = (byte)n;
                break;
            } else {
                // 步骤1：取出字节串末7位
                // 对于上述取出的7位：在最高位添加1构成一个字节
                // 如果是最后一次取出，则在最高位添加0构成1个字节
                i32buf[idx++] = (byte)((n & 0x7F) | 0x80);
                // 步骤2：通过将字节串整体往右移7位，继续从字节串的末尾选取7位，直到取完为止。
                n >>>= 7;
            }
        }
        // 步骤3： 将上述形成的每个字节 按序拼接 成一个字节串
        // 即该字节串就是经过Varint编码后的字节
        //trans_.write(i32buf, 0, idx);
    }

    @Test
    public void test5() {
        int fat = 2; // 00000010
        int features = 25;// 00011001

        System.out.println(features);
        System.out.println(features & fat);

        features = fat | features;
        System.out.println(features);
        System.out.println(features & fat);

        features = (features & fat) ^ features;
        System.out.println(features);
        System.out.println(features & fat);
    }

}

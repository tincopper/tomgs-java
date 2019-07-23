package com.tomgs.algorithm.array;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @since 2019-07-18 10:34
 **/
public class ArrayTest {

    /**
     * 给定一个整数数组和一个目标值，找出数组中和为目标值的两个数，要求时间复杂度为o(n)
     */
    @Test
    public void test1() {
        int[] nums = {1, 2, 7, 5, 4};
        int target = 8;

        int[] result = findTowSum1(nums, target);
        System.out.println(result[0] + ":" + result[1]);

        int[] result2 = findTowSum2(nums, target);
        System.out.println(result2[0] + ":" + result2[1]);
    }

    /**
     * 首先想到的肯定是拿第一个元素与后面的依次加，看是否等于目标值，
     * 然后再第二个、第三个...
     */
    private int[] findTowSum1(int[] nums, int target) {
        final int[] result = new int[2];
        for (int i = 0; i < nums.length - 1; i++) {
            for (int i1 = i + 1; i1 < nums.length; i1++) {
                if (nums[i] + nums[i1] == target && i != i1) {
                    result[0] = i;
                    result[1] = i1;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 把数值作为 key，它的下标作为 value
     * 遍历数组，判断 map 是否含有这个目标值-当前数值，
     * 有直接返回，没有的话放到map里面
     *
     * 所以以后写代码，如果有双层 for 循环，首先考虑一下能否用 map 替换一层
     */
    private int[] findTowSum2(int[] nums, int target) {
        final int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                final Integer index = map.get(nums[i]);
                result[0] = index;
                result[1] = i;
                return result;
            }
            map.put(target - nums[i], i);
        }
        return result;
    }

}

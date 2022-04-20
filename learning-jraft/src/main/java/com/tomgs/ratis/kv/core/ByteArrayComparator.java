package com.tomgs.ratis.kv.core;

import java.util.Comparator;

/**
 * ByteArrayComparator
 *
 * @author tomgs
 * @since 2022/4/20
 */
public class ByteArrayComparator implements Comparator<byte[]> {

    public int compare(byte[] o1, byte[] o2) {
        int offset1 = 0;
        int offset2 = 0;
        int length1 = o1.length;
        int length2 = o2.length;
        int end1 = offset1 + length1;
        int end2 = offset2 + length2;
        for (int i = offset1, j = offset2; i < end1 && j < end2; i++, j++) {
            int a = (o1[i] & 0xff);
            int b = (o2[j] & 0xff);
            if (a != b) {
                return a - b;
            }
        }
        return length1 - length2;
    }

}

package com.hls;

import java.util.Collections;
import java.util.List;

public class Sort {


    public static void run(List<Integer> list) {
        int l = 0, r = list.size() - 1;
        do_sort(list, l, r);
    }

    private static void do_sort(List<Integer> list, int l, int r) {
        int left = l, right = r;
        while (l <= r) {
            while (list.get(l) < list.get(r)) {
                r--;
            }
            if (l >= r) {
                do_sort(list, left, l - 1);
                do_sort(list, l + 1, right);
                break;
            }
            Collections.swap(list, l, r);
            l++;

            while (list.get(l) < list.get(r)) {
                l++;
            }
            if (l >= r) {
                do_sort(list, left, l - 1);
                do_sort(list, l + 1, right);
                break;
            }
            Collections.swap(list, l, r);
            r--;
        }
    }
}

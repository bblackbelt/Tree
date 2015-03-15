package com.blackbelt.utils;

/**
 * Created by emanuele on 15.03.15.
 */
public class Utils {

    public static long getFirstPrime(long n) {
        if (n <= 2) {
            return 1;
        }
        for (long i = n + 1; ; i++) {
            if (isPrime(i)) {
                return i;
            }
        }
    }

    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        long sqrtN = (long) Math.sqrt(n) + 1;
        for (long i = 6L; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
        }
        return true;
    }

    public static int roundup2(int v) {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;
        return v;
    }

    public static long pow(long a, long n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return a;
        }

        if ((n & 1) == 0) {
            return pow(a * a, n >> 1);
        } else {
            return a * pow(a * a, (n - 1) >> 1);
        }
    }
}

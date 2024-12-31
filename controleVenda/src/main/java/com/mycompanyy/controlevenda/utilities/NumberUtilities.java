package com.mycompanyy.controlevenda.utilities;

import java.math.BigDecimal;

/**
 *
 * @author gabri
 */
public class NumberUtilities {

    public NumberUtilities() {
    }

    public static boolean greatherThanZero(BigDecimal a) {
        if (a.compareTo(BigDecimal.ZERO) == -1
                || a.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        return true;
    }

    public static boolean greatherThan(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) == -1 || a.compareTo(b) == 0) {
            return false;
        }

        return true;
    }
}

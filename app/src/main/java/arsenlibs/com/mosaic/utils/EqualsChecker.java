package arsenlibs.com.mosaic.utils;


public final class EqualsChecker {

    public static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

}

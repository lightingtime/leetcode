// ============================================================
// 本地测试共用的比较工具：只服务于各题目 main 里的自测，
// 不参与力扣提交（提交时只提取 // ==== 提交代码开始 ==== 区间）。
// 生成器：create_problem.js 会让每道题直接调用本类。
// ============================================================
import java.util.*;

public class TestUtil {

    public static boolean checkEq(Object expected, Object actual, String label) {
        return eq(label, norm(expected), norm(actual));
    }

    // 顺序无关比较：数组/List 元素顺序不同也算相等（题目允许任意顺序返回时使用，如两数之和的下标对）
    public static boolean checkEqUnordered(Object expected, Object actual, String label) {
        return eq(label, normUnordered(expected), normUnordered(actual));
    }

    public static boolean eq(String label, String e, String a) {
        if (e.equals(a)) { System.out.println(label + " 通过 ✓"); return true; }
        System.out.println(label + " 失败 ✗ 期望=" + e + " 实际=" + a);
        return false;
    }

    // Map/Set 按排序后的规范形式输出，迭代顺序不影响比较；List/数组保持原有顺序
    public static String norm(Object o) {
        if (o == null) return "null";
        if (o instanceof Map) {
            List<String> es = new ArrayList<>();
            for (Object k : ((Map<?, ?>) o).keySet()) es.add(String.valueOf(k) + "=" + norm(((Map<?, ?>) o).get(k)));
            Collections.sort(es);
            return "{" + String.join(", ", es) + "}";
        }
        if (o instanceof Set) {
            List<String> es = new ArrayList<>();
            for (Object v : (Set<?>) o) es.add(norm(v));
            Collections.sort(es);
            return "[" + String.join(", ", es) + "]";
        }
        if (o instanceof List) {
            List<String> es = new ArrayList<>();
            for (Object v : (List<?>) o) es.add(norm(v));
            return "[" + String.join(", ", es) + "]";
        }
        if (o instanceof int[]) return Arrays.toString((int[]) o);
        if (o instanceof long[]) return Arrays.toString((long[]) o);
        if (o instanceof double[]) return Arrays.toString((double[]) o);
        if (o instanceof boolean[]) return Arrays.toString((boolean[]) o);
        if (o instanceof char[]) return Arrays.toString((char[]) o);
        if (o instanceof Object[]) {
            List<String> es = new ArrayList<>();
            for (Object v : (Object[]) o) es.add(norm(v));
            return "[" + String.join(", ", es) + "]";
        }
        return String.valueOf(o);
    }

    // 忽略元素顺序的规范形式（Map/Set 本身无序；数组/List 先排序再输出）
    public static String normUnordered(Object o) {
        if (o instanceof int[]) { int[] x = ((int[]) o).clone(); Arrays.sort(x); return Arrays.toString(x); }
        if (o instanceof long[]) { long[] x = ((long[]) o).clone(); Arrays.sort(x); return Arrays.toString(x); }
        if (o instanceof double[]) { double[] x = ((double[]) o).clone(); Arrays.sort(x); return Arrays.toString(x); }
        if (o instanceof char[]) { char[] x = ((char[]) o).clone(); Arrays.sort(x); return Arrays.toString(x); }
        if (o instanceof boolean[]) {
            boolean[] b = (boolean[]) o;
            int t = 0;
            for (boolean v : b) if (v) t++;
            List<String> es = new ArrayList<>();
            for (int i = 0; i < t; i++) es.add("true");
            for (int i = t; i < b.length; i++) es.add("false");
            return "[" + String.join(", ", es) + "]";
        }
        if (o instanceof Object[]) {
            List<String> es = new ArrayList<>();
            for (Object v : (Object[]) o) es.add(norm(v));
            Collections.sort(es);
            return "[" + String.join(", ", es) + "]";
        }
        if (o instanceof List) {
            List<String> es = new ArrayList<>();
            for (Object v : (List<?>) o) es.add(norm(v));
            Collections.sort(es);
            return "[" + String.join(", ", es) + "]";
        }
        return norm(o);
    }

    private TestUtil() {}
}

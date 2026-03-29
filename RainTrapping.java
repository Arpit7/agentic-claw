/**
 * Rain Trapping Problem
 * =====================
 * Given n non-negative integers representing an elevation map where the width
 * of each bar is 1, compute how much water it can trap after raining.
 *
 * Example:
 *   Input:  [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
 *   Output: 6
 */
public class RainTrapping {

    // ─────────────────────────────────────────────────────────────
    // Approach 1: Two Pointer — O(n) time, O(1) space  ✅ Optimal
    // ─────────────────────────────────────────────────────────────
    public static int trapTwoPointer(int[] height) {
        if (height == null || height.length < 3) return 0;

        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }

        return water;
    }

    // ─────────────────────────────────────────────────────────────
    // Approach 2: Dynamic Programming — O(n) time, O(n) space
    // ─────────────────────────────────────────────────────────────
    public static int trapDP(int[] height) {
        if (height == null || height.length < 3) return 0;

        int n = height.length;
        int[] leftMax  = new int[n];
        int[] rightMax = new int[n];

        // Fill left max — max height from left up to index i
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }

        // Fill right max — max height from right down to index i
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        // Water at each index = min(leftMax, rightMax) - height[i]
        int water = 0;
        for (int i = 0; i < n; i++) {
            water += Math.min(leftMax[i], rightMax[i]) - height[i];
        }

        return water;
    }

    // ─────────────────────────────────────────────────────────────
    // ASCII Visualization
    // ─────────────────────────────────────────────────────────────
    public static String visualize(int[] height) {
        if (height == null || height.length == 0) return "";

        int n = height.length;
        int maxH = 0;
        for (int h : height) maxH = Math.max(maxH, h);

        // Compute water level at each index
        int[] leftMax  = new int[n];
        int[] rightMax = new int[n];
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) rightMax[i] = Math.max(rightMax[i + 1], height[i]);

        int[] waterLevel = new int[n];
        for (int i = 0; i < n; i++) waterLevel[i] = Math.min(leftMax[i], rightMax[i]);

        StringBuilder sb = new StringBuilder();
        for (int row = maxH; row >= 1; row--) {
            sb.append(String.format("%2d |", row));
            for (int i = 0; i < n; i++) {
                if (height[i] >= row)      sb.append("█");
                else if (waterLevel[i] >= row) sb.append("~");
                else                           sb.append(" ");
            }
            sb.append("|\n");
        }
        sb.append("   +").append("-".repeat(n)).append("+\n");
        sb.append("    ");
        for (int i = 0; i < n; i++) sb.append(i % 10);
        sb.append("\n");

        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // Test Runner
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        int[][][] testCases = {
            {{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, {6}},
            {{4, 2, 0, 3, 2, 5},                   {9}},
            {{3, 0, 2, 0, 4},                       {7}},
            {{1, 0, 1},                              {1}},
            {{1, 2, 3, 4, 5},                        {0}},   // no trapping — ascending
            {{5, 4, 3, 2, 1},                        {0}},   // no trapping — descending
        };

        String[] names = {
            "Classic example",
            "Deep valley",
            "Multiple pits",
            "Simple case",
            "Ascending — no water",
            "Descending — no water",
        };

        System.out.println("=".repeat(52));
        System.out.println("        RAIN TRAPPING PROBLEM — JAVA");
        System.out.println("=".repeat(52));

        boolean allPassed = true;

        for (int t = 0; t < testCases.length; t++) {
            int[] height   = testCases[t][0];
            int   expected = testCases[t][1][0];

            int resultTP = trapTwoPointer(height);
            int resultDP = trapDP(height);
            boolean passed = resultTP == expected && resultDP == expected;
            allPassed = allPassed && passed;

            System.out.printf("\n%s  %s%n", passed ? "✅ PASS" : "❌ FAIL", names[t]);
            System.out.printf("  Height      : %s%n", java.util.Arrays.toString(height));
            System.out.printf("  Expected    : %d%n", expected);
            System.out.printf("  Two Pointer : %d%n", resultTP);
            System.out.printf("  DP          : %d%n", resultDP);
            System.out.println();
            System.out.print(visualize(height));
        }

        System.out.println("\n" + "=".repeat(52));
        System.out.println("  " + (allPassed ? "All tests passed ✅" : "Some tests FAILED ❌"));
        System.out.println("=".repeat(52));
    }
}

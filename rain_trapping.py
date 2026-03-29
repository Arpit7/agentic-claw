"""
Rain Trapping Problem
=====================
Given n non-negative integers representing an elevation map where the width
of each bar is 1, compute how much water it can trap after raining.

Approach: Two Pointer — O(n) time, O(1) space
"""

def trap(height: list[int]) -> int:
    """
    Calculate total trapped rainwater using two-pointer approach.

    Args:
        height: List of non-negative integers representing elevation map

    Returns:
        Total units of water trapped
    """
    if not height or len(height) < 3:
        return 0

    left, right = 0, len(height) - 1
    left_max, right_max = 0, 0
    water = 0

    while left < right:
        if height[left] < height[right]:
            if height[left] >= left_max:
                left_max = height[left]
            else:
                water += left_max - height[left]
            left += 1
        else:
            if height[right] >= right_max:
                right_max = height[right]
            else:
                water += right_max - height[right]
            right -= 1

    return water


def trap_dp(height: list[int]) -> int:
    """
    Calculate total trapped rainwater using dynamic programming.

    Approach: Precompute left_max and right_max arrays — O(n) time, O(n) space
    """
    n = len(height)
    if n < 3:
        return 0

    left_max = [0] * n
    right_max = [0] * n

    left_max[0] = height[0]
    for i in range(1, n):
        left_max[i] = max(left_max[i - 1], height[i])

    right_max[n - 1] = height[n - 1]
    for i in range(n - 2, -1, -1):
        right_max[i] = max(right_max[i + 1], height[i])

    water = 0
    for i in range(n):
        water += min(left_max[i], right_max[i]) - height[i]

    return water


def visualize(height: list[int]) -> str:
    """
    ASCII visualization of the elevation map with trapped water.
    """
    if not height:
        return ""

    n = len(height)
    max_h = max(height)

    # Compute water level at each position
    left_max = [0] * n
    right_max = [0] * n
    left_max[0] = height[0]
    for i in range(1, n):
        left_max[i] = max(left_max[i - 1], height[i])
    right_max[n - 1] = height[n - 1]
    for i in range(n - 2, -1, -1):
        right_max[i] = max(right_max[i + 1], height[i])

    water_level = [min(left_max[i], right_max[i]) for i in range(n)]

    lines = []
    for row in range(max_h, 0, -1):
        line = ""
        for i in range(n):
            if height[i] >= row:
                line += "█"   # wall
            elif water_level[i] >= row:
                line += "~"   # water
            else:
                line += " "
        lines.append(f"{row:2d} |{line}|")

    lines.append("   +" + "-" * n + "+")
    lines.append("    " + "".join(str(i % 10) for i in range(n)))
    return "\n".join(lines)


if __name__ == "__main__":
    test_cases = [
        {
            "height": [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1],
            "expected": 6,
            "name": "Classic example"
        },
        {
            "height": [4, 2, 0, 3, 2, 5],
            "expected": 9,
            "name": "Deep valley"
        },
        {
            "height": [3, 0, 2, 0, 4],
            "expected": 7,
            "name": "Multiple pits"
        },
        {
            "height": [1, 0, 1],
            "expected": 1,
            "name": "Simple case"
        },
    ]

    print("=" * 50)
    print("       RAIN TRAPPING PROBLEM")
    print("=" * 50)

    all_passed = True
    for tc in test_cases:
        result_two_ptr = trap(tc["height"])
        result_dp = trap_dp(tc["height"])
        passed = result_two_ptr == tc["expected"] and result_dp == tc["expected"]
        all_passed = all_passed and passed
        status = "✅ PASS" if passed else "❌ FAIL"

        print(f"\n{status} | {tc['name']}")
        print(f"  Height : {tc['height']}")
        print(f"  Expected: {tc['expected']} | Two-Pointer: {result_two_ptr} | DP: {result_dp}")
        print()
        print(visualize(tc["height"]))

    print("\n" + "=" * 50)
    print(f"  Result: {'All tests passed ✅' if all_passed else 'Some tests failed ❌'}")
    print("=" * 50)

public class BruteForceSolver {

    public String solve(int a[]) {
        final int stacks[] = new int[a.length];
        int stacksLength = 0;
        for (final int number : a) {
            int index = 0;
            while (index < stacksLength && stacks[index] <= number) {
                index++;
            }
            if (index == stacksLength) {
                stacksLength++;
            }
            stacks[index] = number;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stacksLength).append(' ');
        for (int i = 0; i < stacksLength; i++) {
            stringBuilder.append(stacks[i]).append(' ');
        }
        return stringBuilder.toString();
    }
}
import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Runnable logic = () -> {
                String commands = generateRoute("RLRFR", 100);
                int rightTurnsCount = calculateRightTurns(commands);
                fillSizeToFreq(rightTurnsCount);
            };
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        printResult();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int calculateRightTurns(String commands) {
        int rightTurnsCount = 0;
        for (char c : commands.toCharArray()) {
            if (c == 'R') {
                rightTurnsCount++;
            }
        }

        return rightTurnsCount;
    }

    public static void fillSizeToFreq(int commandRepetitionsCount) {
        synchronized (sizeToFreq) {
            if (!sizeToFreq.containsKey(commandRepetitionsCount)) {
                sizeToFreq.put(commandRepetitionsCount, 1);
            } else {
                int value = sizeToFreq.get(commandRepetitionsCount) + 1;
                sizeToFreq.put(commandRepetitionsCount, value);
            }
        }
    }

    public static void printResult() {
        int keyMaxValueInMap = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
        int maxValueInMap = sizeToFreq.get(keyMaxValueInMap);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Самое частое количество повторений %d (встретилось %d раз)\n", keyMaxValueInMap, maxValueInMap));
        sb.append("Другие размеры:\n");

        for (Map.Entry<Integer, Integer> pair : sizeToFreq.entrySet()) {
            if (pair.getValue() != maxValueInMap) {
                sb.append(String.format("- %d (%d раз)\n", pair.getKey(), pair.getValue()));
            }
        }
        System.out.println(sb);
    }
}
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static AtomicInteger counter3 = new AtomicInteger(0);
    public static AtomicInteger counter4 = new AtomicInteger(0);
    public static AtomicInteger counter5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        Thread palindromeThread = new Thread(() -> {
            Set<String> palindromes = findPalindrome(texts);
            updateCounter(palindromes);
        });
        palindromeThread.start();
        threads.add(palindromeThread);

        Thread repeatedThread = new Thread(() -> {
            Set<String> repeatedChars = findRepeatChars(texts);
            updateCounter(repeatedChars);
        });
        repeatedThread.start();
        threads.add(repeatedThread);

        Thread alphabetThread = new Thread(() -> {
            Set<String> alphabetChars = findAlphabetChars(texts);
            updateCounter(alphabetChars);
        });
        alphabetThread.start();
        threads.add(alphabetThread);

        for (Thread thr : threads) {
            thr.join();
        }

        System.out.printf("Красивых слов с длиной 3: %d шт\n", counter3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", counter4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт\n", counter5.get());

    }

    private static void updateCounter(Set<String> texts) {
        Set<String> text3 = texts.stream()
                .filter(size -> size.length() == 3)
                .collect(Collectors.toSet());
        counter3.getAndAdd(text3.size());

        Set<String> text4 = texts.stream()
                .filter(size -> size.length() == 4)
                .collect(Collectors.toSet());
        counter4.getAndAdd(text4.size());

        Set<String> text5 = texts.stream()
                .filter(size -> size.length() == 5)
                .collect(Collectors.toSet());
        counter5.getAndAdd(text5.size());
    }

    public static Set<String> findRepeatChars(String[] texts) {
        Set<String> repeatChars = new HashSet<>();
        for (String rep : texts) {
            if (isRepeat(rep)) {
                repeatChars.add(rep);
            }
        }
        return repeatChars;
    }

    public static boolean isRepeat(String text) {
        char c = text.charAt(0);
        for (int j = 1; j < text.length(); j++) {
            if (text.charAt(j) != c) {
                return false;
            }
        }
        return true;
    }

    public static Set<String> findAlphabetChars(String[] texts) {
        Set<String> alphabetChars = new HashSet<>();
        for (String alphabet : texts) {
            if (isAlphabet(alphabet)) {
                alphabetChars.add(alphabet);
            }
        }
        return alphabetChars;
    }

    public static boolean isAlphabet(String text) {
        int textLength = text.length();
        for (int i = 1; i < textLength; i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Set<String> findPalindrome(String[] texts) {
        Set<String> palindromes = new HashSet<>();
        for (String pal : texts) {
            if (isPalindrome(pal)) {
                palindromes.add(pal);
            }
        }
        return palindromes;
    }

    private static boolean isPalindrome(String text) {
        StringBuilder original = new StringBuilder(text);
        StringBuilder reverse = original.reverse();
        return (reverse.toString()).equals(text);
    }
}

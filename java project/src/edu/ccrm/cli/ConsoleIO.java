package edu.ccrm.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleIO {
    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                int v = Integer.parseInt(line.trim());
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Enter a number between " + min + " and " + max + ".");
            }
        }
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Input cannot be empty.");
        }
    }

    public String readEmail(String prompt) {
        while (true) {
            String email = readNonEmpty(prompt);
            if (EMAIL_PATTERN.matcher(email).matches()) return email;
            System.out.println("Invalid email format.");
        }
    }

    public LocalDate readDate(String prompt, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        while (true) {
            String s = readNonEmpty(prompt);
            try { return LocalDate.parse(s, fmt); } catch (DateTimeParseException e) { System.out.println("Invalid date. Use pattern: " + pattern); }
        }
    }
}



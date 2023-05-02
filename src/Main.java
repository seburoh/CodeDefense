import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private final static StringBuilder errorLog = new StringBuilder("Log File #");
    private final static InputData id = new InputData();

    public static void main(String[] args) {
        errorLog.append(System.currentTimeMillis()).append('\n');
        userInputPhase();


    }

    private static void userInputPhase() {
        Scanner scn = new Scanner(System.in);

        errorLog.append("Reached First Name Input phase\n");
        System.out.println("Enter First Name.");
        System.out.println("Max 50 characters, alphabetic characters, and dashes only.");
        System.out.println("Apologies that if you are Elon Musk's kid, you cannot use this service.");
        String input = scn.nextLine();
        while (checkNameFailed(input)) {
            printFailure("Regex did not match for first name: " + input);
            input = scn.nextLine();
        }
        id.setFirstName(input);
        errorLog.append("First Name Input Accepted\n");
        printSuccess();

        errorLog.append("Reached Last Name Input phase\n");
        System.out.println("Enter Last Name.");
        System.out.println("Max 50 characters, alphabetic characters, and dashes only.");
        input = scn.nextLine();
        while (checkNameFailed(input)) {
            printFailure("Regex did not match for last name: " + input);
            input = scn.nextLine();
        }
        id.setLastName(input);
        errorLog.append("Last Name Input Accepted\n");
        printSuccess();

        errorLog.append("Reached First int Input phase\n");
        System.out.println("Enter an integer");
        System.out.println("Minimum value " + Integer.MIN_VALUE + " and maximum value " + Integer.MAX_VALUE);
        input = scn.nextLine();
        while (testIntFailed(input)) {
            printFailure("Value not accepted by Integer.parseInt: " + input);
            input = scn.nextLine();
        }
        id.setFirstInt(Integer.parseInt(input));
        errorLog.append("First int Accepted\n");
        printSuccess();


        errorLog.append("Reached Second int Input phase\n");
        System.out.println("Enter another integer");
        System.out.println("Minimum value " + Integer.MIN_VALUE + " and maximum value " + Integer.MAX_VALUE);
        input = scn.nextLine();
        while (testIntFailed(input)) {
            printFailure("Value not accepted by Integer.parseInt: " + input);
            input = scn.nextLine();
        }
        id.setSecondInt(Integer.parseInt(input));
        errorLog.append("Second int Accepted\n");
        printSuccess();


        errorLog.append("Reached Input File Input phase\n");
        System.out.println("Enter input file path");
        System.out.println("File must be a .txt");
        input = scn.nextLine();
        while (testIntFailed(input)) {
            printFailure("Value not accepted by Integer.parseInt: " + input);
            input = scn.nextLine();
        }
        id.setSecondInt(Integer.parseInt(input));
        errorLog.append("Second int Accepted\n");
        printSuccess();
    }

    private static BufferedReader openFile(final String path) {
//        BufferedReader br = new BufferedReader();
        //fails
        return null;
        //else

//        return br;
    }

    private static boolean testIntFailed(final String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return true;
        }
        return false;
    }

    private static void printSuccess() {
        System.out.println("Input accepted, thank you.");
    }

    private static void printFailure(final String message) {
        System.out.println("Input not valid, please try again.");
        errorLog.append(message).append('\n');
    }

    private static boolean checkNameFailed(final String name) {
        String regex = "(?i)^(?!-)(?!.*[-]{2})[-a-z]{1,50}(?<!-)$";
        return !name.matches(regex);
    }

    private static class InputData {
        private String firstName;
        private String lastName;
        private int firstInt;
        private int secondInt;
        private String hashedPassword;
        private String inputFileName;

        public String addNumbers() {
            return String.valueOf((long)firstInt + (long)secondInt);
        }

        public String multiplyNumbers() {
            return String.valueOf((long)firstInt * (long)secondInt);
        }

        @Override
        public String toString() {
            return "First Name: " + firstName + '\n' +
                    "Last Name: " + lastName + '\n' +
                    "Added Numbers: " + addNumbers() + '\n' +
                    "Multiplied Numbers: " + multiplyNumbers() + '\n';
        }

        /* Getters and Setters Live Here. */

        public String getInputFileName() {
            return inputFileName;
        }

        public void setInputFileName(final String inputFileName) {
            this.inputFileName = inputFileName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(final String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(final String lastName) {
            this.lastName = lastName;
        }

        public int getFirstInt() {
            return firstInt;
        }

        public void setFirstInt(final int firstInt) {
            this.firstInt = firstInt;
        }

        public int getSecondInt() {
            return secondInt;
        }

        public void setSecondInt(final int secondInt) {
            this.secondInt = secondInt;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }

        public void setHashedPassword(final String hashedPassword) {
            this.hashedPassword = hashedPassword;
        }
    }
}
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * TODO questions.
 * Do we have to check for output file before pw?
 * Can we assume user will not mess with outfile during timeframe?
 *
 * TODO implementation.
 * Password.
 */
public class Main {

    private final static StringBuilder errorLog = new StringBuilder();
    private final static InputData id = new InputData();

    public static void main(String[] args) {
        boolean progress = true;
        id.setCurrentTime(System.currentTimeMillis());
        errorLog.append(id.getCurrentTime()).append('\n');

        while (progress) {
            userInputPhase();
            progress = !writeOutFile();
            progress &= !writeLogFile();
        }

        System.out.println("The borderland was wrapped in scarlet magic. Weebs believe that you solve this mystery");
    }

    private static void userInputPhase() {
        Scanner scn = new Scanner(System.in);

        errorLog.append("Reached First Name Input Phase");
        System.out.println("""
                Enter First Name.
                Max 50 characters, alphabetic characters, and dashes only.
                Apologies that if you are Elon Musk's kid, you cannot use this service.""");
        String input = scn.nextLine();
        while (checkNameFailed(input)) {
            printFailure("Regex did not match for first name: " + input);
            input = scn.nextLine();
        }
        id.setFirstName(input);
        printSuccess("First Name input accepted");


        errorLog.append("Reached Last Name Input Phase");
        System.out.println("Enter Last Name.\nMax 50 characters, alphabetic characters, and dashes only.");
        input = scn.nextLine();
        while (checkNameFailed(input)) {
            printFailure("Regex did not match for last name: " + input);
            input = scn.nextLine();
        }
        id.setLastName(input);
        printSuccess("Last Name input accepted");


        errorLog.append("Reached First int Input Phase");
        System.out.println("Enter an integer.\nMinimum value "
                + Integer.MIN_VALUE + " and maximum value " + Integer.MAX_VALUE);
        input = scn.nextLine();
        while (testIntFailed(input)) {
            printFailure("Value not accepted as integer: " + input);
            input = scn.nextLine();
        }
        id.setFirstInt(Integer.parseInt(input));
        printSuccess("First int input accepted");


        errorLog.append("Reached Second int Input Phase");
        System.out.println("Enter an another integer.\nMinimum value "
                + Integer.MIN_VALUE + " and maximum value " + Integer.MAX_VALUE);
        input = scn.nextLine();
        while (testIntFailed(input)) {
            printFailure("Value not accepted as integer: " + input);
            input = scn.nextLine();
        }
        id.setSecondInt(Integer.parseInt(input));
        printSuccess("Second int input accepted");


        errorLog.append("Reached Input File Input Phase");
        System.out.println("Enter input file path including file name.\nFile must be a .txt");
        input = scn.nextLine();
        while (!readFile(input)) {
            printFailure("File unable to be opened with provided path: " + input);
            input = scn.nextLine();
        }
        printSuccess("Input file contents have been read.");


        errorLog.append("Reached Output File Input Phase");
        System.out.println("Enter path to create new output file at including file name and .txt extension.");
        input = scn.nextLine();
        while (outFileExists(input)) {
            printFailure("File unable to be created with provided path: " + input);
            input = scn.nextLine();
        }
        id.outputFilePath = input;
        printSuccess("Output file path has been verified.");

        //TODO: do password stuff
    }

    private static boolean writeLogFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("logfile_"+id.getCurrentTime()+".txt"));
            writer.write("Log File #"+id.getCurrentTime());
            writer.write(errorLog.toString());
            writer.close();
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return false;
        }
        return true;
    }

    private static boolean writeOutFile() {
        errorLog.append("Reached OutFile write.");
        System.out.println("Weebs are doing their best now with your file content, please watch warmly until it is ready.");
        StringBuilder outContent = new StringBuilder();
        outContent.append("Thank you for using Cereal Experiments Weebs Input Validator.\n");
        outContent.append(id.toString());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(id.outputFilePath));
            writer.write(outContent.toString());
            writer.close();
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return false;
        }
        errorLog.append("OutFile write successful.");
        return true;
    }

    private static boolean outFileExists(final String path) {
        File outFile;

        try {
            outFile = new File(path);
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return true;
        }

        return outFile.getAbsolutePath().equals(id.inputFilePath) || outFile.isFile() || !path.endsWith(".txt");
    }

    private static boolean readFile(final String path) {

        if (path == null || path.length() < 5 || !path.endsWith(".txt")) {
            return false;
        }

        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        String absPath = "";

        try {
//            br = new BufferedReader(new FileReader(path));
            File inFile = new File(path);
            br = new BufferedReader(new FileReader(inFile));
            String line = br.readLine();

            while (line != null) {
                sb.append(line).append('\n');
                line = br.readLine();
            }

            br.close();
            absPath = inFile.getAbsolutePath();

        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return false;
        }

        id.setInputFilePath(absPath);
        id.setInputFileData(sb.toString());
        return true;
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

    private static void printSuccess(final String message) {
        System.out.println("SUCCESS: " + message);
        errorLog.append(message).append('\n');
    }

    private static void printFailure(final String message) {
        System.out.println("ERROR: " + message);
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
        private String inputFilePath;
        private String inputFileData;
        private String outputFilePath;
        private long currentTime;

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
                    "Multiplied Numbers: " + multiplyNumbers() + '\n' +
                    "Input File Contents:\n" + inputFileData + '\n';
        }

        /* Getters and Setters Live Here. */

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public String getOutputFilePath() {
            return outputFilePath;
        }

        public void setOutputFilePath(String outputFilePath) {
            this.outputFilePath = outputFilePath;
        }

        public String getInputFileData() {
            return inputFileData;
        }

        public void setInputFileData(String inputFileData) {
            this.inputFileData = inputFileData;
        }

        public String getInputFilePath() {
            return inputFilePath;
        }

        public void setInputFilePath(final String inputFileName) {
            this.inputFilePath = inputFileName;
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
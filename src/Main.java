import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private final static StringBuilder errorLog = new StringBuilder();
    private final static InputData id = new InputData();

    /**
     * Main runtime for program. Loops until success is reached.
     * @param args unused.
     */
    public static void main(String[] args) {
        boolean progress = true;
        id.setCurrentTime(System.currentTimeMillis());
//        errorLog.append("Error Log#").append(id.getCurrentTime()).append('\n');

        while (progress) {
            userInputPhase();
            progress = !writeOutFile();
            progress &= !writeLogFile();
        }

        System.out.println("The borderland was wrapped in scarlet magic. Weebs believe that you solve this mystery");
    }

    /**
     * Collects input from user and ensures sanitized.
     */
    private static void userInputPhase() {
        Scanner scn = new Scanner(System.in);

        errorLog.append("Reached First Name Input Phase");
        System.out.println("""
                Enter First Name.
                Max 50 characters, alphabetic characters, and dashes only. Dashes cannot be chained.
                Name cannot begin or end with a dash.
                Apologies that if you are Elon Musk's kid, you cannot use this service.""");
        String input = scn.nextLine();
        while (checkNameFailed(input)) {
            printFailure("Regex did not match for first name: " + input);
            input = scn.nextLine();
        }
        id.setFirstName(input);
        printSuccess("First Name input accepted");


        errorLog.append("Reached Last Name Input Phase");
        System.out.println("""
                Enter Last Name.
                Max 50 characters, alphabetic characters, and dashes only. Dashes cannot be chained.
                Name cannot begin or end with a dash.
                Apologies that if you are Elon Musk's kid, you cannot use this service.""");
        input = scn.nextLine();
        while (checkNameFailed(input)) {
            printFailure("Regex did not match for last name: " + input);
            input = scn.nextLine();
        }
        id.setLastName(input);
        printSuccess("Last Name input accepted");


        errorLog.append("Reached First int Input Phase");
        System.out.println("Enter an integer.\nMinimum value "
                + Integer.MIN_VALUE + " and maximum value " + Integer.MAX_VALUE +
                "Decimal valued integers such as 2.0 are not allowed.");
        input = scn.nextLine();
        while (testIntFailed(input)) {
            printFailure("Value not accepted as integer: " + input);
            input = scn.nextLine();
        }
        id.setFirstInt(Integer.parseInt(input));
        printSuccess("First int input accepted");


        errorLog.append("Reached Second int Input Phase");
        System.out.println("Enter an another integer.\nMinimum value "
                + Integer.MIN_VALUE + " and maximum value " + Integer.MAX_VALUE +
                "Decimal valued integers such as 2.0 are not allowed.");
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
        System.out.println("""
                Enter path to create new output file at including file name and .txt extension.
                Any legal filename in your given system with at least one character is allowed.
                If you make a dumb filename that works, that's just on you.""");
        input = scn.nextLine();
        while (outFileExists(input)) {
            printFailure("File unable to be created with provided path: " + input);
            input = scn.nextLine();
        }
        id.setOutputFilePath(input);
        printSuccess("Output file path has been verified.");

        errorLog.append("Reached Password File Phase");
        id.setHashedPasswordPath("hash_" + id.getCurrentTime() + ".txt");
        System.out.println("""
                Enter password to hash.
                Password must contain an uppercase and lowercase letter, and a number.
                Password must be at least 13 characters long, and may optionally contain these symbols: !@#
                Password may not have characters repeated 3+ times in a row.
                This will be stored in a file beginning with the name hash_.""");
        input = scn.nextLine();
        while (!writePassword(input, id.getHashedPasswordPath())) {
            printFailure("Password creation failed: " + input);
            input = scn.nextLine();
        }
        printSuccess("Hashed password successfully saved.");

        errorLog.append("Reached Password Compare Phase");
        System.out.println("Enter password again to compare against previously input password.");
        input = scn.nextLine();
        while (!comparePassword(input)) {
            printFailure("Password compare failed: " + input);
            input = scn.nextLine();
        }
        printSuccess("Hashed password successfully compared.");

    }

    /**
     * Compares hashed passwords to ensure equality.
     * Retrieves original password from file on each attempt.
     * @param password new password typed to hash and compare with.
     * @return true if passwords are equal when hashed.
     */
    private static boolean comparePassword(final String password) {
        byte[] firstPassword = readPasswordFromFile(id.getHashedPasswordPath());
        byte[] secondPassword = hashPassword(password);

        return firstPassword != null && secondPassword != null && Arrays.equals(firstPassword,secondPassword);
    }

    /**
     * Writes password to a file after hashing it.
     * Password must be alphabetic/numeric with upper and lower case, and at least 13char. !@# optional.
     * @param password password to hash.
     * @param path where to put the password.
     * @return true if successful.
     */
    private static boolean writePassword(final String password, final String path){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*(.)\\1{2,})[A-Za-z\\d!@#]{13,}$";
        if (password == null || !password.matches(regex)) {
            printFailure("Password not strong enough to be saved.");
            return false;
        }

        byte[] hashedPassword = hashPassword(password);

        if (hashedPassword == null) {
            return false;
        }

        try {
            Files.write(Path.of(path), hashedPassword);
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return false;
        }

        return true;
    }

    /**
     * Attempts to read a hashed password from a given filepath.
     * @param path filepath to check for password.
     * @return hashed password if possible, null otherwise.
     */
    private static byte[] readPasswordFromFile(final String path) {
        byte[] retrievedPassword;

        try {
            retrievedPassword = Files.readAllBytes(Path.of(path));
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return null;
        }

        return retrievedPassword;
    }

    /**
     * Hashes a password using SHA256 and a securely generated random salt.
     * Stores the salt only during runtime, salt is lost on program exit.
     * @param password password to hash.
     * @return hashed password, null if failed.
     */
    private static byte[] hashPassword(final String password) {
        byte[] salt;
        if (id.getStoredSalt() == null) {
            SecureRandom random = new SecureRandom();
            salt = new byte[16];
            random.nextBytes(salt);
            id.setStoredSalt(salt);
        } else {
            salt = id.getStoredSalt();
        }

        byte[] hash;

        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return null;
        }

        return hash;
    }

    /**
     * Writes contents of log to a file.
     * @return true if successful.
     */
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

    /**
     * Writes contents of user out file.
     * @return true if successful.
     */
    private static boolean writeOutFile() {
        errorLog.append("Reached OutFile write.");
        System.out.println("Weebs are doing their best now with your file content, please watch warmly until it is ready.");
        StringBuilder outContent = new StringBuilder();
        outContent.append("Thank you for using Cereal Experiments Weebs Input Validator.\n");
        outContent.append(id.toString());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(id.getOutputFilePath()));
            writer.write(outContent.toString());
            writer.close();
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return false;
        }
        errorLog.append("OutFile write successful.");
        return true;
    }

    /**
     * Checks if user desired location for a new outfile is valid.
     * @param path location to check.
     * @return true if INVALID.
     */
    private static boolean outFileExists(final String path) {
        File outFile;

        if (path == null || path.length() < 5 || !path.endsWith(".txt")) {
            return true;
        }

        try {
            outFile = new File(path);
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return true;
        }

        return outFile.getAbsolutePath().equals(id.getInputFilePath())
                || outFile.isFile() || outFile.exists();
    }

    /**
     * Reads a .txt file into a String.
     * @param path filepath to read in.
     * @return true if successful.
     */
    private static boolean readFile(final String path) {

        if (path == null || path.length() < 5 || !path.endsWith(".txt")) {
            return false;
        }

        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        String absPath;

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

    /**
     * Attempts to take user input in as an int.
     * @param input string to turn into int.
     * @return true if INVALID.
     */
    private static boolean testIntFailed(final String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            errorLog.append(Arrays.toString(e.getStackTrace())).append('\n');
            return true;
        }
        return false;
    }

    /**
     * Prints a success message to a user and logs that message.
     * @param message message to display and log.
     */
    private static void printSuccess(final String message) {
        System.out.println("SUCCESS: " + message);
        errorLog.append(message).append('\n');
    }

    /**
     * Prints a failure message to a user and logs that message.
     * @param message message to display and log.
     */
    private static void printFailure(final String message) {
        System.out.println("ERROR: " + message);
        errorLog.append(message).append('\n');
    }

    /**
     * Checks that a given name string is valid under the given constraints.
     * 50char max, a-z and hyphens only, no chained hyphens, no beginning/ending on a hyphen.
     * @param name string to check.
     * @return true if INVALID.
     */
    private static boolean checkNameFailed(final String name) {
        String regex = "(?i)^(?!-)(?!.*-{2})[-a-z]{1,50}(?<!-)$";
        return !name.matches(regex);
    }

    /**
     * Private inner class which holds user data.
     * Mostly getter/setter methods.
     */
    private static class InputData {
        private String firstName;
        private String lastName;
        private int firstInt;
        private int secondInt;
        private String hashedPasswordPath;
        private String inputFilePath;
        private String inputFileData;
        private String outputFilePath;
        private long currentTime;
        private byte[] storedSalt;

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

        public byte[] getStoredSalt() {
            return storedSalt;
        }

        public void setStoredSalt(byte[] storedSalt) {
            this.storedSalt = storedSalt;
        }

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

        public String getHashedPasswordPath() {
            return hashedPasswordPath;
        }

        public void setHashedPasswordPath(final String hashedPasswordPath) {
            this.hashedPasswordPath = hashedPasswordPath;
        }
    }
}
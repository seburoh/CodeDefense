import java.util.Arrays;
import java.util.Scanner;

public class Scratch {
    public static void main(String[] args) {
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println(testInt(scn.nextLine()));
            String inp = String.valueOf(scn.nextLine());
            long l = Long.parseLong(inp);
            int i = Integer.parseInt(inp);
            System.out.println(l);
            System.out.println(i);
            System.out.println((int)l == i);
        }
    }

    private static boolean testInt(final String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }
}

import java.util.Arrays;
import java.util.Scanner;

public class Scratch {
    public static void main(String[] args) {
        String regex = "^[+-]?[0-9a-f]{1,8}$";
        System.out.println("0a".matches(regex));
    }

}

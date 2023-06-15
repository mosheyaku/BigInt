import java.util.Scanner;

/*A main program that uses the BigInt class.
The program receives from the user two strings representing two unlimited numbers,
and performs on them the various operations defined in the BigInt class.*/
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /*Prompt user for first number*/
        System.out.print("Please enter the first number: ");
        String input1 = scanner.nextLine();

        /*Validate input and create BigInt object*/
        BigInt num1 = null;
        while (num1 == null) {
            try {
                num1 = new BigInt(input1);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter a valid number: ");
                input1 = scanner.nextLine();
            }
        }

        /*Prompt user for second number*/
        System.out.print("Please enter the second number: ");
        String input2 = scanner.nextLine();

        /*Validate input and create BigInt object*/
        BigInt num2 = null;
        while (num2 == null) {
            try {
                num2 = new BigInt(input2);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter a valid number: ");
                input2 = scanner.nextLine();
            }
        }

        /*Perform operations and print results*/
        System.out.println("First number: " + num1.toString());
        System.out.println("Second number: " + num2.toString());
        System.out.println("Addition between the two parameters: " + num1.plus(num2));
        System.out.println("Subtraction between the two parameters: " + num1.minus(num2));
        System.out.println("Multiplication between the two parameters: " + num1.multiply(num2));

        try {
            System.out.println("Division between the two parameters: " + num1.divide(num2));
        } catch (ArithmeticException e) {
            System.out.println("Division by zero is not allowed.");
        }

        switch (num1.compareTo(num2)) {
            case 1:
                System.out.println("Comparison: the first parameter is greater than the second");
                break;
            case -1:
                System.out.println("Comparison: the first parameter is less than the second");
                break;
            case 0:
                System.out.println("Comparison: both parameters are equal");
                break;
        }

        System.out.println("Are the two parameters equal: " + num1.equals(num2));
    }
}

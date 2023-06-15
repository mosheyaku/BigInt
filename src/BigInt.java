import java.util.ArrayList;

/*
The BigInt class handles "unlimited" integers and includes implementation of the
arithmetic operations - addition, subtraction, multiplication and division on them.
*/
public class BigInt implements Comparable<BigInt> {

    private ArrayList<Integer> digits;
    private boolean isPositive;

    /*
     A constructor that accepts a string and produces an unlimited number from it.
     In case the string does not represent a valid number, an exception is triggered.*/
    public BigInt(String num) {
        if (num.length() == 0) {
            throw new IllegalArgumentException("Invalid input: empty string");
        }

        if (num.charAt(0) == '-') {
            isPositive = false;
            num = num.substring(1);
            if (num.length() == 0)
                throw new IllegalArgumentException("Invalid input: empty string");
        } else {
            isPositive = true;
        }

        digits = new ArrayList<>();
        for (int i = num.length() - 1; i >= 0; i--) {
            char c = num.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Invalid input: " + num);
            }
            digits.add(c - '0');
        }
    }

    /*A constructor that accepts an array of type int and a
    boolean variable and produces an unlimited number of it.*/
    private BigInt(ArrayList<Integer> digits, boolean isPositive) {
        this.digits = digits;
        this.isPositive = isPositive;
    }

    /*The method accepts a BigInt as a parameter and returns a
    new BigInt which is the sum of the number and the parameter.*/
    public BigInt plus(BigInt other) {

        if (this.isPositive != other.isPositive) {
            BigInt unsignedCurrentBigInt = new BigInt(this.digits, true);
            BigInt unsignedOtherBigInt = new BigInt(other.digits, true);

            if (unsignedCurrentBigInt.compareTo(unsignedOtherBigInt) == 0)
                return new BigInt("0");

            return unsignedCurrentBigInt.compareTo(unsignedOtherBigInt) == 1 ?
                    new BigInt(this.unsignedMinus(other.digits), this.isPositive) :
                    new BigInt(other.unsignedMinus(this.digits), other.isPositive);

        }
        return new BigInt(this.unsignedPlus(other.digits), this.isPositive);
    }

    /*The method accepts a BigInt as a parameter and returns a new
    BigInt which is the difference between the number and the parameter.*/
    public BigInt minus(BigInt other) {

        return plus(new BigInt(other.digits, !other.isPositive));
    }

    /*The method accepts a BigInt as a parameter and returns a
    new BigInt which is the multiplication of the number and the parameter.*/
    public BigInt multiply(BigInt other) {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < digits.size() + other.digits.size(); i++) {
            result.add(0);
        }

        int carry = 0;
        int index = 0;

        for (int i = 0; i < digits.size(); i++) {
            for (int j = 0; j < other.digits.size(); j++) {
                int multiplication = digits.get(i) * other.digits.get(j) + carry + result.get(index);
                carry = multiplication / 10;
                result.set(index, multiplication % 10);
                index++;
            }

            if (carry > 0) {
                result.set(index, carry);
                carry = 0;
            }

            index = i + 1;
        }

        while (result.size() > 1 && result.get(result.size() - 1) == 0) {
            result.remove(result.size() - 1);
        }

        return new BigInt(result, isPositive == other.isPositive);
    }

    /*The method receives a BigInt as a parameter and returns a new BigInt which
    is the result of dividing the number in the parameter (division by integers).*/
    public BigInt divide(BigInt divisor) {
        if (divisor.equals(new BigInt("0"))) {
            throw new ArithmeticException("Invalid input: division by zero");
        }

        boolean positiveResult = (this.isPositive == divisor.isPositive);
        BigInt temp = new BigInt(this.digits, true);
        BigInt temp2 = new BigInt(divisor.digits, true);
        BigInt result = new BigInt("0");

        while (temp.compareTo(temp2) >= 0) {
            result = result.plus(new BigInt("1"));
            temp = temp.minus(temp2);
        }

        result.isPositive = positiveResult;
        while (result.digits.size() > 1 && result.digits.get(result.digits.size() - 1) == 0) {
            result.digits.remove(result.digits.size() - 1);
        }

        return result;
    }

    /*The method returns a string representing the unlimited number.*/
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!isPositive) {
            sb.append("-");
        }
        for (int i = digits.size() - 1; i >= 0; i--) {
            sb.append(digits.get(i));
        }
        return sb.toString();
    }

    /*The method accepts an object (parameter) and returns a boolean value indicating
    whether the parameter and the number on which the method was invoked are equal.*/
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BigInt)) {
            return false;
        }
        BigInt other = (BigInt) obj;
        if (this.isPositive != other.isPositive || this.digits.size() != other.digits.size()) {
            return false;
        }
        for (int i = 0; i < this.digits.size(); i++) {
            if (this.digits.get(i) != other.digits.get(i)) {
                return false;
            }
        }
        return true;
    }

    /*The method accepts a BigInt as a parameter and returns a negative, zero, or positive number;
    In case the number is less than, equal to, or greater than the parameter - respectively.*/
    public int compareTo(BigInt other) {
        if (isPositive && !other.isPositive) {
            return 1;
        }
        if (!isPositive && other.isPositive) {
            return -1;
        }
        if (digits.size() > other.digits.size()) {
            return isPositive ? 1 : -1;
        }
        if (digits.size() < other.digits.size()) {
            return isPositive ? -1 : 1;
        }
        for (int i = digits.size() - 1; i >= 0; i--) {
            int digit1 = digits.get(i);
            int digit2 = other.digits.get(i);
            if (digit1 > digit2) {
                return isPositive ? 1 : -1;
            }
            if (digit1 < digit2) {
                return isPositive ? -1 : 1;
            }
        }
        return 0;
    }

    /*An auxiliary method that receives a BigInt as a parameter and performs an addition between unsigned parameters (+-)
    (meaning between the parameter on which the method is invoked and the received parameter that are both "unsigned").*/
    private ArrayList<Integer> unsignedPlus(ArrayList<Integer> other) {
        ArrayList<Integer> result = new ArrayList<>();
        int carry = 0;
        int i = 0;

        while (i < digits.size() || i < other.size() || carry > 0) {
            int sum = carry;
            if (i < digits.size()) {
                sum += digits.get(i);
            }
            if (i < other.size()) {
                sum += other.get(i);
            }
            result.add(sum % 10);
            carry = sum / 10;
            i++;
        }
        return result;
    }

    /*An auxiliary method that receives a BigInt as a parameter and performs subtraction between unsigned parameters (+-)
    (meaning between the parameter on which the method is invoked and the received parameter that are both "unsigned").*/
    private ArrayList<Integer> unsignedMinus(ArrayList<Integer> other) {
        ArrayList<Integer> result = new ArrayList<>();
        int borrow = 0;
        int i = 0;

        while (i < digits.size() || i < other.size()) {
            int diff = borrow;
            if (i < digits.size()) {
                diff += digits.get(i);
            }
            if (i < other.size()) {
                diff -= other.get(i);
            }
            if (diff < 0) {
                diff += 10;
                borrow = -1;
            } else {
                borrow = 0;
            }
            result.add(diff);
            i++;
        }

        while (result.size() > 1 && result.get(result.size() - 1) == 0) {
            result.remove(result.size() - 1);
        }
        return result;
    }
}


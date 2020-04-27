import java.util.Scanner;

public class problem2 {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        while (true) {
            double x = scan.nextDouble();
            double y = scan.nextDouble();

            double c1 = Math.sqrt(Math.pow(x - 36.98, 2) + Math.pow(y - 129.65, 2));
            double c2 = Math.sqrt(Math.pow(x - 25.08, 2) + Math.pow(y - 90.08, 2));
            System.out.println(c1);
            System.out.println(c2);
        }
    }
}

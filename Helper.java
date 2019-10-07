import java.util.Arrays;

public class Helper {
	static boolean[] prime;
	/** 
    * Class constructor.
    */
	public Helper (int num) {
		prime = new boolean[num + 1];
		Arrays.fill(prime, true);
		for (int i = 2; i < prime.length; i++) {
			if (!prime[i]) {
				continue;
			}
			for (int j = i - 1; j >= 2; j--) {
				if (i % j == 0) {
					prime[i] = false;
					break;
				}
			}
			for (int j = 2; i * j < prime.length; j++) {
				prime[i * j] = false;
			}
		}
	}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		for (int i = 2; i < x; i++) {
			if (x % i == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	* This method is used to get the largest prime factor 
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {
//		if (isPrime(x)) {
//			return x;
//		}
//		for (int i = x - 1; i >= 2; i--) {
//			if (prime[i] && x % i == 0) {
//				return i;
//			}
//		}
//		return -1;

		if (isPrime(x)) {
			return x;
		}
		for (int i = x - 1; i >= 2; i--) {
			if (x % i == 0 && isPrime(i)) {
				return i;
			}
		}
		System.out.println("Warning!");
		return -1;

    }
}
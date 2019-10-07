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
		if (x <= 1) {
			return false;
		}
		if (x == 2 || x == 3) {
			return false;
		}
		if ((x * x - 1) % 24 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method is used to get the largest prime factor
	 * @param x A positive integer number
	 * @return int The largest prime factor of x
	 */
	public static int getLargestPrimeFactor(int x) {
		int maxPrime = -1;
		while (x % 2 == 0) {
			maxPrime = 2;
			x >>= 1;
		}
		for (int i = 3; i <= Math.sqrt(x); i+= 2) {
			while (x % i == 0) {
				maxPrime = i;
				x /= i;
			}
		}
		if (x >= 2) {
			maxPrime = x;
		}
		return maxPrime;
	}
}
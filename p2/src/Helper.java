public class Helper {
	
	/** 
    * Class constructor.
    */
	private Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param n A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
		public static boolean isPrime(int n) {
		if (n <= 1)  return false;

		for (int i = 2; i < n; i++)
			if (n % i == 0)
				return false;
		return true;
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
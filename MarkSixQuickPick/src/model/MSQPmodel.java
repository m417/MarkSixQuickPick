package model;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MSQPmodel {

	private int myStart;
	private int myEnd;

	public MSQPmodel(int theStart, int theEnd) {
		myStart = theStart;
		myEnd = theEnd + 1;
	}

	private BigInteger factorial(int theNum) {
		BigInteger result = BigInteger.ONE;
		for (int i = 2; i <= theNum; i++) {
			result = result.multiply(BigInteger.valueOf(i));
		}
		return result;
	}

	private BigInteger theKthSumOfNumbers(int theN, int theK) {
		BigInteger numerator = factorial(theN + theK);
		BigInteger denominator = factorial(theN - 1).multiply(factorial(theK + 1));
		return numerator.divide(denominator);
	}

	public List<Integer> getCombination(int theNumOfInts) {
		return new Random().ints(myStart, myEnd).distinct().limit(theNumOfInts).boxed().sorted()
				.collect(Collectors.toList());
	}

	public int getTotalInvestment(int theCount, char theType) {
		int result = 0;
		if (theType == 'U') {
			result = theKthSumOfNumbers(7, theCount - 7).intValueExact() * 10;
		} else if (theType == 'P') {
			result = theKthSumOfNumbers(7, theCount - 7).intValueExact() * 5;
		}
		return result;
	}

}

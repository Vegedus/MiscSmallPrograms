import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

//https://blog.svpino.com/2015/05/07/five-programming-problems-every-software-engineer-should-be-able-to-solve-in-less-than-1-hour

public class FiveProblems {

	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);list.add(1);list.add(2);list.add(3);list.add(4);list.add(5);
		ArrayList<Integer> list2 = (ArrayList<Integer>) list.clone();
		System.out.println(addListFor(list));
		System.out.println(addListWhile(list));
		System.out.println(addListRec(list2));

		char[] chars = {'a','b','c'};
		int[] nums = {1,2,3};

		Object[] interList = interleave(chars, nums);
		for (int i = 0; i < interList.length; i++) {
			System.out.print(interList[i]);
		}

		Fibonacchi(0,1,0);
		int[] form = {50, 2, 1, 9, 98, 7, 76, 78};
		System.out.println(formHighest(form));
		System.out.println(Math.log10(100));
		
		digitsToHundred();

	}

	public static int addListFor(ArrayList<Integer> list){
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		return sum;
	}

	public static int addListWhile(ArrayList<Integer> list){
		int sum = 0;
		while(!list.isEmpty()) {
			sum += list.remove(0);

		}
		return sum;
	}

	public static int addListRec(ArrayList<Integer> list){
		if(list.isEmpty()) return 0;
		int sum = list.get(0);
		//System.out.println(sum);
		list.remove(0);
		return sum + addListRec(list);
	}

	public static Object[] interleave(char[] chars, int[] nums){
		int maxLength = Math.max(chars.length, nums.length);
		Object[] interList = new Object[chars.length+nums.length];
		int j = 0;
		for (int i = 0; i < maxLength; i++) {
			if(i < chars.length){ interList[j] = chars[i]; j++; } 
			if(i < nums.length){ interList[j] = nums[i]; j++; }				
		}
		return interList;
	}

	public static void Fibonacchi(int a, int b, int i){
		if(i == 10) return;
		System.out.println(a+b);
		i++;
		//System.out.println("i "+i);
		Fibonacchi(b, a+b,i);
	}

	public static int[] formHighest(int[] form){
		int[] highestForm = new int[form.length];
		//Set<Integer> decimals = new HashSet<Integer>();
		int[] decimals = new int[form.length];

		int maxDecimals = 0;
		for (int i = 0; i < form.length; i++) {
			decimals[i] = (int)(Math.log10(form[i]));
			if( decimals[i] > maxDecimals) maxDecimals = decimals[i];
		}

		//91 922   92291 > 91922
		//9 91	   991 > 919
		//8 89	   898 > 889
		//8 888888888888888888888889


		for (int d = 0; d < decimals.length; d++) {
			for (int i = 0; i < form.length; i++) {
				double max = -1;
				int maxIndex = -1;
				for (int j = 0; j < form.length; j++) {
					double singleDigit = form[j] / Math.pow(10,decimals[j]);
					if(decimals[j] == 0) singleDigit += singleDigit/9;

					if( max < singleDigit && form[j] != -1){ max = singleDigit; maxIndex = j;}
				}
				if(maxIndex != -1){highestForm[i] = form[maxIndex]; form[maxIndex] = -1; System.out.print(highestForm[i]);}
			}
		}

		return highestForm;

	}

	public enum Sign{plus,minus,blank};

	public static Sign[] digitsToHundred(){
		int[] nums = {1,2,3,4,5,6,7,8,9};
		Sign[] signs = {Sign.blank,Sign.minus,Sign.plus,Sign.plus,Sign.plus,Sign.plus,Sign.plus,Sign.plus}; //8 of them
		int sum = 45;
		while(sum != 100){
			
			//Change signs
			int diff = 100 - sum;
			if( )
			
			
			//Parse
			int tempSum = nums[0];
			boolean positive = true;
			for (int j = 1; j < nums.length; j++) {
				if(signs[j-1] == Sign.blank){
					tempSum *= 10;
					tempSum += nums[j];					
				} 
				else {
					if(positive) sum += tempSum; 
					else sum -= tempSum;
					tempSum = 0;
					tempSum += nums[j];
					if(signs[j-1] == Sign.plus)positive = true;
					else if(signs[j-1] == Sign.minus)positive = false;

				}
			} sum += tempSum;
			
			System.out.println(sum);
		}
		
		
		return signs;
	}
}
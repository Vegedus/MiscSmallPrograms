
public class FizzBuzz {

	public static void main(String[] args) {
		for (int i = 1; i <= 100; i++) {
			boolean notFizz = true;
			if(i % 3 == 0) { System.out.print("Fizz "); notFizz = false; }
			if(i % 5 == 0) { System.out.println("Buzz"); notFizz = false; }
			if(notFizz){
			System.out.print(i + " ");}
		}
	}
}

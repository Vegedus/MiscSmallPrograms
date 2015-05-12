import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* Alle terninger har et navn og et array, hvor array.length er antal sider.
 * Hvert tal i sides repræsentere et bestemt symbol på terningen, som følger
 * 2: Success
 * 0: Ingenting/blank
 * 
 * Positive tal er generelt gode for spilleren, negative dårlige, og størrelsen
 * angiver hvor meget
 */
class Die{
	public String name;
	public int[] sides;
	//Can be used used to estimate when it's worth it to do a reroll
	public int sideSum = 0;

	public Die(String name, int[] sides){
		this.name = name;
		this.sides = sides;
		for (int i = 0; i < sides.length; i++) {
			sideSum += sides[i];
		}
	}
}

public class ModusOperandiDieStatistics {

	//Begynder at blive langsom (>1 sekund) over 10.000.000 
	private static int sampleSize = 1000*1000*10;

	public static void main(String[] args) {

		Die alsidig = new Die("alsidig", new int[]{0,0,0,1,1,1} );
		Die modstand = new Die("modstand", new int[]{0,0,-1,-1,-1,-2} );
		Die d6 = new Die("6", new int[]{1,2,3,4,5,6} );

		//Threshold is non-inclusive, full name should be "reroll UNDER this threshold"
		Die[] plusMinusDice = {alsidig, modstand};
		int[] logicThres = {1,0};
		
		generateData(plusMinusDice, 4);
	}

	public static void generateData(Die[] dieTypes, int maxAmount){
		int[] diceAmount;
		int[] threshold = {1,0};
		int maxRerolls = 3;

		int sumRanges = maxAmount*2;

		HashMap<Integer, Integer>[] experimentResults = new HashMap[maxRerolls+1];
		String fileString = "";

		for (int j = -sumRanges; j <= sumRanges; j++) {
			if(j == -sumRanges) fileString += "Thresholds: " + threshold[0] + ", " + threshold[1];
			fileString += "\t" + j;
		}
		fileString += "\t\tAverage\t\tSuccess Rate\n";

		for (int k = 1; k <= maxAmount; k++) {
			for (int l = 0; l <= maxAmount; l++) {
				diceAmount = new int[]{k,l};
				
				for (int i = 1; i < 4; i++) {
					experimentResults[i] = sampleDice(dieTypes, diceAmount,
							i, threshold);

					double average = 0;
					double successRate = 0;
					fileString += experimentString(dieTypes, diceAmount, i,
							threshold) + "\t";

					for (int j = -sumRanges; j <= sumRanges; j++) {
						if (experimentResults[i].get(j) == null)
							fileString += "\t";
						else {
							double oneSum = experimentResults[i].get(j)
									/ (double) sampleSize;
							if (j >= 1)
								successRate += oneSum;
							average += experimentResults[i].get(j) * j;
							fileString += Math.round(10000.0 * oneSum) / 100.0
									+ "\t";
						}
					}
					average = Math.round(average / (double) sampleSize * 100.0) / 100.0;
					successRate = Math.round(10000.0 * successRate) / 100.0;
					fileString += "\t" + average + "\t\t" + successRate + "%\n";
				}
				fileString += "\n";
			}
		}
		
		System.out.println(fileString);
		PrintWriter writer = null;
		try {writer = new PrintWriter("ModusOperandi-LessReroll-1.xls", "UTF-8");
		} catch (Exception e) {}
		writer.print(fileString);
		writer.close();
	}



	public static HashMap<Integer, Integer> sampleDice(Die[] dieType, int[] diceAmount, int rerolls, int rerollThreshold[]){

		int totalSum = 0;
		int totalDice = 0;
		for (int i = 0; i < diceAmount.length; i++) totalDice += diceAmount[i];
		HashMap<Integer, Integer> totalResults = new HashMap<Integer, Integer>(totalDice*2);

		//Repeats the rolls a certain amount of times to make a nice sample for statistics
		for (int i = 0; i < sampleSize; i++) {

			int[] results = new int[totalDice];
			//Ensures all dice will be "re-rolled" the first time
			for (int j = 0; j < results.length; j++) results[j] = Integer.MIN_VALUE;

			int sum = Integer.MIN_VALUE;
			//You should roll at least once..
			for (int j = 0; j <= rerolls; j++) {
				int m = 0;
				
				for (int k = 0; k < dieType.length; k++) {
					for (int l = 0; l < diceAmount[k]; l++) {
						//... If the last result wasn't above the threshol
						if(results[m] < rerollThreshold[k]) { if(k != 1 || j != rerolls || results[m] < -1)  results[m] = rollDie(dieType[k]); 
						//else System.out.println("dgr");
						}
						
						m++;
					}
				}
				sum = 0;
				for (int k = 0; k < results.length; k++) {
					sum += results[k];
				}
			}

			//This is all about how to save the results, basically.
			sum = 0;
			for (int k = 0; k < results.length; k++) {
				sum += results[k];
			}
			totalSum += sum;

			if (totalResults.get(sum) == null){ totalResults.put(sum, 1);
			} else totalResults.put(sum, totalResults.get(sum)+1);
		}
		
		return totalResults;
	}

	public static String experimentString(Die[] dieType, int[] diceAmount, int rerolls, int rerollThreshold[]){
		//Making the string to print (because who needs raw data, amirite?)
		String outputMessage = "";
		for (int i = 0; i < dieType.length; i++) {
			outputMessage += diceAmount[i] + " " + dieType[i].name;
			if(i != (dieType.length - 1)) outputMessage += ", ";			
		}

		//Removes plural s if there's only one reroll
		if(rerolls == 1) outputMessage += ", " + rerolls + " reroll";
		else if(rerolls > 1) outputMessage += ", " + rerolls + " rerolls"; 
		else outputMessage += ", no rerolls"; 
		return outputMessage;
	}


	public static void sampleDice(Die dieType, int diceAmount, int rerolls, int rerollThreshold){
		int sum = 0;

		//Repeats the rolls a certain amount of times to make a nice sample for statistics
		for (int i = 0; i < sampleSize; i++) {

			int[] results = new int[diceAmount];
			//Ensures all dice will be "re-rolled" the first time
			for (int j = 0; j < results.length; j++) results[j] = Integer.MIN_VALUE;

			//You should roll at least once..
			for (int j = 0; j < (rerolls+1); j++) {
				//Roll each die
				for (int k = 0; k < diceAmount; k++) {
					//... If the last result wasn't above the threshold
					if(results[k] < rerollThreshold) 
						results[k] = rollDie(dieType);
				}
			}
			for (int k = 0; k < results.length; k++) sum += results[k];
		}

		double statistic = (double)sum / (double)sampleSize;
		String outputMessage = diceAmount + " D " + dieType.name;

		//Removes plural s if there's only one reroll
		if(rerolls == 1) outputMessage += ", " + rerolls + " reroll, rerolling on results less than " + rerollThreshold;
		else if(rerolls > 1)	outputMessage += ", " + rerolls + " rerolls, rerolling on results less than " + rerollThreshold; 
		System.out.println(outputMessage + ":\n"+statistic +"\n");	
	}

	public static void sampleDice(Die dieType){
		sampleDice(dieType, 1, 0, 0);
	}

	public static void sampleDice(Die dieType, int diceAmount, int rerolls, boolean smart){
		sampleDice(dieType, diceAmount, rerolls, dieType.sideSum);
	}

	public static void sampleDice(Die dieType, int diceAmount){
		sampleDice(dieType, diceAmount, 0, 0);
	}

	//Returns the number on a random side of the dice
	public static int rollDie(Die die){
		return die.sides[(int)(Math.random()*die.sides.length)];
	}

}
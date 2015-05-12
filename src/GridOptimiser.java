import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class GridOptimiser {


	public static int sizeX = 25;
	public static int sizeY = 20;
	public static int[] origArea  = new int[sizeX*sizeY];
	public static int[] area  = new int[sizeX*sizeY];


	public static void main(String[] args) {

		//Unavailiable
		for (int i = 10; i <= 14; i++) {
			for(int j = 0; j <= 4; j++) {
				origArea[i+j*sizeX] = 9;
			}
		}

		for (int bigs = 19; bigs <= 19; bigs++) {
			System.out.println("Number of big houses: " + bigs);

			int smallHousesAmount = 0;
			//Randomly put bigs
			int neededAmount = (int)Math.ceil((475.0 - 9.0*bigs))-2;

			System.out.println("Needed:"+ neededAmount);

			while(smallHousesAmount < neededAmount) {
				
				smallHousesAmount = 0;
				
				System.arraycopy( origArea, 0, area, 0, origArea.length );
				
				//area = origArea.clone();
				for (int i = 0; i < bigs; i++) {
					int x = 0;
					int y = 0;
					
					//Cheatz, placing by logic instead of programming, booh!
					if(i == 0)
					for (int j = 4; j < 11; j+=3) {
						for (int j2 = 5; j2 < 19; j2+=3) {
							setSpace(j,j2,3);
							i++;
						}
					}
					
					while (checkSpace(x, y, 3) != -1) {
						
						x = (int)((sizeX-3)*Math.random());
						y = (int)((sizeY-3)*Math.random());						
					}
					
					/*while (checkSpace(x, y, 3) != -1) {
						x++;
						if(x == sizeX-4){x = 0; y++;}
					}*/

					setSpace(x,y,3);

	
				}

				for (int i = 0; i < (sizeX-1); i++) {
					for (int j = 0; j < (sizeY-1); j++) {
						int check = checkSpace(i, j, 2);

						if(check == 2) i++;
						else if(check == -1){ setSpace(i,j,2); smallHousesAmount++;}
					}
				}
			
			}
			System.out.println("Houses" + smallHousesAmount);
			printMatrix(sizeX, sizeY);
			
		}
	}



	public static void printMatrix(int sizeX, int sizeY) {
		for(int j = 0; j < sizeY; j++) {				
			for (int i = 0; i < sizeX; i++) {
				System.out.print(area[i+j*sizeX]+" ");
			}
			System.out.println();
		}
	}

	public static int checkSpace(int x, int y, int size){
		for (int i = x; i < size+x; i++) {
			for (int j = y; j < size+y; j++) {
				//System.out.println(i + " checking " + j);

				try {
					if(area[i+j*sizeX] != 0) return i-x;
				} catch (Exception e) {
					return i-x;
				}
			}
		}
		//System.out.println("Check!");
		return -1;
	}

	public static void setSpace(int x, int y, int size){
		for (int i = x; i < size+x; i++) {
			for (int j = y; j < size+y; j++){
				area[i+j*sizeX] = size;
			}
		}
		//System.out.println("Set");
	}
}

package algorithm;

import java.io.*;
import javax.swing.*;

import utilities.Utility;
import utilities.Utility.ThreadCircularShift;

public class ModifiedDES {
    // first key-Permutation Table 
    private static final int[] P128 = { 
				3, 116, 107, 97, 80, 65, 104, 37, 21, 13, 49, 35, 17, 86, 101, 94, 
				8, 76, 69, 16, 128, 73, 120, 9, 126, 91, 56, 34, 59, 115, 110, 70, 
				83, 28, 39, 63, 118, 32, 123, 87, 67, 95, 27, 51, 125, 61, 5, 22, 
				46, 15, 89, 108, 121, 12, 111, 84, 102, 64, 99, 77, 7, 26, 31, 52, 
				79, 82, 20, 43, 36, 92, 117, 109, 105, 48, 45, 54, 40, 62, 50, 25, 
				113, 6, 74, 122, 23, 96, 57, 11, 72, 47, 30, 1, 10, 88, 119, 19, 
				127, 114, 78, 44, 53, 66, 60, 41, 55, 38, 2, 98, 103, 14, 106, 93, 
				100, 85, 68, 81, 75, 90, 42, 124, 4, 29, 112, 58, 24, 71, 33, 18
    		}; 

    // second key-Permutation Table (Eliminated 17,23,37,57,71,83,97,113)
    private static final int[] P120 = { 
				49, 95, 102, 61, 81, 1, 53, 77, 41, 24, 47, 110, 73, 68, 52, 48, 63, 44, 67, 27, 
				46, 84, 56, 5, 128, 38, 42, 22, 15, 9, 123, 82, 29, 118, 2, 32, 16, 106, 7, 54, 
				104, 19, 13, 75, 120, 69, 35, 20, 30, 100, 111, 14, 58, 99, 90, 65, 51, 86, 62, 125, 
				115, 25, 109, 59, 3, 70, 60, 33, 6, 94, 76, 66, 98, 72, 117, 4, 85, 122, 10, 26,
				8, 34, 43, 11, 28, 119, 64, 107, 12, 36, 124, 80, 45, 127, 96, 105, 21, 50, 78, 108, 
				31, 88, 93, 55, 126, 40, 87, 74, 112, 103, 116, 39, 121, 79, 91, 18, 89, 92, 101, 114
			}; 
    
    //Shift bits for key generation
    private static final int[] shiftBits = { 2, 5, 10, 21, 50 }; 

	//Initial permutation
    private static final int[] IP = { 
	    		26, 61, 89, 73, 100, 78, 91, 102, 111, 77, 62, 59, 14, 98, 72, 27,
	    		125, 8, 37, 69, 34, 19, 105, 20, 44, 88, 10, 83, 47, 33, 84, 116,
	    		48, 71, 2, 23, 93, 128, 3, 124, 106, 36, 50, 24, 52, 4, 70, 99,
	    		109, 30, 43, 97, 15, 46, 40, 42, 1, 75, 65, 56, 81, 67, 114, 39,
	    		53, 82, 104, 28, 80, 101, 35, 121, 25, 112, 31, 95, 17, 92, 96, 119,
	    		13, 16, 85, 127, 22, 9, 45, 12, 57, 29, 21, 11, 123, 126, 6, 60,
	    		94, 64, 7, 55, 18, 66, 32, 108, 63, 103, 86, 79, 38, 68, 107, 122,
	    		74, 110, 49, 87, 51, 58, 76, 90, 54, 5, 115, 118, 113, 120, 117, 41 
            }; 
  
    // Inverse Initial Permutation Table 
    private static final int[] FP = { 
	    		57, 35, 39, 46, 122, 95, 99, 18, 86, 27, 92, 88, 81, 13, 53, 82, 
	    		77, 101, 22, 24, 91, 85, 36, 44, 73, 1, 16, 68, 90, 50, 75, 103, 
	    		30, 21, 71, 42, 19, 109, 64, 55, 128, 56, 51, 25, 87, 54, 29, 33, 
	    		115, 43, 117, 45, 65, 121, 100, 60, 89, 118, 12, 96, 2, 11, 105, 98, 
	    		59, 102, 62, 110, 20, 47, 34, 15, 4, 113, 58, 119, 10, 6, 108, 69, 
	    		61, 66, 28, 31, 83, 107, 116, 26, 3, 120, 7, 78, 37, 97, 76, 79, 
	    		52, 14, 48, 5, 70, 8, 106, 67, 23, 41, 111, 104, 49, 114, 9, 74, 
	    		125, 63, 123, 32, 127, 124, 80, 126, 72, 112, 93, 40, 17, 94, 84, 38 
    		}; 

    // Expansion D-box Table 
    private static final int[] EP = { 
	    		1, 4, 2, 14, 21, 13, 5, 13, 31, 3,
	    		7, 10, 22, 12, 11, 17, 15, 32, 29, 16,
	    		9, 27, 24, 18, 2, 17, 5, 29, 8, 11,
	    		29, 9, 30, 16, 23, 8, 24, 28, 12, 1,
	    		27, 15, 6, 25, 14, 19, 26, 20, 3, 31,
	    		6, 30, 19, 21, 26, 22, 18, 25, 28, 7
    		}; 
  
    // S-box Table 
    public static final int[][][] sbox = { 
            { { 0, 8, 10, 2, 11, 4, 14, 6 }, 
              { 9, 7, 1, 12, 3, 13, 5, 15 }, 
              { 2, 14, 11, 1, 10, 15, 12, 3 }, 
              { 9, 4, 8, 5, 13, 6, 0, 7 }, 
              { 1, 8, 9, 14, 3, 12, 5, 13 },
              { 11, 7, 2, 10, 15, 4, 0, 6 },
              { 2, 5, 11, 14, 4, 1, 7, 13 },
              { 10, 0, 9, 3, 6, 12, 15, 8 }
            }, 
            { { 10, 0, 14, 8, 2, 6, 13, 4 }, 
              { 15, 9, 1, 11, 7, 3, 5, 12 }, 
              { 0, 2, 10, 4, 12, 6, 14, 8 }, 
              { 1, 9, 3, 11, 5, 13, 7, 15 }, 
              { 15, 1, 13, 3, 11, 5, 9, 7 },
              { 0, 14, 2, 12, 4, 10, 6, 8 },
              { 7, 9, 5, 11, 3, 13, 1, 15 },
              { 8, 6, 10, 4, 12, 2, 14, 0 }
            }, 
            { { 9, 11, 7, 13, 1, 6, 3, 15 }, 
              { 10, 8, 12, 0, 14, 2, 5, 4 }, 
              { 0, 7, 15, 6, 2, 11, 13, 4 }, 
              { 9, 8, 1, 10, 5, 12, 3, 14 }, 
              { 1, 3, 15, 5, 13, 7, 11, 9 },
              { 0, 2, 14, 4, 12, 6, 10, 8 },
              { 5, 7, 3, 1, 9, 11, 13, 15 },
              { 4, 6, 2, 0, 8, 10, 12, 14 }
            }, 
            { { 10, 9, 2, 12, 11, 4, 14, 7 }, 
              { 1, 10, 3, 8, 6, 13, 5, 15 }, 
              { 1, 2, 3, 7, 8, 11, 15, 10 }, 
              { 12, 13, 14, 0, 4, 5, 6, 9 }, 
              { 1, 2, 8, 6, 12, 4, 13, 15 },
              { 5, 11, 0, 3, 7, 9, 14, 10 },
              { 12, 11, 13, 8, 9, 14, 15, 0 },
              { 2, 1, 0, 7, 5, 6, 4, 3 }
            }, 
            { { 11, 10, 9, 8, 12, 13, 14, 15 }, 
              { 3, 2, 1, 0, 4, 5, 6, 7 }, 
              { 4, 6, 2, 7, 9, 12, 14, 15 }, 
              { 5, 3, 1, 0, 8, 11, 10, 13 }, 
              { 9, 7, 12, 6, 5, 11, 4, 15 },
              { 8, 13, 0, 10, 1, 14, 2, 3 },
              { 5, 3, 6, 13, 7, 14, 8, 9 },
              { 4, 12, 11, 0, 10, 1, 15, 2 }
            }, 
            { { 0, 2, 12, 5, 11, 15, 13, 14 }, 
              { 1, 3, 10, 4, 6, 7, 8, 9 }, 
              { 3, 1, 4, 6, 8, 10, 15, 13 }, 
              { 2, 0, 5, 7, 9, 11, 14, 12 }, 
              { 9, 13, 11, 7, 5, 3, 1, 15 },
              { 8, 12, 10, 6, 4, 2, 0, 14 },
              { 0, 4, 8, 12, 13, 9, 5, 1 },
              { 3, 7, 11, 15, 14, 10, 6, 2 }
            }, 
            { { 5, 1, 10, 12, 14, 7, 3, 0 }, 
              { 4, 2, 9, 13, 11, 8, 6, 15 }, 
              { 2, 11, 7, 6, 3, 12, 4, 5 }, 
              { 1, 8, 0, 15, 9, 10, 13, 14 }, 
              { 15, 13, 2, 3, 6, 7, 11, 10 },
              { 14, 12, 0, 1, 5, 4, 8, 9 },
              { 1, 6, 15, 8, 13, 4, 11, 10 },
              { 5, 3, 14, 7, 9, 12, 2, 0 }
            }, 
            { { 1, 15, 8, 9, 13, 12, 11, 5 }, 
              { 0, 2, 3, 10, 14, 7, 6, 4 }, 
              { 1, 2, 3, 4, 9, 13, 12, 11 }, 
              { 0, 5, 6, 7, 8, 14, 15, 10 }, 
              { 4, 5, 6, 7, 15, 14, 13, 12 },
              { 0, 1, 2, 3, 11, 10, 9, 8 },
              { 6, 5, 4, 3, 10, 11, 12, 15 },
              { 9, 0, 1, 2, 7, 8, 14, 13 }
            },
            { { 7, 10, 15, 3, 6, 11, 6, 1, }, 
              { 11, 1, 0, 8, 7, 9, 3, 12, }, 
              { 3, 5, 10, 2, 14, 4, 9, 14, }, 
              { 2, 4, 10, 15, 8, 0, 4, 7, }, 
              { 1, 6, 2, 8, 7, 13, 9, 15, },
              { 0, 12, 9, 13, 15, 14, 13, 10, },
              { 11, 5, 13, 2, 8, 3, 12, 14, },
              { 12, 5, 0, 4, 6, 1, 5, 11 }
            },
            { { 8, 0, 6, 3, 11, 0, 9, 3, }, 
              { 1, 12, 2, 7, 4, 10, 13, 12, }, 
              { 12, 5, 9, 8, 14, 6, 5, 0, }, 
              { 14, 4, 13, 11, 2, 3, 13, 7, }, 
              { 6, 5, 10, 1, 8, 7, 1, 9, },
              { 9, 11, 15, 14, 5, 12, 6, 4, },
              { 1, 7, 3, 14, 0, 10, 15, 15, },
              { 11, 4, 13, 2, 15, 8, 2, 10 }
            }
        }; 
        
    // Straight Permutation Table 
    private static final int[] P = { 
	    		4, 13, 20, 7, 18, 24, 3, 26,
	    		2, 14, 19, 32, 10, 16, 27, 17,
	    		8, 35, 40, 32, 5, 23, 25, 28,
	    		22, 9, 31, 21, 6, 30, 37, 29
    		}; 
  
    //Variable to store generated keys
    private static String keys[]=new String[5];
    
    
  //Thread class to support multithreading in s box evaluation
    private static class ThreadSBox extends Thread {
    	private String string,result;
    	private int index;
    	public ThreadSBox(String s, int i) {
    		string=s;
    		index=i;
    	}
    	public void run() {
    		int num = index/6;     //Determine which s box to be used
            int row = Integer.parseInt(string.charAt(0)+""+string.charAt(2)+""+string.charAt(4), 2); //Determing row using first 2 and last 2 character
            int col = Integer.parseInt(string.charAt(1)+""+string.charAt(3)+""+string.charAt(5), 2);   //Determine the column using middle characters
            result=Utility.toBinary(String.valueOf(sbox[num][row][col]),4); 
    	}
    	public String getValue() {
    		return result;
    	}
    }
    
    // preparing 16 keys for 16 rounds 
    public void generateKeys(String key) 
    {  
        key = Utility.permutation(P128, key);   //1st permutation
        for (int i = 0; i < 5; i++) { 
        	//Multithreading
        	try {
	        	ThreadCircularShift thread1 = new ThreadCircularShift(key.substring(0, 64), shiftBits[i]);
	        	ThreadCircularShift thread2 = new ThreadCircularShift(key.substring(64, 128), shiftBits[i]);
	            thread1.start();
	            thread2.start();
	            thread1.join();
	            thread2.join();
	            key = thread1.getValue() + thread2.getValue();
        	}
        	catch(InterruptedException e) {e.printStackTrace();}
        	
        	keys[i] = Utility.permutation(P120, key);   //2nd permutation
        }
    } 
  
    // s-box lookup 
    private static String sBox(String input, String str) 
    { 
        StringBuffer output=new StringBuffer();
        ThreadSBox thread[] = new ThreadSBox[10];
        for (int i=0,j=0; i < 60; i+=6, j++) {   //Consider 6 bits at a time
            thread[j] = new ThreadSBox(input.substring(i, i+6),i);
            thread[j].start();
        }
        for(int j=0; j<10; j++) {
        	try {
        		thread[j].join();
        		output.append(thread[j].getValue());
        	} catch(InterruptedException e) {e.printStackTrace();}}
        return output.toString(); 
    } 
  
    //One Fiestel round
    private String round(String key, String input) 
    { 
        String left = input.substring(0, 64); 
        String right = input.substring(64, 128); 
        String temp = right;   
        temp = Utility.permutation(EP, temp.substring(0,32))+Utility.permutation(EP, temp.substring(32,64));  // Expansion permutation 
        temp = Utility.xor(temp, key);  // xor temp and round key
        temp = sBox(temp.substring(0,60),"a")+sBox(temp.substring(60,120),"b"); // lookup in s-box table
        temp = Utility.permutation(P, temp.substring(0,40))+Utility.permutation(P, temp.substring(40,80));  // Straight D-box 
        left = Utility.xor(left, temp);  
        return right+left;   //Swap and return
    } 
  
  //Encryption of plain text
    public File encrypt(File plainText, File cipherText, JProgressBar p, SwingWorker<Void,Void> worker) 
    { 
    	long iterations = plainText.length()/128;
    	int j;
    	double stepSize=25.0/iterations, currentValue=p.getValue();
    	try {
	    	Reader reader = new FileReader(plainText.getAbsolutePath());
			PrintWriter out = new PrintWriter(cipherText.getAbsolutePath());
	    	char buffer[] = new char[128];
	    	
	    	while(reader.read(buffer) != -1) {
	    		currentValue+=stepSize;
	    		p.setValue((int)currentValue); //Set value of progressBar
	    		String text=String.valueOf(buffer);  //Take 64 bit data
	        	text = Utility.permutation(IP, text);   //Initial permutation
	        	for(j=0; j<5; j++)  //16 Fiestel rounds
		        {
	        		if(worker.isCancelled()) return null;
		        	text = round(keys[j],text);
//		        	System.out.println("Round-"+(j+1)+" : "+text);

		        }
		        //Undo final swap done by round function
		        text = text.substring(64, 128) + text.substring(0, 64); 
		        text = Utility.permutation(FP, text); //final permutation
		        out.print(text);
		        
	    	}
	    	reader.close();
	    	out.close();
    	} 
    	catch(FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
        return cipherText;
    } 
  
  
    //Decryption of cipher text
    public File decrypt(File cipherText, File plainText, JProgressBar p, SwingWorker worker) 
    {   
    	long iterations = plainText.length()/128;
    	int j;
    	double stepSize=100.0/iterations, currentValue=p.getValue();
    	try {
	    	Reader reader = new FileReader(cipherText.getAbsolutePath());
			PrintWriter out = new PrintWriter(plainText.getAbsolutePath());
	    	char buffer[] = new char[128];
	    	
	    	while(reader.read(buffer) != -1) {
	    		currentValue+=stepSize;
	    		p.setValue((int)currentValue); //Set value of progressBar
	    		String text=String.valueOf(buffer);  //Take 64 bit data
	        	text = Utility.permutation(IP, text);   //Initial permutation
	        	for(j=4; j>=0; j--)  //16 Fiestel rounds
		        {
	        		if(worker.isCancelled()) return null;
		        	text = round(keys[j],text);
//		        	System.out.println("Round-"+(j+1)+" : "+text);
		        }
		        //Undo final swap done by round function
		        text = text.substring(64, 128) + text.substring(0, 64); 
		        text = Utility.permutation(FP, text); //final permutation
		        out.print(text);
	    	}
	    	reader.close();
	    	out.close();
    	} 
    	catch(FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
        return plainText;
    }
    
    public static String[] getKeys() {
    	return keys;
    }
} 
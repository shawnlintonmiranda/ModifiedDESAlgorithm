package utilities;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Utility {
	public static Map<Character,Integer> key=new LinkedHashMap<Character,Integer>();
	public static Map<Integer,Character> value=new LinkedHashMap<Integer,Character>();
	public static void createMap() //Map alphabets to some key values
	{
		//Mapping of values to keys
		value.put(0,'A'); value.put(1,'B'); value.put(2,'C'); value.put(3,'D');
		value.put(4,'E'); value.put(5,'F'); value.put(6,'G'); value.put(7,'H');
		value.put(8,'I'); value.put(9,'J'); value.put(10,'K'); value.put(11,'L');
		value.put(12,'M'); value.put(13,'N'); value.put(14,'O'); value.put(15,'P');
		value.put(16,'Q'); value.put(17,'R'); value.put(18,'S'); value.put(19,'T');
		value.put(20,'U'); value.put(21,'V'); value.put(22,'W'); value.put(23,'X');
		value.put(24,'Y'); value.put(25,'Z'); value.put(26,'a'); value.put(27,'b');
		value.put(28,'c'); value.put(29,'d'); value.put(30,'e'); value.put(31,'f');
		value.put(32,'g'); value.put(33,'h'); value.put(34,'i'); value.put(35,'j');
		value.put(36,'k'); value.put(37,'l'); value.put(38,'m'); value.put(39,'n');
		value.put(40,'o'); value.put(41,'p'); value.put(42,'q'); value.put(43,'r');
		value.put(44,'s'); value.put(45,'t'); value.put(46,'u'); value.put(47,'v');
		value.put(48,'w'); value.put(49,'x'); value.put(50,'y'); value.put(51,'z');
		value.put(52,'0'); value.put(53,'1'); value.put(54,'2'); value.put(55,'3');
		value.put(56,'4'); value.put(57,'5'); value.put(58,'6'); value.put(59,'7');
		value.put(60,'8'); value.put(61,'9'); value.put(62,'.'); value.put(63,' ');
		
		//Reverse Mapping
		key.put('A',0); key.put('B',1); key.put('C',2); key.put('D',3);
		key.put('E',4); key.put('F',5); key.put('G',6); key.put('H',7);
		key.put('I',8); key.put('J',9); key.put('K',10); key.put('L',11);
		key.put('M',12); key.put('N',13); key.put('O',14); key.put('P',15);
		key.put('Q',16); key.put('R',17); key.put('S',18); key.put('T',19);
		key.put('U',20); key.put('V',21); key.put('W',22); key.put('X',23);
		key.put('Y',24); key.put('Z',25); key.put('a',26); key.put('b',27);
		key.put('c',28); key.put('d',29); key.put('e',30); key.put('f',31);
		key.put('g',32); key.put('h',33); key.put('i',34); key.put('j',35);
		key.put('k',36); key.put('l',37); key.put('m',38); key.put('n',39);
		key.put('o',40); key.put('p',41); key.put('q',42); key.put('r',43);
		key.put('s',44); key.put('t',45); key.put('u',46); key.put('v',47);
		key.put('w',48); key.put('x',49); key.put('y',50); key.put('z',51);
		key.put('0',52); key.put('1',53); key.put('2',54); key.put('3',55);
		key.put('4',56); key.put('5',57); key.put('6',58); key.put('7',59);
		key.put('8',60); key.put('9',61); key.put('.',62); key.put(' ',63);
	}
	
	//Thread for circular shift during key generation
    public static class ThreadCircularShift extends Thread {
    	private String string,result;
    	private int bits;
    	public ThreadCircularShift(String s, int b) {
    		string=s;
    		bits=b;
    	}
    	public void run() {
    		int j;
        	char[] chars = string.toCharArray();
            for(int i=1; i<=bits; i++) {
            	char temp=chars[0];
            	for(j=1; j<string.length();j++)
            		chars[j-1]=chars[j];
            	chars[j-1]=temp;
            }
            result = String.valueOf(chars); 
    	}
    	public String getValue() {
    		return result;
    	}
    }

	//Converts character string to binary string (for key)
	public static String toBinary(String input) throws UnsupportedCharacterException 
    { 
		StringBuffer binary=new StringBuffer(""), temp=new StringBuffer("");
        for(int i=0; i<input.length(); i++) {
        	temp.setLength(0);  //Clear string builder
        	try {
        		temp.append(Long.toBinaryString(Utility.key.get(input.charAt(i)))); 
        	} catch(NullPointerException e)  //Other than defined 64 characters
        	{
        		throw new UnsupportedCharacterException();
        	}
        	while (temp.length() < 6)   //Convert each character to 6 bits by prepending 0's
        		temp.insert(0, '0');
        	binary.append(temp);
        }
        return binary.toString(); 
    }
	
	//Converts character string to binary and stores in file (for textbox data)
	public static File toBinary(StringBuffer input, JProgressBar progress) throws UnsupportedCharacterException 
    { 
		StringBuffer temp=new StringBuffer("");
		File file = new File("generated/plainText.txt");
		int length = input.length();
		int halfLength = length/2;
		try {
			FileWriter fw = new FileWriter(file);
	        for(int i=0; i<length; i++) {
	        	if(i==halfLength) progress.setValue(progress.getValue()+1); //Setting progress bar
	        	temp.setLength(0);  //Clear string builder
        		temp.append(Long.toBinaryString(Utility.key.get(input.charAt(i)))); 
	        	
	        	while (temp.length() < 6)   //Convert each character to 6 bits by prepending 0's
	        		temp.insert(0, '0');
	        	fw.write(temp.toString());
	        }
			fw.close();
		}
		catch(NullPointerException e) { //Other than defined 64 characters
    		throw new UnsupportedCharacterException();
    	}
		catch(IOException e) {
			e.printStackTrace();
		}
        return file;
    }
	
	//Converts data in file to binary and stores in file (for file input)
	public static File toBinary(File input, JProgressBar progress) throws UnsupportedCharacterException 
    { 
		StringBuffer temp=new StringBuffer("");
		File file = new File("generated/plainText.txt");
		try {
			FileWriter fw = new FileWriter(file);
			FileInputStream src=new FileInputStream(input);
			int ch;
			progress.setValue(progress.getValue()+1); //Setting progress bar
			while((ch=src.read())!=-1)
			{
				temp.setLength(0);  //Clear string builder
        		temp.append(Long.toBinaryString(Utility.key.get((char)ch))); 
	        	
	        	while (temp.length() < 6)   //Convert each character to 6 bits by prepending 0's
	        		temp.insert(0, '0');
	        	fw.write(temp.toString());
			}
			fw.close();
			src.close();
		}
		catch(NullPointerException e) { //Other than defined 64 characters
    		throw new UnsupportedCharacterException();
    	}
		catch(IOException e) {
			e.printStackTrace();
		}
        return file;
    }
	
	public static String toBinary(String input,int bits) //used in s-box reference
	{
        input = Long.toBinaryString(Long.parseUnsignedLong(input, 10)); 
        while (input.length() < bits) 
            input = "0" + input; 
        return input; 	
	}
  
    //Converts binary string to Character string
	public static String toString(String binary) 
    { 
		StringBuffer output=new StringBuffer();
        char ch; 
        for(int i=0; i<binary.length(); i+=6) {
            if(i+6<=binary.length()) {
            	ch=(char)Integer.parseUnsignedInt(binary.substring(i, i+6), 2); 
        		ch=Utility.value.get((int)ch);
        		output.append(ch);
            }
            else {
            	int remaining=binary.length()-i;
            	if(remaining==2)
            		output.append("[\\2]");  //Denotes last 2 characters left out
            	else if(remaining==4)
            		output.append("[\\4]");   //Denotes last 4 characters left
            }
        }
        return output.toString(); 
    } 

	public static StringBuffer loadTextBox(File data, JTextArea jText, JProgressBar p) 
    { 
		if(p!=null) p.setValue(p.getValue()+1); //Set progress bar value
		StringBuffer output=new StringBuffer();
        char ch; 
        try {
        	int r=0;
	        Reader reader = new FileReader(data.getAbsolutePath());
	        char buffer[] = new char[6];
	        
	        while((r=reader.read(buffer)) != -1) {
	        	if(r==6) {
		    		String text=String.valueOf(buffer);  //Take 64 bit data
		        	ch=(char)Integer.parseUnsignedInt(text, 2); 
		    		ch=Utility.value.get((int)ch);
		    		output.append(ch);
	        	}
	        	else if(r==4) 
	        		output.append("[\\4]");  //Denotes last 2 characters left out
	        	else if(r==2)
	        		output.append("[\\2]");   //Denotes last 4 characters left
	        }
	        if(p!=null) p.setValue(p.getValue()+1); //Set progress bar value
//	        buffer = new char[4];
//	        if(reader.read(buffer) != -1) 
//	    		output.append("[\\4]");  //Denotes last 2 characters left out
//	        buffer = new char[2];
//	        if(reader.read(buffer) != -1)
//	    		output.append("[\\2]");   //Denotes last 4 characters left
	        reader.close();
	        System.out.println("\n\n\n");
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
        jText.setText(output.toString());
        return output; 
    } 
	
	//Convert text by appending blank spaces
	public static File convertText(File input, File output, int bits, JProgressBar p, SwingWorker<Void,Void> worker) throws UnsupportedCharacterException
	{
		p.setValue(p.getValue()+1); //Set progress bar value
		try {
			FileInputStream src = new FileInputStream(input.getAbsolutePath());
			PrintWriter out = new PrintWriter(output.getAbsolutePath());
			int i;
			while((i=src.read()) != -1) 
				out.print((char)i);
			src.close();
			
			if(worker.isCancelled()) {out.close(); return null;}
			p.setValue(p.getValue()+1); //Set progress bar value
			long n = input.length();
			String str = Utility.toBinary(" "+(n/6));
			p.setValue(p.getValue()+1); //Set progress bar value
			n=n+str.length();
			out.print(str);
			while((n++)%bits != 0)  {
				out.print("1"); System.out.println("hi"+bits);}
			out.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
        return output;
	}
	
	//Recover original text
	public static void recoverText(File input)
	{
		StringBuffer sb = new StringBuffer("");
		boolean flag1=false,flag2=false;
		try {
			Reader reader = new FileReader(input.getAbsolutePath());
	    	char buffer[] = new char[6];
	    	
	    	while(reader.read(buffer) == 6) {
	    		char ch=Utility.toString(String.valueOf(buffer)).charAt(0);  //Take 64 bit data
	    		System.out.println(ch);
	    		if(ch==' ' && !flag1 && !flag2) 
	    			flag1=true;
	    		else if(ch!=' ' && flag1 && !flag2)
	    			sb.append(ch);
	    		else if(ch==' ' && flag1 && !flag2)
	    			flag2=true;
	    		else if(ch!=' ' && flag1 && flag2) {
	    			flag2 = false;
	    			sb.setLength(0);
	    			sb.append(ch);
	    		}
	    	}
	    	reader.close();
	    	RandomAccessFile file = new RandomAccessFile(input,"rw");
			file.setLength(Integer.parseInt(sb.toString())*6);
			file.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    //Compute permutation based on specified sequence
    public static String permutation(int[] sequence, String input) 
    { 
        StringBuffer output=new StringBuffer();
        for (int i = 0; i < sequence.length; i++)
            output.append(input.charAt(sequence[i]-1));
        return output.toString(); 
    } 
  
    //Compute XOR operation of two binary strings
    public static String xor(String a, String b) 
    { 
        StringBuffer output=new StringBuffer();
        for(int i=0; i<a.length(); i++)
        {
        	if((a.charAt(i)=='0' && b.charAt(i)=='0') || (a.charAt(i)=='1' && b.charAt(i)=='1'))
        		output.append('0');
        	else
        		output.append('1');
        }
        return output.toString(); 
    } 
}

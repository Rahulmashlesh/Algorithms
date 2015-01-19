/*
 * CharSet.java
 * Author: Poorn Pragya
 * Created on: Oct 26th, 2014
 * This class is used by Huffman_code program to store char set and their frequencies
 */

public class CharSet {

	Character c;
	int freq;
	CharSet left;
	CharSet right;
	
	public CharSet(Character c, int freq) {
		this.c=c;
		this.freq=freq;
		left=null;
		right=null;
	}

}

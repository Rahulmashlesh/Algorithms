/*
 * Hufman_Code.java
 * Author: Poorn Pragya
 * Created on: Oct 26th, 2014
 * This program takes command line input as English text file and encodes it with huffman code 
 * and decodes the same 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Huffman_Code {

	// Data member to store file contents
	String fileContents;

	// Data member to store unique character set in file along with frequency of
	// occurrence
	CharSet charSet[];

	// Hash table to store huffman code for each unique character
	Map<Character, String> huffmanCodeTable;

	// Data member to store the huffman encoded string
	String encoded_string;

	// Data member to store file size
	long file_size;

	/*
	 * Constructor reads the contents of the file and stores it in Data Member
	 * fileContents, then initializes charSet[] array and huffmanCodeTable
	 */
	public Huffman_Code(String path) throws FileNotFoundException {
		this.readFile(path);
		Map<Character, Integer> charSetFreqCount = this
				.generateCharSetFrequency();
		charSet = new CharSet[charSetFreqCount.size()];
		int j = 0;

		for (Character i : charSetFreqCount.keySet())
			charSet[j++] = new CharSet(i, charSetFreqCount.get(i));
		this.huffmanCodeTable = new LinkedHashMap<Character, String>();
	}

	/*
	 * This function uses hash table to generate frequency of occurrence of each
	 * unique character
	 */
	Map<Character, Integer> generateCharSetFrequency() {
		Map<Character, Integer> hashMap = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < this.fileContents.length(); i++) {
			char c = this.fileContents.charAt(i);
			if (hashMap.containsKey(c)) {
				hashMap.put(c, hashMap.get(c) + 1);
			} else {
				hashMap.put(c, 1);
			}
		}
		return hashMap;
	}

	/*
	 * This function is used to print each character along with its frequency of
	 * occurrence
	 */
	void printCharSet() {
		for (CharSet l : charSet) {
			if (l.c == '\n')
				System.out.print("<New Line>" + ":" + l.freq + "\t");
			else if (l.c == ' ')
				System.out.print("<White Space>" + ":" + l.freq + "\t");
			else if (l.c == '\t')
				System.out.print("<Tab Space>" + ":" + l.freq + "\t");
			else
				System.out.print("'" + l.c + "'" + ":" + l.freq + "\t");
		}
		System.out.println();
	}

	/*
	 * This function uses Min Heap to creates the huffman tree and returns the
	 * root pointer
	 */
	CharSet huffmanTreeCreation() {
		CharSet left, right, root;
		Heap h = new Heap(charSet);
		while (h.size() > 1) {
			left = h.ExtractMin();
			right = h.ExtractMin();
			root = new CharSet(null, left.freq + right.freq);
			root.left = left;
			root.right = right;
			h.insert(root);
		}
		return h.ExtractMin();
	}

	/*
	 * This function recursively generates the Huffman code table and stores the
	 * result in data member huffmanCodeTable which is a hash table
	 */
	void genrateHuffmanCodeTable(CharSet root, int stack[], int top) {

		/*
		 * Recursively assign weight to left edge '0' and right edge '1' and
		 * store the weights in stack as tree is traversed
		 */
		if (root.left != null) {
			stack[top] = 0;
			genrateHuffmanCodeTable(root.left, stack, top + 1);
		}

		if (root.right != null) {
			stack[top] = 1;
			genrateHuffmanCodeTable(root.right, stack, top + 1);
		}

		/*
		 * if a leaf node is found, pop the contents of the stack and store it.
		 */
		if (root.left == null && root.right == null) {
			String temp = "";
			for (int i = 0; i < top; i++)
				temp = temp + stack[i];
			this.huffmanCodeTable.put(root.c, temp);
		}

	}

	/*
	 * This function is used to print the data member huffmanCodeTable
	 */
	void printHuffmanCodeTable() {
		for (Character i : this.huffmanCodeTable.keySet()) {
			if (i == '\n')
				System.out.println("<New Line>" + ": "
						+ this.huffmanCodeTable.get(i));
			else if (i == ' ')
				System.out.println("<White Space>" + ": "
						+ this.huffmanCodeTable.get(i));
			else if (i == '\t')
				System.out.println("<Tab Space>" + ": "
						+ this.huffmanCodeTable.get(i));
			else

				System.out.println(i + ": " + this.huffmanCodeTable.get(i));
		}
	}

	/*
	 * This function is used to encode the given text using generated huffman
	 * codes and writes the encoded bits to a file
	 */
	void encodeText(String destFileName) throws IOException {
		File file = new File(destFileName);
		BufferedWriter f = new BufferedWriter(new FileWriter(file));
		String Text_code = new String();
		for (int i = 0; i < this.fileContents.length(); i++) {
			/*
			 * for each text character lookup the corresponding huffman code in
			 * hash table and append the code a string text_code
			 */
			char c = this.fileContents.charAt(i);
			Text_code = Text_code + this.huffmanCodeTable.get(c);
			f.append(this.huffmanCodeTable.get(c));
		}
		encoded_string = Text_code;
		f.close();
		System.out.println("Huffman Encoded Text:" + Text_code);
	}

	/*
	 * This function is used to decode a particular encoded string using huffman
	 * tree
	 */
	void decode(CharSet root) {
		CharSet temp = root;
		System.out.println("Decoded Text is: ");

		for (int i = 0; i < this.encoded_string.length(); i++) {
			/*
			 * In encoded_string if 0 is found go left, if 1 is found go right
			 * if leaf node is found print the character and restart from the
			 * root s
			 */
			if (temp.left == null && temp.right == null) {
				System.out.print(temp.c);
				temp = root;
			}

			if (this.encoded_string.charAt(i) == '0')
				temp = temp.left;
			if (this.encoded_string.charAt(i) == '1')
				temp = temp.right;
		}

		if (temp.left == null && temp.right == null) {
			System.out.print(temp.c);
			temp = root;
		}
	}

	/*
	 * This dunction calculates the min amount of bits required to represent the
	 * entire text in fixed code length
	 */
	long fixedCodeLength() {
		long len = huffmanCodeTable.size();
		long n = 0;
		long optimal_length = 1;
		while (optimal_length < len) {
			n++;
			optimal_length = (int) Math.pow(2, n);
		}
		return n;
	}

	/*
	 * This function is used to compute the compression ratio of fixed length
	 * code vs huffman code
	 */
	void compressionRatio() {
		long huffman_code_length = encoded_string.length();
		long fixed_code_length = fixedCodeLength() * fileContents.length();
		System.out.println();
		System.out
				.println("Using minimum length bit code to compute compression ratio--->");
		System.out.println("Total number of unqique characters in the file= "
				+ huffmanCodeTable.size());
		System.out
				.println("Using Fixed Length encoding, each unique character in text can be represented by "
						+ fixedCodeLength() + " bit code");
		System.out
				.println("Total size of encoded text length using fixed length encoding in bits="
						+ fixed_code_length
						+ "\n"
						+ "Total size of encoded text length using huffman encoding in bits="
						+ huffman_code_length);
		System.out.println("Compression Ratio= " + (float) fixed_code_length
				/ huffman_code_length);
		System.out.println();
		System.out
				.println("Using File Size(ASCII code) to compute Compression ratio---> ");
		System.out.println("Total size of uncompressed file in bits="
				+ file_size * 8);
		System.out.println("Total size of compressed file in bits="
				+ huffman_code_length);
		System.out.println("Compression Ratio= " + (float) (file_size * 8)
				/ huffman_code_length);

	}

	/*
	 * This function reads the contents of the file and stores the result in
	 * data member fileContents
	 */
	void readFile(String path) throws FileNotFoundException {
		File file = new File(path);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file, "utf-8");
		this.file_size = file.length();
		this.fileContents = "";
		while (sc.hasNextLine()) {
			this.fileContents += sc.nextLine();
			if (sc.hasNextLine())
				this.fileContents += '\n';
		}
	}

	// Main method
	public static void main(String[] args) throws IOException {

		if (args.length < 2) {
			System.out.println("Please enter file name with absolute path to read and dest file name to write encoded bits");
			System.exit(1);
		}

		long total_time = 0;
		// Object creation call the constructor and finds the unique charset
		// with frequency
		Huffman_Code obj = new Huffman_Code(args[0]);

		System.out.println("Printing fequency of each character in text-->");
		obj.printCharSet();

		// Creating huffman tree
		long Huffman_Tree_Creation_start = System.currentTimeMillis();
		CharSet root = obj.huffmanTreeCreation();
		long Huffman_Tree_Creation_end = System.currentTimeMillis();
		total_time += Huffman_Tree_Creation_end - Huffman_Tree_Creation_start;

		// Creating huffman code table
		int[] stack = new int[700];
		long Code_Table_start = System.currentTimeMillis();
		obj.genrateHuffmanCodeTable(root, stack, 0);
		long Code_Table_end = System.currentTimeMillis();
		total_time += Code_Table_end - Code_Table_start;

		System.out.println("");
		System.out
				.println("Printing Huffman Code for each unique character in text file--->");
		obj.printHuffmanCodeTable();

		// Encoding the text file with huffman encoding
		System.out.println();
		long Encode_start = System.currentTimeMillis();
		obj.encodeText(args[1]);
		long Encode_end = System.currentTimeMillis();
		total_time += Encode_end - Encode_start;

		// Decoding the encoded text
		System.out.println();
		long Decode_start = System.currentTimeMillis();
		obj.decode(root);
		long Decode_end = System.currentTimeMillis();
		System.out.println();
		System.out.println();
		total_time += Decode_end - Decode_start;

		System.out.println("Time Complexity Analysis--->");
		System.out.println("Execution time of Huffman Tree Creation: "
				+ (Huffman_Tree_Creation_end - Huffman_Tree_Creation_start)
				+ " milli secs");
		System.out.println("Execution time of Huffman Code generation: "
				+ (Code_Table_end - Code_Table_start) + " milli secs");
		System.out.println("Execution time of Encoding Text: "
				+ (Encode_end - Encode_start) + " milli secs");
		System.out.println("Execution time of Decoding Text: "
				+ (Decode_end - Decode_start) + " milli secs");
		System.out.println("Total Time taken for entire process= " + total_time
				+ " milli sec");

		System.out.println();
		System.out.println();
		System.out.println("Compression Analysis-->");
		obj.compressionRatio();
	}

}

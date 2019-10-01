package org.commoncrawl.examples.mapreduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class IndexTermsUtils {
	
	public static String[] getTerms(String filename) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> terms = new ArrayList<String>();
		while (scanner.hasNextLine()) {
			terms.add(scanner.nextLine());
		}
		
		return terms.toArray(new String[0]);
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		System.out.println("---");
		String[] terms = getTerms("index_terms.txt");
		for (String t : terms) {
			System.out.println(t);
		}
		System.out.println("---");
	}

}

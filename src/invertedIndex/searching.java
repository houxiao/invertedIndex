package invertedIndex;

import java.io.*;
import java.util.Scanner;
import invertedIndex.InvertedIndex;

public class searching {
    public static void main(String[] args) throws IOException {
        InvertedIndex index = new InvertedIndex();
        index.read();
        index.createIndex("input.txt");
        System.out.println("please input words you want to search (split by space):");
		@SuppressWarnings("resource")
		Scanner scanner=new Scanner(System.in);
        String str=scanner.nextLine();
        index.search(str);
    }
}

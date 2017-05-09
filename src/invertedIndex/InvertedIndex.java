/*
*Name: Xiao Hou
*SID: 10406833
*Description: define the class of inverted index, 
*this class contains method to load file collection,
*	create inverted index, 
*	and searching word from index.
*/

package invertedIndex;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import invertedIndex.Trie;

public class InvertedIndex {

	private Trie index;// trie of total
	private Map<Integer, Integer> subIndex;// map of location and frequency info

	public void read() throws IOException// read files and combine them together
	{
		// Load stop word list file and save to a set
		// stop word list, according to:
		// http://patft.uspto.gov/netahtml/PTO/help/stopword.htm
		Scanner sc = new Scanner(new File("stopword.txt"));
		Set<String> stopW = new HashSet<String>();
		while (sc.hasNextLine()) {
			stopW.add(sc.nextLine());
		}
		sc.close();

		String[] filename = { "1.txt", "2.txt", "3.txt", "4.txt", "5.txt" };
		BufferedWriter fileWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("input.txt")));
		for (String fn : filename) {
			BufferedReader fileRead = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));
			String line = null;
			while ((line = fileRead.readLine()) != null) {
				String[] words = line.toLowerCase().split(" ");
				String nLine = "";
				for (String word : words) {
					// judge if a word is a stopword
					if (!stopW.contains(word)) {
						nLine += word + " ";
					}
				}
				fileWrite.write(nLine.toLowerCase());
				fileWrite.flush();
			}
			fileWrite.newLine();
			fileRead.close();
		}
		fileWrite.close();
	}

	// create inverted index for file collection
	public void createIndex(String filePath) {
		index = new Trie();

		try {
			File file = new File(filePath);
			InputStream is = new FileInputStream(file);
			BufferedReader read = new BufferedReader(new InputStreamReader(is));

			String temp = null;
			int line = 1;// initial line to 1, start from line 1
			while ((temp = read.readLine()) != null) {
				String[] words = temp.split(" ");
				for (String word : words) {
					if (!index.search(word)) {
						subIndex = new HashMap<Integer, Integer>();
						subIndex.put(line, 1);// save location, set frequency=1
						index.insert(word, subIndex);// save word and location
					} else {
						subIndex = index.getsubI(word);
						if (subIndex.containsKey(line)) {
							int count = subIndex.get(line);
							subIndex.put(line, count + 1);
						} else {
							subIndex.put(line, 1);
						}
					}
				}
				line++;
			}
			read.close();
			is.close();
		} catch (IOException e) {
			System.out.println("error in read file");
		}
	}

	// searching words, output location and frequency.
	public void search(String str) {
		String[] words = str.toLowerCase().split(" ");
		for (String word : words) {
			StringBuilder sb = new StringBuilder();
			if (index.search(word)) {
				sb.append("word: " + word + " in ");
				
				//using treemap and comparator to sort the information 
				//to output the files with higher relevance before files with lower relevance
				Map<Integer, Integer> temp = new TreeMap<Integer, Integer>();
				temp = index.getsubI(word);
				List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(temp.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Entry<Integer, Integer> c1, Entry<Integer, Integer> c2) {
						return c2.getValue().compareTo(c1.getKey());
					}
				});
				for (Map.Entry<Integer, Integer> e : list) {
					sb.append("file " + e.getKey() + " [" + e.getValue() + "]  ");
				}
			} else {
				sb.append("word: " + word + " not found");
			}
			System.out.println(sb);
		}
	}

}

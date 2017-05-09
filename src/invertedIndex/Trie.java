/*
*Name: Xiao Hou
*SID: 10406833
*Description: Define the class of trie, 
*and method to insert word, search, delete word and get subIndex.
*/

package invertedIndex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class Node {
	char content;
	boolean isEnd;
	int count;
	LinkedList<Node> childList;
	Map<Integer, Integer> subI;
	
	public Node(char c) {
		childList = new LinkedList<Node>();
		isEnd = false;
		content = c;
		count = 0;
		subI = new HashMap<Integer, Integer>();
	}

	public Node subNode(char c) {
		if (childList != null) {
			for (Node eachChild : childList) {
				if (eachChild.content == c) {
					return eachChild;
				}
			}
		}
		return null;
	}
}

public class Trie {
	private Node root;

	public Trie() {
		root = new Node(' ');
	}

	// define insert method
	public void insert(String word, Map<Integer, Integer> subIndex ) {
		if (search(word) == true)
			return;

		Node current = root;
		for (int i = 0; i < word.length(); i++) {
			Node child = current.subNode(word.charAt(i));
			if (child != null) {
				current = child;
			} else {
				current.childList.add(new Node(word.charAt(i)));//add characters
				current = current.subNode(word.charAt(i));
			}
			current.count++;
		}
		current.subI=subIndex;
		// set isEnd to indicate end of the word
		current.isEnd = true;
	}

	//search if a word exists in trie
	public boolean search(String word) {
		Node current = root;

		for (int i = 0; i < word.length(); i++) {
			if (current.subNode(word.charAt(i)) == null)
				return false;
			else
				current = current.subNode(word.charAt(i));
		}
		if (current.isEnd == true)
			return true; //complete the word and complete searching
		else //e.g. searching "ba" in balls, the searching is not complete
			return false;
	}

	//define deleting from trie (possibly not been used)
	public void deleteWord(String word) {
		if (search(word) == false)
			return;

		Node current = root;
		for (char c : word.toCharArray()) {
			Node child = current.subNode(c);
			if (child.count == 1) {
				current.childList.remove(child);
				return;
			} else {
				child.count--;
				current = child;
			}
		}
		current.subI = new HashMap<Integer, Integer>();
		current.isEnd = false;
	}
	
	//define method to get subIndex of a word 
	//refers to the location and frequency information of this word
	public Map<Integer, Integer> getsubI(String word){
			Node current=root;
		for (int i = 0; i < word.length(); i++) {
				current = current.subNode(word.charAt(i));
		}
		return current.subI;		
	}
}
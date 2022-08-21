package typingTutor;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;

//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE
	
	private static  FallingWord[] words; //list of words
	private static int noWords; //how many
	private static Score score; //user score
	
	CatchWord(String typedWord) {
		target=typedWord;
	}
	
	public static void setWords(FallingWord[] wordList) {
		words=wordList;	
		noWords = words.length;
	}
	
	public static void setScore(Score sharedScore) {
		score=sharedScore;
	}
	
	public static void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}
	
	public void run() {
		int i=0;
		ArrayList<FallingWord> temp = new ArrayList<FallingWord>();
		int tempIndex = 0;

		while (i<noWords) {		
			while(pause.get()) {};
			if (words[i].matchWord(target)) {    
				temp.add(words[i]);	//Place all matching words in the temp array
				tempIndex++;
			}
		   i++;
		}

		//When loop is done, update Score and remove FallingWord only if matching word(s) found
		if (temp.length >= 1){
			FallingWord lowest;
			double lowestY = 0d;

			for (FallingWord word: temp){ 	//Loop through all matching words to determine the lowest
				if (word.getY() > lowestY){
					lowestY = word.getY();	//Set new lowest word
					lowest = word;		
				}
			}

			//Reset the lowest word and update score
			lowest.resetWord();
			System.out.println( " score! '" + target); //for checking
			score.caughtWord(target.length());
		}
	}	
}

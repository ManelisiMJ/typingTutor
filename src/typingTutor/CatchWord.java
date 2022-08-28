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
		//Compare the typed word with hungry word first
		if (words[noWords-1].matchWord(target)){		//HungryWord is always last in the words array		
			words[noWords-1].setWord("HungryWord found");		//Change the hungry word to indicate it has been found
			System.out.println( " score HungryWord! '" + target);
			score.caughtWord(target.length());					//Update score
			words[noWords-1].dropHungryWord();					//Take it out of the screen
		}

		else{
			int i=0;
			ArrayList<FallingWord> temp = new ArrayList<FallingWord>();
			while (i<noWords-1) {			//Loop through other words		
				while(pause.get()) {};
				if (words[i].matchWord(target))  
					temp.add(words[i]);	//Place all matching words in the temp array
			   i++;
			}
	
			//When loop is done, check if matching word(s) found
			if (temp.size() >= 1){
				FallingWord lowest = null;
				double lowestY = 0d;
	
				for (FallingWord word: temp){ 	//Loop through all matching words to determine the lowest. Hungry word takes precedence
					if (word.getY() > lowestY){
						lowestY = word.getY();	//Set new lowest word
						lowest = word;		
					}
				}
				//Reset the lowest word and update score
				lowest.resetWord();
				System.out.println( " score! '" + target); 
				score.caughtWord(target.length());
			}
		}		
	}	
}

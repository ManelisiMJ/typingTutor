package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends Thread {
	private FallingWord myWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	private CountDownLatch hungryLatch;
	
	HungryWordMover( FallingWord word) {
		myWord = word;
	}
	
	HungryWordMover( FallingWord word,WordDictionary dict, Score score,AtomicBoolean d, AtomicBoolean p) {
		myWord = word;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
	
	
	public void run() {
		System.out.println("HUNGRY WORD "+myWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
			while (!myWord.dropped() && !done.get()) {
				    myWord.slide(10);
					try {
						sleep(myWord.getSpeed()-5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord();
				return;
			}
		}
	}
	
}


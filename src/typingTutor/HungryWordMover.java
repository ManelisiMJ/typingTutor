package typingTutor;

import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends Thread {
	private FallingWord hungryWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	private CountDownLatch hungryLatch;
	private FallingWord[] words;
	
	public HungryWordMover( FallingWord word) {
		hungryWord = word;
	}
	
	public HungryWordMover( FallingWord word,WordDictionary dict, Score score,AtomicBoolean d, AtomicBoolean p, FallingWord[] words) {
		hungryWord = word;
		this.score=score;
		this.done=d;
		this.pause=p;
		this.words = words;
	}
	
	public void run() {
		System.out.println("HUNGRY WORD "+hungryWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
			while (!hungryWord.dropped() && !done.get()) {
					try {
						sleep(hungryWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		

					hungryWord.slide(10);

					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && hungryWord.dropped()) {
				if (!hungryWord.getWord().equals("HungryWord found")){		//Hungry word not found, missed++
					score.missedWord();
					System.out.println("HungryWord missed");
				}
				return;		//When dropped, stop the HungryWordMover thread by returning
			}
		}
	}
	
}


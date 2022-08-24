package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WordMover extends Thread {
	private FallingWord myWord, hungryWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	CountDownLatch startLatch; //so all can start at once
	
	WordMover( FallingWord word) {
		myWord = word;
	}
	
	WordMover( FallingWord word,WordDictionary dict, Score score,
			CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p, FallingWord hungry) {
		this(word);
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
		hungryWord = hungry;
	}
	
	/**
	 * Determines whether or not the FallingWord is overlapping with the HungryWord
	 * @return true if the FallingWord overlaps with the HungryWord
	 */
	public synchronized boolean overLap(){
		int myWordEndX = myWord.getX() + myWord.getWord().length()*13;
		int hungryEndX = hungryWord.getX() + hungryWord.getWord().length()*13;
		int myWordMid = (myWordEndX - myWord.getX())/2;
		boolean overLapX = false, overLapY =false, overLapXMid = false; 

		//If the y value of myWord falls in the range [hungryWord.getY()-15; hungryWord.getY()+15] then remove it
		if (myWord.getY() >= hungryWord.getY()-15 && myWord.getY() <= hungryWord.getY()+15)
			overLapY = true;

		//The myWord has a startX = word.getX() and an EndX calculated using the length of the word and a mid calculated using startX and endX
		//If the word's start X is in range [hungryWord.getX; hungryWord.endX] or 
		//If the word's end X is in range [hungryWord.getX; hungryWord.endX] then it has bumped into the hungry word
		
		if ((myWordEndX >= hungryWord.getX() && myWordEndX<=hungryEndX) || (myWord.getX() >= hungryWord.getX() 
		&& myWord.getX()<=hungryEndX))
			overLapX = true;

		//plus exceptional case for long myWord and short hungry words where myWord startX and endX don't fall within hungry word range of X
		if (myWordMid>=hungryWord.getX() && myWordMid<=hungryEndX){
			overLapXMid = true;
		}

		return overLapY && (overLapX || overLapXMid);
	}
	

	public void run() {
		//System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
		try {
			System.out.println(myWord.getWord() + " waiting to start " );
			startLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
		System.out.println(myWord.getWord() + " started" );
		while (!done.get()) {				
			while (!myWord.dropped() && !done.get()) {
					if (!hungryWord.getWord().equals("HungryWord found")){	//If HungryWord is still in play
						if (overLap()){			//normal word overlaps with HungryWord
							System.out.println(myWord.getWord()+" Colided with hungry word!");
							myWord.dropNormalWord();		//Remove the normal word
						}
					}
					myWord.drop(10);		//Drop the word

					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord();		//normal word fallen out of play
				myWord.resetWord();
			}
			myWord.resetWord();
		}
	}
	
}

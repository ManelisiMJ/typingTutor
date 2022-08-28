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
		AtomicBoolean overLapX , overLapY, overLapXMid; 
		overLapX = new AtomicBoolean(false);
		overLapY = new AtomicBoolean(false);
		overLapXMid = new AtomicBoolean(false);
		int myWordEndX = myWord.getX() + myWord.getWord().length()*13 +3 ; 
		int myWordMid = (myWordEndX + myWord.getX())/2;
		//If the y value of myWord falls in the range [hungryWord.getY()-15; hungryWord.getY()+15] then remove it
		synchronized(myWord){
			if (myWord.getY() >= hungryWord.getY()-12 && myWord.getY() <= hungryWord.getY()+15)
				overLapY.set(true);
		}
		//The myWord has a startX = word.getX() and an EndX calculated using the length of the word and a mid calculated using startX and endX
		//If the word's start X is in range [hungryWord.getX; hungryWord.endX] or 
		//If the word's end X is in range [hungryWord.getX; hungryWord.endX] then it has bumped into the hungry word
		synchronized(hungryWord){
			//Calculate hungry word's end x inside synchronized block as it may change
			int hungryEndX = hungryWord.getX() + hungryWord.getWord().length()*13 +3 ; 

			if ((myWordEndX >= hungryWord.getX() && myWordEndX<=hungryEndX) || (myWord.getX() >= hungryWord.getX() 
			&& myWord.getX()<=hungryEndX))
				overLapX.set(true);

			//Exceptional case for long myWord and short hungry words where myWord startX and endX don't fall within hungry word range of X
			if (myWordMid>=hungryWord.getX() && myWordMid<=hungryEndX)
				overLapXMid.set(true);
		}
		return overLapY.get() && (overLapX.get() || overLapXMid.get());
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
				myWord.drop(10);		//Drop the word
				
				if (!hungryWord.getWord().equals("HungryWord found")){	//If HungryWord is still in play
					synchronized(this){	//Check-then-act
						if (overLap()){			//normal word overlaps with HungryWord
							System.out.println(myWord.getWord()+" Colided with hungry word!");
							myWord.dropNormalWord();		//Remove the normal word
						}
					}
				}
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

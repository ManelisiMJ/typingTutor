package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends Thread {
	private FallingWord myWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	private CountDownLatch hungryLatch;
	private FallingWord[] words;
	
	public HungryWordMover( FallingWord word) {
		myWord = word;
	}
	
	public HungryWordMover( FallingWord word,WordDictionary dict, Score score,AtomicBoolean d, AtomicBoolean p, FallingWord[] words) {
		myWord = word;
		this.score=score;
		this.done=d;
		this.pause=p;
		this.words = words;
	}

	public synchronized boolean overLap(FallingWord normal){
		return true;
	}
	
	public void run() {
		System.out.println("HUNGRY WORD "+myWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
			while (!myWord.dropped() && !done.get()) {
				    myWord.slide(10);
					//synchronized(this){
					//	System.out.println("("+myWord.getX()+", "+myWord.getY()+")");
					//}
					
					/**int hungryEndX = myWord.getX()+(5*myWord.getWord().length());
					int hungryEndY = myWord.getY()+10;

					for (int i=0; i<words.length-1;i++){
						int otherEndX = words[i].getX()+(5*words[i].getWord().length());
						int otherEndY = words[i].getY()+10;
						
						if (words[i].getY() == myWord.getY()){//(!(otherEndX<myWord.getX() || hungryEndX<words[i].getX() || otherEndY < myWord.getY() || hungryEndY < words[i].getY())){
							System.out.println("OVERLAP!!");
						}
					}*/

					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				if (!myWord.getWord().equals("HungryWord found"))		//Hungry word not found, missed++
					score.missedWord();
				return;		//When dropped, stop the HungryWordMover thread by returning
			}
		}
	}
	
}


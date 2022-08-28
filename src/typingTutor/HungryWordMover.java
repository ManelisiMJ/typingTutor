package typingTutor;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Defines a thread object used to move the special FallingWord object that is a hungry word
 */
public class HungryWordMover extends Thread {
	private FallingWord hungryWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	
	public HungryWordMover( FallingWord word, Score score,AtomicBoolean d, AtomicBoolean p) {
		hungryWord = word;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
	
	public void run() {
		System.out.println("HUNGRY WORD "+hungryWord.getWord() + " started" );
		while (!done.get()) {				
			while (!hungryWord.dropped() && !done.get()) {	//While hungry word is still in play
					try {
						sleep(hungryWord.getSpeed());	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		

					hungryWord.slide(10);		//animate the word's movememnt

					while(pause.get()&&!done.get()) {};	//game paused
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


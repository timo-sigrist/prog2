package ch.zhaw.prog2.fxmlcalculator;
/**
 * Most basic interface for beeing an observer
 * @author bles
 *
 */
public interface IsObserver {
	/**
	 * This method is always called when an observed object
	 * changes
	 */
	void update();
}

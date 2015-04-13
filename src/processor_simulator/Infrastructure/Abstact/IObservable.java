package processor_simulator.Infrastructure.Abstact;

public interface IObservable<T>
{
	/**
	 * Adds the listener.
	 *
	 * @param listener
	 *            the listener
	 */
	void addListener(T listener);

	/**
	 * Removes the listener.
	 *
	 * @param listener
	 *            the listener
	 */
	void removeListener(T listener);
}

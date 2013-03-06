package ts;


public class EventFactory<E extends Event>{

	private final Class<? extends E> eventClass;

	public EventFactory(Class<? extends E> eventClass){
		this.eventClass = eventClass;
	}

	public E createEvent(){
		try {
			return eventClass.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Event factory failed", ex);
		}
	}
}
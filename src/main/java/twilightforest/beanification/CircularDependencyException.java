package twilightforest.beanification;

public class CircularDependencyException extends RuntimeException {

	@java.io.Serial
	private static final long serialVersionUID = 1L;

	public CircularDependencyException(String message) {
		super(message);
	}
}

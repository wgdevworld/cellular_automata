package cellsociety.Controller;

/**
 * Purpose: Custom XML exceptions to be thrown by XML Controller - caught and displayed by frontend
 * <p>
 * Assumptions: given error accurately describes error cause
 *
 * @author Jay Yoon
 */
public class XMLException extends RuntimeException {

  public XMLException(String message) {
    super(message);
  }

  public XMLException(Throwable cause, String message) {
    super(message, cause);
  }
}

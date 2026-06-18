package grupo2.docubot.exceptions.response;

public class NonPublishedDocumentException extends RuntimeException {
    public NonPublishedDocumentException(String message) {
        super(message);
    }
}

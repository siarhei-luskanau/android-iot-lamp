package siarhei.luskanau.iot.lamp.domain.exception;

public class SimpleErrorMessageFactory implements ErrorMessageFactory {

    @Override
    public String create(Throwable exception) {
        return exception.getMessage();
    }
}

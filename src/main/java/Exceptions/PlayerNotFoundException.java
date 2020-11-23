package Exceptions;

public class PlayerNotFoundException extends Exception {

    private static final long serialVersionUID = 6421798437058703463L;

    public PlayerNotFoundException(String message) {
        super(message);
    }
}

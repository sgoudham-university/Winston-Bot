package exceptions;

public class HeroNotFoundException extends Exception {

    private static final long serialVersionUID = 6421798437058703463L;

    public HeroNotFoundException(String message) {
        super(message);
    }
}

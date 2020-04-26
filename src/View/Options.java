package View;

public enum Options {

    /**
     * Yes option.
     */
    Yes,

    /**
     * No option.
     */
    No,

    /**
     * Required option.
     */
    Required,

    /**
     * Not required option.
     */
    NotRequired,

    /**
     * Any option.
     */
    Any;

    @Override
    public String toString() {
        return super.toString().replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
    }

}

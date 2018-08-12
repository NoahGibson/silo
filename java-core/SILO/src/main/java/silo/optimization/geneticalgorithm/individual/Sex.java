package silo.optimization.geneticalgorithm.individual;

import java.util.Objects;
import java.util.Random;

/**
 * Possible types for an individual's sex.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180721
 */
public class Sex {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The sex type.
     */
    private SexType type;


    /*
     * ENUM TYPE -------------------------------------------------------------------------------------------------------
     */

    /**
     * Enum type for sex.
     */
    public enum SexType {

        /**
         * Types.
         */
        FEMALE("FEMALE"), MALE("MALE");

        /**
         * String representation.
         */
        private String rep;

        /**
         * Default constructor.
         *
         * @param rep
         *      the string representation
         */
        SexType(String rep) {
            this.rep = rep;
        }

        @Override
        public final String toString() {
            return this.rep;
        }

    }

    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sex sex = (Sex) o;
        return type == sex.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "Sex{" +
                "type=" + type +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Default constructor.
     *
     * @param type
     *      the type of the sex
     */
    public Sex(SexType type) {
        this.type = type;
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Returns a new {@link Sex} object with a random sex (either MALE or FEMALE).
     *
     * @return a new {@code Sex} object with a random sex
     */
    public static final Sex random() {
        Random rand = new Random();
        if (rand.nextDouble() < 0.5) {
            return new Sex(SexType.FEMALE);
        } else {
            return new Sex(SexType.MALE);
        }
    }

    /**
     * Get the sex type of the sex.
     *
     * @return the sex type of {@code this}
     */
    public final Sex.SexType getSexType() {
        return this.type;
    }

}

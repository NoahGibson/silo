package silo.optimization.geneticalgorithm.individual;

import silo.optimization.geneticalgorithm.chromosome.Chromosome;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * An object representing a generic individual within a population of a genetic algorithm.
 * <p>
 * An individual is represented by the following attributes:
 * <ul>
 *     <li>chromosome - the {@link Chromosome} of the individual
 *     <li>sex - the {@link Sex} of the individual, either male or female
 *     <li>age - the number of generations that the individual has existed, beginning at 0
 * </ul>
 *
 * @param <T>
 *      the type of the genes of the individual's chromosome
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180721
 */
public class Individual<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The chromosome of the individual.
     */
    private Chromosome<T> chromosome;

    /**
     * The sex of the individual.
     */
    private Sex sex;

    /**
     * The age of the individual.
     */
    private int age;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_CHROMOSOME_MSG = "chromosome must not be null";

    private static final String NULL_GENERATOR_MSG = "geneGenerator must not be null";

    private static final String NULL_SEX_MSG = "sex must not be null";


    /*
     * PRIVATE METHODS -------------------------------------------------------------------------------------------------
     */

    /**
     * Creates a new chromosome for {@code this} using the given gene generator and the given chromosome size.
     *
     * @param chromosomeSize
     *      the size of the new chromosome
     *
     * @return a new chromosome, randomized
     */
    private Chromosome<T> createNewChromosome(Supplier<T> geneGenerator, int chromosomeSize) {
        return new Chromosome<>(geneGenerator, chromosomeSize);
    }


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual<?> that = (Individual<?>) o;
        return age == that.age &&
                Objects.equals(chromosome, that.chromosome) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(chromosome, sex, age);
    }

    @Override
    public String toString() {
        return "Individual{" +
                "chromosome=" + chromosome +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Constructs a new {@link Individual} with the given chromosome, a random sex, and an age of 0.
     *
     * @param chromosome
     *      the chromosome for the individual
     *
     * @throws IllegalArgumentException if the {@code chromosome} is null
     */
    public Individual(Chromosome<T> chromosome) throws IllegalArgumentException {
        if (chromosome == null) {
            throw new IllegalArgumentException(NULL_CHROMOSOME_MSG);
        } else {
            this.chromosome = chromosome;
            this.sex = Sex.random();
            this.age = 0;
        }
    }

    /**
     * Constructs a new {@link Individual} object with the given member values.
     *
     * @param chromosome
     *      the chromosome of the individual
     * @param sex
     *      the sex of the individual
     * @param age
     *      the age of the individual
     *
     * @throws IllegalArgumentException if either {@code chromosome} or {@code sex} is null
     */
    public Individual(Chromosome<T> chromosome, Sex sex, int age) throws IllegalArgumentException {
        if (chromosome == null) {
            throw new IllegalArgumentException(NULL_CHROMOSOME_MSG);
        } else if (sex == null) {
            throw new IllegalArgumentException(NULL_SEX_MSG);
        } else {
            this.chromosome = chromosome;
            this.sex = sex;
            this.age = age;
        }
    }

    /**
     * Constructs a new {@link Individual} object from the given gene generator {@code geneGenerator}, and randomizes
     * the chromosome of the individual with the given size {@code chromosomeSize} using the gene generator.
     * <p>
     * The sex of the individual is randomly chosen. The age of the individual is set to 0.
     *
     * @param geneGenerator
     *      a supplier function which outputs a random gene of type {@code T}
     * @param chromosomeSize
     *      the number of genes in the individual's chromosome
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public Individual(Supplier<T> geneGenerator, int chromosomeSize) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.chromosome = createNewChromosome(geneGenerator, chromosomeSize);
            this.sex = Sex.random();
            this.age = 0;
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Get the chromosome of {@code this}.
     *
     * @return the chromosome of the individual
     */
    public final Chromosome<T> getChromosome() {
        return chromosome;
    }

    /**
     * Set the chromosome of {@code this}.
     *
     * @param chromosome
     *      the new chromosome of the individual
     *
     * @throws IllegalArgumentException if the {@code chromosome} is null
     */
    public final void setChromosome(Chromosome<T> chromosome) throws IllegalArgumentException {
        if (chromosome != null) {
            this.chromosome = chromosome;
        } else {
            throw new IllegalArgumentException(NULL_CHROMOSOME_MSG);
        }
    }

    /**
     * Get the sex of {@code this}.
     *
     * @return the sex of the individual
     */
    public final Sex getSex() {
        return sex;
    }

    /**
     * Set the sex of {@code this}.
     *
     * @param sex
     *      the new sex of the individual
     *
     * @throws IllegalArgumentException if the {@code sex} is null
     */
    public final void setSex(Sex sex) throws IllegalArgumentException {
        if (sex != null) {
            this.sex = sex;
        } else {
            throw new IllegalArgumentException(NULL_SEX_MSG);
        }
    }

    /**
     * Get the age of {@code this}.
     *
     * @return the age of the individual
     */
    public final int getAge() {
        return age;
    }

    /**
     * Set the age of {@code this}.
     *
     * @param age
     *      the new age of the individual
     */
    public final void setAge(int age) {
        this.age = age;
    }

    /**
     * Adds 1 to the current age of the individual.
     */
    public final void ageStep() {
        this.age++;
    }

    /**
     * Adds the given number of steps to the age of the individual.
     *
     * @param steps
     *      the number of steps to add to the age of {@code this}
     */
    public final void ageStep(int steps) {
        this.age += steps;
    }

}

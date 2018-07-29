package org.teamgobbu.geneticalgorithm.population;

import org.teamgobbu.geneticalgorithm.individual.Individual;
import org.teamgobbu.geneticalgorithm.individual.IndividualFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Object to perform operations on a {@link Population}.
 *
 * @param <T>
 *      the type of the chromosomes of the individuals within the population
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180717
 */
public class PopulationFactory<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The individual factory used by {@code this}.
     */
    private IndividualFactory<T> individualFactory;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_GENERATOR_MSG = "geneGenerator must not be null";

    private static final String NULL_IND_FACT_MSG = "individualFactory must not be null";

    private static final String NULL_PARENTS_MSG = "parents must not be null";

    private static final String EVEN_PARENTS_MSG = "the size of parents must be even";


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PopulationFactory<?> that = (PopulationFactory<?>) o;
        return Objects.equals(individualFactory, that.individualFactory);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(individualFactory);
    }

    @Override
    public final String toString() {
        return "PopulationFactory{" +
                "individualFactory=" + individualFactory +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes a new {@link PopulationFactory} with a new {@link IndividualFactory} with the given gene generator.
     *
     * @param geneGenerator
     *      the gene generator for the individual factory of {@code this}
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public PopulationFactory(Supplier<T> geneGenerator) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.individualFactory = new IndividualFactory<>(geneGenerator);
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }

    /**
     * Initializes a new {@link PopulationFactory} with the given individual factory.
     *
     * @param individualFactory
     *      the individual factory to use for {@code this}
     *
     * @throws IllegalArgumentException if the {@code individualFactory} is null
     */
    public PopulationFactory(IndividualFactory<T> individualFactory) throws IllegalArgumentException {
        if (individualFactory != null) {
            this.individualFactory = individualFactory;
        } else {
            throw new IllegalArgumentException(NULL_IND_FACT_MSG);
        }
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Get the gene generator of the factory.
     *
     * @return the gene generator
     */
    public final Supplier<T> getGeneGenerator() {
        return individualFactory.getGeneGenerator();
    }

    /**
     * Set the gene generator of the factory.
     *
     * @param geneGenerator
     *      the new gene generator
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public final void setGeneGenerator(Supplier<T> geneGenerator) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.individualFactory.setGeneGenerator(geneGenerator);
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }

    /**
     * Get the individual factory of the factory.
     *
     * @return the individual factory of {@code this}
     */
    public final IndividualFactory<T> getIndividualFactory() {
        return individualFactory;
    }

    /**
     * Set the individual factory for the factory.
     *
     * @param individualFactory
     *      the new individual factory
     *
     * @throws IllegalArgumentException if the {@code individualFactory} is null
     */
    public final void setIndividualFactory(IndividualFactory<T> individualFactory) throws IllegalArgumentException {
        if (individualFactory != null) {
            this.individualFactory = individualFactory;
        } else {
            throw new IllegalArgumentException(NULL_IND_FACT_MSG);
        }
    }

    /**
     * Returns a new population with the given size, where each member has a chromosome size of {@code chromosomeSize},
     * and each member's chromosome is randomized.
     *
     * @param populationSize
     *      the size of the generated population
     * @param chromosomeSize
     *      the size of each member's chromosome
     *
     * @return a population of the given size with members with the given chromosome size
     */
    public final Population<T> generatePopulation(int populationSize, int chromosomeSize) {
        Population<T> population = new Population<>();
        for (int i = 0; i < populationSize; i++) {
            population.addMember(this.individualFactory.generateIndividual(chromosomeSize));
        }
        return population;
    }

    /**
     * Returns a new generation produced from the given parents. The individuals of the returned population are
     * generated using k-point crossover on mutually exclusive pairs of individuals in {@code parents}.
     * <p>
     * Each pair of parents will produce {@code numChildren} children.
     * <p>
     * The size of {@code parents} must be even.
     * <p>
     * For the best results, the order of the list of parents should be randomized.
     *
     * @param parents
     *      the parents of the returned generation
     * @param numChildren
     *      the number of children each pair of parents will produce
     * @param numCrossoverPoints
     *      the number of crossover points to use during k-point crossover
     * @param mutationRate
     *      the mutation rate used when producing the children
     *
     * @return a new population produced from the given parents and parameters
     *
     * @throws IllegalArgumentException if the {@code parents} is null, or if the size of {@code parents} is non-even
     */
    public final Population<T> produceNextGeneration(List<Individual<T>> parents,
                                                     int numChildren,
                                                     int numCrossoverPoints,
                                                     double mutationRate) throws IllegalArgumentException {
        if (parents == null) {
            throw new IllegalArgumentException(NULL_PARENTS_MSG);
        } else if (parents.size() % 2 != 0) {
            throw new IllegalArgumentException(EVEN_PARENTS_MSG);
        }
        Population<T> nextGeneration = new Population<>();
        for (int i = 0; i < parents.size(); i += 2) {
            nextGeneration.addMembers(this.individualFactory.produceChildren(
                    parents.get(i),
                    parents.get(i+1),
                    numChildren,
                    numCrossoverPoints,
                    mutationRate)
            );
        }
        return nextGeneration;
    }

}

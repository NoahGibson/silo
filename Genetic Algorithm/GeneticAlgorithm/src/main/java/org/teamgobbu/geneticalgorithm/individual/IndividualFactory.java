package org.teamgobbu.geneticalgorithm.individual;

import org.teamgobbu.geneticalgorithm.chromosome.ChromosomeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Object to perform operations on {@link Individual}s.
 *
 * @param <T>
 *      the type of the individual's chromosome genes
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180721
 */
public class IndividualFactory<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The chromosome factory used by {@code this}.
     */
    private ChromosomeFactory<T> chromosomeFactory;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_GENERATOR_MSG = "geneGenerator must not be null";

    private static final String NULL_INDIVIDUAL_MSG = "individuals must not be null";

    private static final String CHROMOSOME_LEN_MSG = "chromosomes must be the same size";

    private static final String NULL_FACTORY_MSG = "chromosomeFactory must not be null";


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndividualFactory<?> that = (IndividualFactory<?>) o;
        return Objects.equals(chromosomeFactory, that.chromosomeFactory);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(chromosomeFactory);
    }

    @Override
    public final String toString() {
        return "IndividualFactory{" +
                "chromosomeFactory=" + chromosomeFactory +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes a new {@link IndividualFactory} with a new {@link ChromosomeFactory} which uses the given gene
     * generator.
     *
     * @param geneGenerator
     *      the gene generator for the factory
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public IndividualFactory(Supplier<T> geneGenerator) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.chromosomeFactory = new ChromosomeFactory<>(geneGenerator);
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }

    /**
     * Initializes a new {@link IndividualFactory} with the given chromosome factory.
     *
     * @param chromosomeFactory
     *      the chromosome factory to be used by the factory
     *
     * @throws IllegalArgumentException if the {@code chromosomeFactory} is null
     */
    public IndividualFactory(ChromosomeFactory<T> chromosomeFactory) throws IllegalArgumentException {
        if (chromosomeFactory != null) {
            this.chromosomeFactory = chromosomeFactory;
        } else {
            throw new IllegalArgumentException(NULL_FACTORY_MSG);
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
        return chromosomeFactory.getGeneGenerator();
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
            this.chromosomeFactory.setGeneGenerator(geneGenerator);
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }

    /**
     * Get the chromosome factory of the factory.
     *
     * @return the chromosome factory used by {@ocde this}
     */
    public final ChromosomeFactory<T> getChromosomeFactory() {
        return chromosomeFactory;
    }

    /**
     * Set the chromosome factory used by the factory.
     *
     * @param chromosomeFactory
     *      the new chromosome factory
     *
     * @throws IllegalArgumentException if the {@code chromosomeFactory} is null
     */
    public final void setChromosomeFactory(ChromosomeFactory<T> chromosomeFactory) throws IllegalArgumentException {
        if (chromosomeFactory != null) {
            this.chromosomeFactory = chromosomeFactory;
        } else {
            throw new IllegalArgumentException(NULL_FACTORY_MSG);
        }
    }

    /**
     * Returns a new {@link Individual} with a randomly generated chromosome of the given length, a random sex, and an
     * age of 0.
     *
     * @param chromosomeSize
     *      the size of the individual's chromosome
     *
     * @return a new individual with a random chromosome
     */
    public final Individual<T> generateIndividual(int chromosomeSize) {
        return new Individual<>(this.chromosomeFactory.getGeneGenerator(), chromosomeSize);
    }

    /**
     * Mutates the given individual with the given mutation rate.
     *
     * @param individual
     *      the individual to mutate
     * @param mutationRate
     *      the mutation rate to use
     *
     * @throws IllegalArgumentException if the {@code individual} is null
     */
    public final void mutateIndividual(Individual<T> individual, double mutationRate) throws IllegalArgumentException {
        if (individual != null) {
            this.chromosomeFactory.mutateChromosome(individual.getChromosome(), mutationRate);
        } else {
            throw new IllegalArgumentException(NULL_INDIVIDUAL_MSG);
        }
    }

    /**
     * Creates a new {@link Individual} from the two given parents using K-Point Crossover with the given number of
     * points. The new individual will have a random sex and age of 0.
     * <p>
     * Mutation occurs with the given mutation rate.
     *
     * @param parent1
     *      the first parent of the child
     * @param parent2
     *      the second parent of the child
     * @param points
     *      the number of points to use for crossover
     * @param mutationRate
     *      the mutation rate of the child
     *
     * @return a new individual birthed from the two parents, with a random sex and age of 0, mutated
     *
     * @throws IllegalArgumentException if either parent is null, or if the size of the parents' chromosomes do not
     *      match
     */
    public final Individual<T> produceChild(Individual<T> parent1,
                                            Individual<T> parent2,
                                            int points,
                                            double mutationRate) throws IllegalArgumentException {
        if (parent1 == null || parent2 == null) {
            throw new IllegalArgumentException(NULL_INDIVIDUAL_MSG);
        } else if (parent1.getChromosome().size() != parent2.getChromosome().size()) {
            throw new IllegalArgumentException(CHROMOSOME_LEN_MSG);
        }
        Individual<T> child = new Individual<>(this.chromosomeFactory.kPointCrossover(parent1.getChromosome(), parent2.getChromosome(), points));
        mutateIndividual(child, mutationRate);
        return child;
    }

    /**
     * Produces the specified number of children from the two given parents using K-Point Crossover with the given
     * number of points. The new individuals will have a random sex and age of 0.
     * <p>
     * Mutation occurs with the given mutation rate.
     *
     * @param parent1
     *      the first parent of the children
     * @param parent2
     *      the second parent of the children
     * @param numChildren
     *      the number of children to produce
     * @param points
     *      the number of points to use for crossover
     * @param mutationRate
     *      the mutation rate of the children
     *
     * @return a list of new individuals birthed from the two parents, with random sexes and ages of 0, mutated
     *
     * @throws IllegalArgumentException if either parent is null, or if the size of the parents' chromosomes do not
     *      match
     */
    public final List<Individual<T>> produceChildren(Individual<T> parent1,
                                                            Individual<T> parent2,
                                                            int numChildren,
                                                            int points,
                                                            double mutationRate) throws IllegalArgumentException {
        if (parent1 == null || parent2 == null) {
            throw new IllegalArgumentException(NULL_INDIVIDUAL_MSG);
        } else if (parent1.getChromosome().size() != parent2.getChromosome().size()) {
            throw new IllegalArgumentException(CHROMOSOME_LEN_MSG);
        }
        List<Individual<T>> children = new ArrayList<>();
        for (int i = 0; i < numChildren; i++) {
            children.add(produceChild(parent1, parent2, points, mutationRate));
        }
        return children;
    }

}

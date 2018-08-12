package silo.optimization.geneticalgorithm.chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Object to store a gene generator function and perform operations on {@link Chromosome}s.
 *
 * @param <T>
 *      the type of the genes for the chromosomes
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180721
 */
public class ChromosomeFactory<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The gene generator function used by this class.
     */
    private Supplier<T> geneGenerator;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_GENERATOR_MSG = "geneGenerator must not be null";

    private static final String NULL_CHROMOSOME_MSG = "chromosome must not be null";

    private static final String CHROMOSOME_LEN_MSG = "chromosomes must be the same size";


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChromosomeFactory<?> that = (ChromosomeFactory<?>) o;
        return Objects.equals(geneGenerator, that.geneGenerator);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(geneGenerator);
    }

    @Override
    public final String toString() {
        return "ChromosomeFactory{" +
                "geneGenerator=" + geneGenerator +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes a new {@link ChromosomeFactory} with the given gene generator.
     *
     * @param geneGenerator
     *      the gene generator for the factory
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public ChromosomeFactory(Supplier<T> geneGenerator) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.geneGenerator = geneGenerator;
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
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
        return geneGenerator;
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
            this.geneGenerator = geneGenerator;
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }

    /**
     * Returns a new chromosome generated using the gene generator of {@code this}.
     *
     * @param chromosomeSize
     *      the size of the returned chromosome
     *
     * @return a new, randomized chromosome
     */
    public final Chromosome<T> generateChromosome(int chromosomeSize) {
        return new Chromosome<>(this.geneGenerator, chromosomeSize);
    }

    /**
     * Mutates the given chromosome using the gene generator of {@code this} and the given mutation rate.
     *
     * @param chromosome
     *      the chromosome to mutate
     * @param mutationRate
     *      the mutation rate to use
     *
     * @throws IllegalArgumentException if the {@code chromosome} is null
     */
    public final void mutateChromosome(Chromosome<T> chromosome, double mutationRate) throws IllegalArgumentException{
        if (chromosome == null) {
            throw new IllegalArgumentException(NULL_CHROMOSOME_MSG);
        } else {
            Random rand = new Random();
            for (int i = 0; i < chromosome.size(); i++) {
                if (rand.nextDouble() < mutationRate) {
                    chromosome.setGene(i, geneGenerator.get());
                }
            }
        }
    }

    /**
     *
     * Performs K-Point Crossover on the two given chromosomes, using the number of points given, to produce a single
     * child chromosome.
     *
     * @param chromosome1
     *      the first parent chromosome
     * @param chromosome2
     *      the second parent chromosome
     * @param points
     *      the number of points to use in the k-point crossover
     *
     * @return a child chromosome produced from performing k-point crossover on the two parents
     *
     * @throws IllegalArgumentException if the two given chromosomes are of unequal size, or if either chromosome is
     *      null
     */
    public final Chromosome<T> kPointCrossover(Chromosome<T> chromosome1, Chromosome<T> chromosome2, int points) throws IllegalArgumentException {
        if (chromosome1 == null || chromosome2 == null) {
            throw new IllegalArgumentException(NULL_CHROMOSOME_MSG);
        } else if (chromosome1.size() != chromosome2.size()) {
            throw new IllegalArgumentException(CHROMOSOME_LEN_MSG);
        }
        Chromosome<T> temp1 = new Chromosome<>(chromosome1);
        Chromosome<T> temp2 = new Chromosome<>(chromosome2);
        Random rand = new Random();
        for (int i = 0; i < points; i++) {
            int crossoverPoint = rand.nextInt(temp1.size());
            List<T> tempList1 = new ArrayList<>(temp1.getGenes(0, crossoverPoint));
            tempList1.addAll(temp2.getGenes(crossoverPoint, temp2.size()));
            List<T> tempList2 = new ArrayList<>(temp2.getGenes(0, crossoverPoint));
            tempList2.addAll(temp1.getGenes(crossoverPoint, temp1.size()));
            temp1 = new Chromosome<>(tempList1);
            temp2 = new Chromosome<>(tempList2);
        }
        Chromosome<T> child;
        if (rand.nextDouble() < 0.5) {
            child = new Chromosome<>(temp1);
        } else {
            child = new Chromosome<>(temp2);
        }
        return child;
    }

}

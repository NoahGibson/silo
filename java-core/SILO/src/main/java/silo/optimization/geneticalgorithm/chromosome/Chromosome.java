package silo.optimization.geneticalgorithm.chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Object representing a generic chromosome.
 * <p>
 * Each chromosome is represented by a list of genes.
 *
 * @param <T>
 *      the type of each gene of the chromosome
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180721
 */
public class Chromosome<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The list representation of the chromosome.
     */
    private List<T> genes;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_GENES_MSG = "genes must not be null";

    private static final String NULL_GENERATOR_MSG = "geneGenerator must not be null";

    private static final String NULL_CHROMOSOME_MSG = "chromosome must not be null";


    /*
     * PRIVATE METHODS -------------------------------------------------------------------------------------------------
     */

    /**
     * Creates a new list of genes for {@code this} which are randomly produced from the given gene generator.
     *
     * @param geneGenerator
     *      the gene generator to use
     * @param chromosomeSize
     *      the number of genes in the new chromosome
     */
    private void generateRandomGenes(Supplier<T> geneGenerator, int chromosomeSize) {
        this.genes = new ArrayList<>();
        for (int i = 0; i < chromosomeSize; i++) {
            this.genes.add(geneGenerator.get());
        }
    }


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome<?> that = (Chromosome<?>) o;
        return Objects.equals(genes, that.genes);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(genes);
    }

    @Override
    public final String toString() {
        return "Chromosome{" +
                "genes=" + genes +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Constructs a new {@link Chromosome} object from the given chromosome.
     * <p>
     * The genes of the new chromosome will be copied from the given chromosome.
     *
     * @param chromosome
     *      the chromosome to copy from
     *
     * @throws IllegalArgumentException if the {@code chromosome} is null
     */
    public Chromosome(Chromosome<T> chromosome) throws IllegalArgumentException {
        if (chromosome != null) {
            this.genes = new ArrayList<>(chromosome.genes);
        } else {
            throw new IllegalArgumentException(NULL_CHROMOSOME_MSG);
        }
    }

    /**
     * Constructs a new {@link Chromosome} object with the given genes.
     *
     * @param genes
     *      the genes of the chromosome
     *
     * @throws IllegalArgumentException if the {@code genes} is null
     */
    public Chromosome(List<T> genes) throws IllegalArgumentException {
        if (genes != null ) {
            this.genes = new ArrayList<>(genes);
        } else {
            throw new IllegalArgumentException(NULL_GENES_MSG);
        }
    }

    /**
     * Constructs a new {@link Chromosome} object using the given gene generator {@code geneGenerator}, and randomizes
     * the chromosome with the given size {@code chromosomeSize} using the gene generator.
     *
     * @param geneGenerator
     *      a supplier function which outputs a random gene of type {@code T}
     * @param chromosomeSize
     *      the number of genes in the chromosome
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public Chromosome(Supplier<T> geneGenerator, int chromosomeSize) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.genes = new ArrayList<>();
            this.generateRandomGenes(geneGenerator, chromosomeSize);
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Get the genes of {@code this}.
     *
     * @return the genes of the chromosome
     */
    public final List<T> getGenes() {
        return genes;
    }

    /**
     * Gets a the genes of the chromosome within the range [{@code fromIndex}, {@code toIndex}).
     *
     * @param fromIndex
     *      the low point of the genes to return, inclusive
     * @param toIndex
     *      the high point of the genes to return, exclusive
     *
     * @return the genes between {@code fromIndex} (inclusive) and {@code toIndex} (exclusive)
     */
    public final List<T> getGenes(int fromIndex, int toIndex) {
        return genes.subList(fromIndex, toIndex);
    }

    /**
     * Set the genes of {@code this}.
     *
     * @throws IllegalArgumentException if the {@code genes} is null
     */
    public final void setGenes(List<T> genes) throws IllegalArgumentException {
        if (genes != null) {
            this.genes = genes;
        } else {
            throw new IllegalArgumentException(NULL_GENES_MSG);
        }
    }

    /**
     * Returns the gene at the specified index {@code index}.
     *
     * @param index
     *      the index of the desired gene
     *
     * @return the gene at index {@code index}
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public final T getGene(int index) throws IndexOutOfBoundsException {
        return this.genes.get(index);
    }

    /**
     * Sets the gene at the specified index {@code index} to the specified value {@code newGene}.
     *
     * @param index
     *      the index of the gene to be replaced
     * @param newGene
     *      the new value of the gene
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public final void setGene(int index, T newGene) throws IndexOutOfBoundsException {
        this.genes.set(index, newGene);
    }

    /**
     * Returns the size of this chromosome.
     *
     * @return the size of {@code this}
     */
    public final int size() {
        return this.genes.size();
    }

}

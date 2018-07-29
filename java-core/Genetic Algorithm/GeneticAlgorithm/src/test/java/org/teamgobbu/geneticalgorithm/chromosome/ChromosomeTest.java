package org.teamgobbu.geneticalgorithm.chromosome;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Test cases for the {@link Chromosome} class.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180715
 */
public class ChromosomeTest {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * A gene generator function to use in the test cases.
     */
    private Supplier<Integer> geneGenerator = () -> {
        Random rand = new Random();
        if (rand.nextDouble() < 0.5) {
            return 0;
        } else {
            return 1;
        }
    };

    /*
     * TEST CASES ------------------------------------------------------------------------------------------------------
     */

    /**
     * Test the list constructor of a Chromosome.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the value of the new chromosome genes equals the given list of genes
     * </ul>
     */
    @Test
    public void listConstructorTest() {
        List<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(2);
        genes.add(3);
        Chromosome<Integer> chromosome = new Chromosome<>(genes);
        Assert.assertEquals(genes, chromosome.getGenes());
    }

    /**
     * Test the chromosome constructor of a Chromosome.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the new chromosome is equal in value to the original chromosome.
     * </ul>
     */
    @Test
    public void chromosomeConstructorTest() {
        List<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(2);
        genes.add(3);
        Chromosome<Integer> original = new Chromosome<>(genes);
        Chromosome<Integer> chromosome = new Chromosome<>(original);
        Assert.assertEquals(original, chromosome);
    }

    /**
     * Test the gene generator constructor of a Chromosome.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the new chromosome size matches the given size, and that each gene is one of the possible values of
     *          the gene generator (0 or 1)
     *     <li>that passing in a null gene generator throws an IllegalArgumentException
     * </ul>
     */
    @Test
    public void geneGeneratorConstructorTest() {
        // Constructor test
        int chromosomeSize = 10;
        Chromosome<Integer> chromosome = new Chromosome<>(this.geneGenerator, chromosomeSize);
        Assert.assertEquals(chromosomeSize, chromosome.size());
        for (int i = 0; i < chromosome.size(); i++) {
            Assert.assertTrue(chromosome.getGene(i).equals(0) || chromosome.getGene(i).equals(1));
        }

        // Null test
        boolean exceptionThrown = false;
        try{
            new Chromosome<>(null, chromosomeSize);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

}

package silo.optimization.geneticalgorithm.chromosome;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Test cases for the {@link ChromosomeFactory} class.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180722
 */
public class ChromosomeFactoryTest {

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
     * Test the gene generator constructor of a ChromosomeFactory.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the gene generator argument equals the new factory's generator
     *     <li>that passing in null throws an IllegalArgumentException
     * </ul>
     */
    @Test
    public void generatorConstructorTest() {
        // Constructor test
        ChromosomeFactory<Integer> cf = new ChromosomeFactory<>(this.geneGenerator);
        Assert.assertEquals(this.geneGenerator, cf.getGeneGenerator());

        // Null test
        boolean exceptionThrown = false;
        try{
            new ChromosomeFactory<>(null);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

    /**
     * Test the generateChromosome method of the factory.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the new chromosome size matches the given size, and that each gene is one of the possible values of
     *          the factory's gene generator (0 or 1)
     * </ul>
     */
    @Test
    public void generateChromosomeTest() {
        int chromosomeSize = 10;
        ChromosomeFactory<Integer> cf = new ChromosomeFactory<>(this.geneGenerator);
        Chromosome<Integer> chromosome = cf.generateChromosome(chromosomeSize);
        Assert.assertEquals(chromosomeSize, chromosome.size());
        for (int i = 0; i < chromosome.size(); i++) {
            Assert.assertTrue(chromosome.getGene(i).equals(0) || chromosome.getGene(i).equals(1));
        }
    }

    /**
     * Test the mutateChromosome method of the factory.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the chromosome size doesn't change
     *     <li>that passing in null throws an IllegalArgumentException
     * </ul>
     */
    @Test
    public void mutateChromosomeTest() {
        // Method test
        int chromosomeSize = 10;
        double mutationRate = 0.01;
        Chromosome<Integer> chromosome = new Chromosome<>(this.geneGenerator, chromosomeSize);
        ChromosomeFactory<Integer> cf = new ChromosomeFactory<>(this.geneGenerator);
        cf.mutateChromosome(chromosome, mutationRate);
        Assert.assertEquals(chromosomeSize, chromosome.size());

        // Null test
        boolean exceptionThrown = false;
        try{
            cf.mutateChromosome(null, mutationRate);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

    /**
     * Tests the kPointCrossover method of the factory.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the size of the returned chromosome is equal to its parents' sizes
     *     <li>that passing in parents of unequal size throws an IllegalArgumentException
     *     <li>that passing either parent as null throws an IllegalArgumentException
     * </ul>
     */
    @Test
    public void kPointCrossoverTest() {
        // Size test
        int chromosomeSize = 10;
        int crossoverPoints = 2;
        ChromosomeFactory<Integer> cf = new ChromosomeFactory<>(this.geneGenerator);
        Chromosome<Integer> parent1 = new Chromosome<>(this.geneGenerator, chromosomeSize);
        Chromosome<Integer> parent2 = new Chromosome<>(this.geneGenerator, chromosomeSize);
        Chromosome<Integer> child = cf.kPointCrossover(parent1, parent2, crossoverPoints);
        Assert.assertEquals(chromosomeSize, child.size());

        // Unequal size test
        parent2 = new Chromosome<>(this.geneGenerator, chromosomeSize - 1);
        boolean exceptionThrown = false;
        try{
            cf.kPointCrossover(parent1, parent2, crossoverPoints);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        // Null parents test
        parent1 = null;
        exceptionThrown = false;
        try{
            cf.kPointCrossover(parent1, parent2, crossoverPoints);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

}

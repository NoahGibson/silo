package silo.optimization.geneticalgorithm.individual;

import org.junit.Assert;
import org.junit.Test;
import silo.optimization.geneticalgorithm.chromosome.Chromosome;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Test cases for the {@link Individual} class.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180722
 */
public class IndividualTest {

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
     * Test the chromosome constructor of an individual.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the individual's chromosome equals the given chromosome
     *     <li>that the individual's age is 0
     *     <li>that the individual's sex is either MALE or FEMALE
     * </ul>
     */
    @Test
    public void chromosomeConstructorTest() {
        int chromosomeSize = 10;
        Chromosome<Integer> testChromosome = new Chromosome<>(this.geneGenerator, chromosomeSize);
        Individual<Integer> individual = new Individual<>(testChromosome);
        Assert.assertEquals(testChromosome, individual.getChromosome());
        Assert.assertEquals(0, individual.getAge());
        Assert.assertTrue(individual.getSex().getSexType().equals(Sex.SexType.MALE) ||
                individual.getSex().getSexType().equals(Sex.SexType.FEMALE));
    }

    /**
     * Test the all parameter constructor of an individual.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the individual's chromosome equals the given chromosome
     *     <li>that the individual's age equals the given age
     *     <li>that the individual's sex equals the given sex
     * </ul>
     */
    @Test
    public void allParamsConstructorTest() {
        int chromosomeSize = 10;
        int age = 10;
        Sex sex = Sex.random();
        Chromosome<Integer> testChromosome = new Chromosome<>(this.geneGenerator, chromosomeSize);
        Individual<Integer> individual = new Individual<>(testChromosome, sex, age);
        Assert.assertEquals(testChromosome, individual.getChromosome());
        Assert.assertEquals(age, individual.getAge());
        Assert.assertEquals(sex, individual.getSex());
    }

    /**
     * Test the gene generator constructor of an individual.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the size of the individual's chromosome equals the given size
     *     <li>that the age of the individual is 0
     *     <li>that the sex of the individual is either MALE or FEMALE
     * </ul>
     */
    @Test
    public void geneGeneratorConstructorTest() {
        int chromosomeSize = 10;
        Individual<Integer> individual = new Individual<>(this.geneGenerator, chromosomeSize);
        Assert.assertEquals(chromosomeSize, individual.getChromosome().size());
        Assert.assertEquals(0, individual.getAge());
        Assert.assertTrue(individual.getSex().getSexType().equals(Sex.SexType.MALE) ||
                individual.getSex().getSexType().equals(Sex.SexType.FEMALE));
    }

    /**
     * Test the single age step method of an individual.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the new age of the individual is 1 (0 + 1)
     * </ul>
     */
    @Test
    public void ageStepByOneTest() {
        int chromosomeSize = 10;
        Individual<Integer> individual = new Individual<>(this.geneGenerator, chromosomeSize);
        individual.ageStep();
        Assert.assertEquals(1, individual.getAge());
    }

    /**
     * Test the amount age step method of an individual.
     * <p>
     * Verifies:
     * <ul>
     *     <li>that the new age of the individual is 5 (0 + 5)
     * </ul>
     */
    @Test
    public void ageStepByAmountTest() {
        int chromosomeSize = 10;
        int steps = 5;
        Individual<Integer> individual = new Individual<>(this.geneGenerator, chromosomeSize);
        individual.ageStep(steps);
        Assert.assertEquals(5, individual.getAge());
    }

}

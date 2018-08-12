package silo.optimization.geneticalgorithm.example;

import silo.optimization.geneticalgorithm.algorithm.GeneticAlgorithm;
import silo.optimization.geneticalgorithm.chromosome.Chromosome;
import silo.optimization.geneticalgorithm.individual.Individual;
import silo.optimization.geneticalgorithm.util.Results;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An example using a {@link GeneticAlgorithm} to solve a knapsack problem.
 * <p>
 * For this problem:
 * <ul>
 *     <li>the max weight the knapsack can carry is 15
 *     <li>each item can be used 0 or more times
 *     <li>the possible items are:
 *     <pre>
 *          | item | value | weight |
 *          |------|-------|--------|
 *          | A    | 4     | 12     |
 *          | B    | 2     | 1      |
 *          | C    | 10    | 4      |
 *          | D    | 1     | 1      |
 *          | E    | 2     | 2      |
 *     </pre>
 * </ul>
 * This example isn't the best, but with finer tuning of the fitness function, algorithm parameters, and convergence
 * criteria it should hopefully work better.
 */
public class KnapsackExample {

    /**
     * Main method.
     */
    public static void main(String[] args) {

        /*
         * Set of all possible items.
         */
        List<String> items = new ArrayList<>();
        items.add("A");
        items.add("B");
        items.add("C");
        items.add("D");
        items.add("E");
        items.add("NONE"); // place holder for a blank item

        /*
         * Map of the items and their values.
         */
        Map<String, Integer> itemValues = new HashMap<>();
        itemValues.put("A", 4);
        itemValues.put("B", 2);
        itemValues.put("C", 10);
        itemValues.put("D", 1);
        itemValues.put("E", 2);
        itemValues.put("NONE", 0);

        /*
         * Map of the items and their weights.
         */
        Map<String, Integer> itemWeights = new HashMap<>();
        itemWeights.put("A", 12);
        itemWeights.put("B", 1);
        itemWeights.put("C", 4);
        itemWeights.put("D", 1);
        itemWeights.put("E", 2);
        itemWeights.put("NONE", 0);

        double totalWeight = 20;

        /*
         * The gene generator for the algorithm. Returns a random item.
         */
        Supplier<String> geneGenerator = () -> {
            Random rand = new Random();
            return items.get(rand.nextInt(items.size()));
        };

        /*
         * Fitness function for the algorithm.
         */
        Function<Individual<String>, Double> fitnessFunction = (individual) -> {
            double value = 0;
            double weight = 0;
            double maxWeight = 15.0;
            Chromosome<String> chromosome = individual.getChromosome();
            for (int i = 0; i < chromosome.size(); i++) {
                value += itemValues.get(chromosome.getGene(i));
                weight += itemWeights.get(chromosome.getGene(i));
            }
            double penalty = totalWeight * Math.abs(weight - 15);
            return value - penalty;
        };

        /*
         * The size of the individuals' chromosomes. In this case, it is 15, since it is the maximum number of items the
         * knapsack can fit, since (max capacity) / MIN(item weights) = 15.
         */
        int chromosomeSize = 15;

        /*
         * Initializing genetic algorithm with above parameters and setting other parameters.
         */
        GeneticAlgorithm<String> ga = new GeneticAlgorithm<>(fitnessFunction, geneGenerator, chromosomeSize);
        ga.setPopulationSize(20);
        ga.setNumChildren(2);
        ga.setMutationRate(0.03);
        ga.setNumCrossoverPoints(1);
        ga.setTournamentSize(10);
        ga.printEvery(20000);

        /*
         * Running the algorithm with convergence criteria and printing the results.
         */
        Results results = ga.run(36.0, 0.0, 0.0, 1000000);
        // the fitness threshold above is only 36 because the best solution is known to be 36
        System.out.println("Solution = " + results.getSolution().getChromosome().getGenes());
        System.out.println("Fitness = " + results.getFitness());
        System.out.println("Generation = " + results.getGeneration());
        System.out.println("Generations Ran = " + results.getGenerationsRan());

    }

}

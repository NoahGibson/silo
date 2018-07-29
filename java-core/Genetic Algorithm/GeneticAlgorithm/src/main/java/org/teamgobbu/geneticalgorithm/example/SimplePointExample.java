package org.teamgobbu.geneticalgorithm.example;

import org.teamgobbu.geneticalgorithm.algorithm.GeneticAlgorithm;
import org.teamgobbu.geneticalgorithm.individual.Individual;
import org.teamgobbu.geneticalgorithm.util.Results;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A simple example of a {@link GeneticAlgorithm} which tries to produce a point closest to the point (1.1, 2.2, 3.3)
 *
 * This example isn't the best, but with finer tuning of the fitness function, algorithm parameters, and convergence
 * criteria it should hopefully work better.
 */
public class SimplePointExample {

    /**
     * Converts a list of binary integers to its float value.
     *
     * @param list
     *      the binary number
     *
     * @return the binary number's float value
     */
    public static float convertBitsToFloat(List<Integer> list) {
        int bits = 0;
        for (int i : list) {
            bits = 10 * bits + i;
        }
        return Float.intBitsToFloat(bits);
    }

    /**
     * Converts a list of binary integers to its double value.
     *
     * @param list
     *      the binary number
     *
     * @return the binary number's double value
     */
    public static double convertBitsToDouble(List<Integer> list) {
        float floatValue = convertBitsToFloat(list);
        return new Double(Float.toString(floatValue));
    }

    /**
     * Returns a string representation of the solution point.
     *
     * @param solution
     *      the individual solution of the algorithm
     *
     * @return a string representation of the individual's point
     */
    public static String getSolutionPoint(Individual<Integer> solution) {
        int split1 = solution.getChromosome().size() / 3;
        int split2 = split1 * 2;
        double x = convertBitsToDouble(solution.getChromosome().getGenes(0, split1));
        double y = convertBitsToDouble(solution.getChromosome().getGenes(split1, split2));
        double z = convertBitsToDouble(solution.getChromosome().getGenes(split2, solution.getChromosome().size()));
        return "(" + x + "," + y + "," + z + ")";
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {

        /*
         * The gene generator for the GA. Produces either 1 or 0 with equal probability.
         */
        Supplier<Integer> geneGenerator = () -> {
            Random rand = new Random();
            return rand.nextDouble() > 0.5 ? 1 : 0;
        };

        /*
         * The fitness function of the GA. Converts the chromosome of integers to 3 double values, corresponding to the
         * x, y, and z values of the point respectively, and then scores based on the distance from the point
         * (1.1, 2.2, 3.3).
         */
        Function<Individual<Integer>, Double> fitnessFunction = (individual) -> {
            int chromosomeSize = individual.getChromosome().size();
            int split1 =  chromosomeSize / 3;
            int split2 = split1 * 2;
            double x = convertBitsToDouble(individual.getChromosome().getGenes(0, split1));
            double y = convertBitsToDouble(individual.getChromosome().getGenes(split1, split2));
            double z = convertBitsToDouble(individual.getChromosome().getGenes(split2, chromosomeSize));
            double distance = Math.sqrt(Math.pow(x - 1.1, 2) + Math.pow(y - 2.2, 2) + Math.pow(z - 3.3, 2));
            return -distance;
        };

        /*
         * The size of the chromosomes of the individuals. Is 96 here since each float value is 32 bits long, and there
         * are 3 of them.
         */
        int chromosomeSize = 96;

        /*
         * Initializing a genetic algorithm with the above parameters, and setting other algorithm parameters.
         */
        GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<>(fitnessFunction, geneGenerator, chromosomeSize);
        ga.setPopulationSize(20);
        ga.setNumChildren(2); // Note that numChildren must be a factor of populationSize
        ga.setNumCrossoverPoints(1);
        ga.setMutationRate(0.01);
        ga.setTournamentSize(3);
        ga.printEvery(20000);

        /*
         * Running the algorithm with convergence criteria and outputting the best point found.
         */
        Results results = ga.run(-0.5, 2.0, 0.5, 500000);
        System.out.println("Solution = " + getSolutionPoint(results.getSolution()));
        System.out.println("Fitness = " + results.getFitness());
        System.out.println("Generation = " + results.getGeneration());
        System.out.println("Generations Ran = " + results.getGenerationsRan());

    }

}

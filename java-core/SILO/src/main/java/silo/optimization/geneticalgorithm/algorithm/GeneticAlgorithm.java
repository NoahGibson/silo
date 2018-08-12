package silo.optimization.geneticalgorithm.algorithm;

import silo.optimization.geneticalgorithm.individual.Individual;
import silo.optimization.geneticalgorithm.population.Population;
import silo.optimization.geneticalgorithm.population.PopulationFactory;
import silo.optimization.geneticalgorithm.util.Results;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Object to execute a generic genetic algorithm, using tournament selection for selecting the individuals
 * to mate, and k-point crossover and mutation for mating individuals. This class is meant to be generic enough
 * to be useful across a range of genetic algorithm applications (though it is limited in that it can only find
 * solutions to problems where the solution can only be one size).
 * <p>
 * Each {@code GeneticAlgorithm} object is defined by the following values:
 * <ul>
 *     <li><b>Fitness Function</b> : the fitness function of the algorithm. The fitness function is used to assess
 *          the "correctness" of a given {@link Individual} to the problem the genetic algorithm is meant to solve. The
 *          fitness function should be implemented such that the better the individual is for the problem, the higher
 *          the fitness score.
 *
 *     <li><b>Gene Generator</b> : the function to generate a random gene of type {@code T}. More specifically, the
 *          gene generator function should return a random element of the set of all possible genes an individual can
 *          have.
 *
 *     <li><b>Chromosome Size</b> : the size of the chromosomes of the individuals of the genetic algorithm.
 *
 *     <li><b>Population Size</b> : the number of individuals in each generation of the algorithm, defaults to 20. The
 *          population size remains constant through each generation, that is, any generation <i>i</i> of the algorithm
 *          will contain this number of individuals. This value, combined with <i>Number of Children</i> determines the
 *          number of parents selected to generate the next generation. As a convention,
 *          <pre>
 *              (population size) >= 2, and
 *              (population size) % (number children) = 0.
 *          </pre>
 *          Ignoring this convention could lead to unpredictable results.
 *
 *     <li><b>Mutation Rate</b> : the probability that any one gene of a child individual will mutate, defaults to 0.01.
 *
 *     <li><b>Number of Children</b> : the number of children each pair of parents in a generation will have, defaults
 *          to 2. This value, combined with <i>Population Size</i> determines the number of parents selected to generate
 *          the next generation. As a convention,
 *          <pre>
 *              2 <= (number children) <= (population size), and
 *              (population size) % (number children) = 0
 *          </pre>
 *          Ignoring this convention could lead to unpredictable results.
 *
 *     <li><b>Number of Crossover Points</b> : the number of crossover points to use in k-point crossover, defaults to 1
 *
 *     <li><b>Tournament Size</b> : the size of each tournament to use during the selection of individuals to mate when
 *          creating the next generation, defaults to 2.
 * </ul>
 *
 * @param <T>
 *      the type of genes of the individuals within the genetic algorithm.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180722
 */
public class GeneticAlgorithm<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The gene generator for the algorithm.
     */
    private Supplier<T> geneGenerator;

    /**
     * The fitness function for the algorithm.
     */
    private Function<Individual<T>, Double> fitnessFunction;

    /**
     * The size of the chromosomes of the algorithm.
     */
    private int chromosomeSize;

    /**
     * The population size of each generation.
     */
    private int populationSize = 20;

    /**
     * The mutation rate for the algorithm.
     */
    private double mutationRate = 0.01;

    /**
     * The number of children each pair of parents should produce.
     */
    private int numChildren = 2;

    /**
     * The number of crossover points to use during mating.
     */
    private int numCrossoverPoints = 1;

    /**
     * The tournament size used for tournament selection.
     */
    private int tournamentSize = 2;

    /**
     * The rate of stats printing.
     */
    private int printEvery = 0;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_GENERATOR_MSG = "geneGenerator must not be null";

    private static final String NULL_FITNESS_MSG = "fitnessFunction must not be null";


    /*
     * EMBEDDED CLASSES ------------------------------------------------------------------------------------------------
     */

    /**
     * Embedded class container for holding an individual and its respective fitness value.
     */
    private class IndividualFitnessPair<E> {

        /**
         * The individual.
         */
        Individual<E> individual;

        /**
         * The individual's fitness.
         */
        double fitness;

        @Override
        public final String toString() {
            return "IndividualFitnessPair{" +
                    "individual=" + individual +
                    ", fitness=" + fitness +
                    '}';
        }

        /**
         * Default constructor.
         *
         * @param individual
         *      the individual for {@code this}
         * @param fitness
         *      the fitness for {@code this}
         */
        public IndividualFitnessPair(Individual<E> individual, double fitness) {
            this.individual = individual;
            this.fitness = fitness;
        }

    }

    /*
     * PRIVATE METHODS -------------------------------------------------------------------------------------------------
     */

    /**
     * Returns a list of {@link IndividualFitnessPair} for each individual in the given population, and computes that
     * individual's fitness.
     *
     * @param population
     *      the population to find the fitness values of
     *
     * @return a list of IndividualFitnessPairs for each individual in the population
     */
    private List<IndividualFitnessPair<T>> computePopulationFitnesses(Population<T> population) {
        List<IndividualFitnessPair<T>> fitnesses = new ArrayList<>();
        for (Individual<T> individual : population) {
            fitnesses.add(new IndividualFitnessPair<>(individual, this.fitnessFunction.apply(individual)));
        }
        return fitnesses;
    }

    /**
     * Returns the {@link IndividualFitnessPair} with the highest fitness in the given list.
     *
     * @param fitnesses
     *      the fitnesses to find the best of
     *
     * @return the best pair
     */
    private IndividualFitnessPair<T> findBestSolution(List<IndividualFitnessPair<T>> fitnesses) {
        IndividualFitnessPair<T> best = fitnesses.get(0);
        for (int i = 1; i < fitnesses.size(); i++) {
            if (fitnesses.get(i).fitness > best.fitness) {
                best = fitnesses.get(i);
            }
        }
        return best;
    }

    /**
     * Returns the average fitness score of the given list of individual fitness pairs.
     *
     * @param fitnesses
     *      the fitness pairs to compute the averages of
     *
     * @return the average of the fitnesses
     */
    private double computeAverageFitness(List<IndividualFitnessPair<T>> fitnesses) {
        double sum = 0;
        for (IndividualFitnessPair<T> fitness : fitnesses) {
            sum += fitness.fitness;
        }
        return sum / fitnesses.size();
    }

    /**
     * Uses tournament selection to select an individual from the given list of candidates.
     *
     * @param candidates
     *      the candidates to choose from
     * @param tournamentSize
     *      the size of the tournament
     *
     * @return the chosen individual
     */
    private IndividualFitnessPair<T> tournamentSelection(List<IndividualFitnessPair<T>> candidates, int tournamentSize) {
        List<IndividualFitnessPair<T>> tournament = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(candidates.get(rand.nextInt(candidates.size())));
        }
        return findBestSolution(tournament);
    }

    /**
     * Uses tournament selection to select the given number of parents from the population.
     *
     * @param fitnesses
     *      the {@link IndividualFitnessPair}s of the population
     * @param numParents
     *      the number of parents to choose
     *
     * @return the selected list of parents
     */
    private List<Individual<T>> selectParents(List<IndividualFitnessPair<T>> fitnesses, int numParents) {
        List<Individual<T>> parents = new ArrayList<>();
        List<IndividualFitnessPair<T>> candidates = new ArrayList<>(fitnesses);
        for (int i = 0; i < numParents; i++) {
            parents.add(tournamentSelection(candidates, this.tournamentSize).individual);
        }
        Collections.shuffle(parents);
        return parents;
    }

    /**
     * Prints stats about the current generation.
     *
     * @param fitnesses
     *      the generation fitnesses
     * @param generation
     *      the current generation number
     * @param bestSoFar
     *      the best solution so far
     */
    private void printStats(List<IndividualFitnessPair<T>> fitnesses, int generation, IndividualFitnessPair<T> bestSoFar) {
        IndividualFitnessPair<T> generationBest = findBestSolution(fitnesses);
        System.out.println("------------------ GENERATION " + generation + " ------------------");
        System.out.println(" Generation Best Solution   :  " + generationBest.individual);
        System.out.println(" Generation Best Fitness    :  " + generationBest.fitness);
        System.out.println(" Generation Average Fitness :  " + computeAverageFitness(fitnesses));
        System.out.println(" Best Solution so Far       :  " + bestSoFar.individual);
        System.out.println(" Best Fitness so Far        :  " + bestSoFar.fitness);
        System.out.println();
    }


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneticAlgorithm<?> that = (GeneticAlgorithm<?>) o;
        return chromosomeSize == that.chromosomeSize &&
                populationSize == that.populationSize &&
                Double.compare(that.mutationRate, mutationRate) == 0 &&
                numChildren == that.numChildren &&
                numCrossoverPoints == that.numCrossoverPoints &&
                tournamentSize == that.tournamentSize &&
                Objects.equals(geneGenerator, that.geneGenerator) &&
                Objects.equals(fitnessFunction, that.fitnessFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                geneGenerator,
                fitnessFunction,
                chromosomeSize,
                populationSize,
                mutationRate,
                numChildren,
                numCrossoverPoints,
                tournamentSize
        );
    }

    @Override
    public String toString() {
        return "GeneticAlgorithm{" +
                "geneGenerator=" + geneGenerator +
                ", fitnessFunction=" + fitnessFunction +
                ", chromosomeSize=" + chromosomeSize +
                ", populationSize=" + populationSize +
                ", mutationRate=" + mutationRate +
                ", numChildren=" + numChildren +
                ", numCrossoverPoints=" + numCrossoverPoints +
                ", tournamentSize=" + tournamentSize +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes a {@link GeneticAlgorithm} object with the given fitness function gene generator, and chromosome
     * size. All other parameters of the genetic algorithm will be set to their default.
     *
     * @param fitnessFunction
     *      the fitness function of the algorithm. The fitness function should take as input an {@link Individual}
     *      object that represents a possible solution to the problem, and should output a double value that represents
     *      the fitness of that individual to the problem (the greater the fitness value, the better the individual).
     *      This function is used to assess the fitness (or score) of a solution to the problem the genetic algorithm is
     *      attempting to solve.
     * @param geneGenerator
     *      a function to generate a random gene of type {@code T}. This function is used to generate the initial
     *      generation of the algorithm, and to mutate the child individuals of future generations. Essentially, this
     *      function should return a random element of the set of all possible genes of the individuals.
     * @param chromosomeSize
     *      the size of each individuals chromosome. That is, each individual's chromosome will have exactly this
     *      length.
     *
     * @throws IllegalArgumentException if the {@code fitnessFunction} or {@code geneGenerator} is null
     */
    public GeneticAlgorithm(Function<Individual<T>, Double> fitnessFunction, Supplier<T> geneGenerator, int chromosomeSize) throws IllegalArgumentException {
        if (fitnessFunction == null) {
            throw new IllegalArgumentException(NULL_FITNESS_MSG);
        } else if (geneGenerator == null) {
            throw  new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
        this.fitnessFunction = fitnessFunction;
        this.geneGenerator = geneGenerator;
        this.chromosomeSize = chromosomeSize;
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Get the gene generator of the algorithm.
     *
     * @return the gene generator of {@code this}
     */
    public Supplier<T> getGeneGenerator() {
        return geneGenerator;
    }

    /**
     * Set the gene generator for the algorithm.
     *
     * @param geneGenerator
     *      the new gene generator for {@code this}
     *
     * @throws IllegalArgumentException if the {@code geneGenerator} is null
     */
    public void setGeneGenerator(Supplier<T> geneGenerator) throws IllegalArgumentException {
        if (geneGenerator != null) {
            this.geneGenerator = geneGenerator;
        } else {
            throw new IllegalArgumentException(NULL_GENERATOR_MSG);
        }
    }

    /**
     * Get the fitness function of the algorithm.
     *
     * @return the fitness function of {@code this}
     */
    public Function<Individual<T>, Double> getFitnessFunction() {
        return fitnessFunction;
    }

    /**
     * Set the fitness function of the algorithm.
     *
     * @param fitnessFunction
     *      the new fitness function for {@code this}
     *
     * @throws IllegalArgumentException if the {@code fitnessFunction} is null
     */
    public void setFitnessFunction(Function<Individual<T>, Double> fitnessFunction) throws IllegalArgumentException {
        if (fitnessFunction != null) {
            this.fitnessFunction = fitnessFunction;
        } else {
            throw new IllegalArgumentException(NULL_FITNESS_MSG);
        }
    }

    /**
     * Get the chromosome size of the algorithm.
     *
     * @return the chromosome size of {@code this}
     */
    public int getChromosomeSize() {
        return chromosomeSize;
    }

    /**
     * Set the chromosome size of the algorithm.
     *
     * @param chromosomeSize
     *      the new chromosome size for {@code this}
     */
    public void setChromosomeSize(int chromosomeSize) {
        this.chromosomeSize = chromosomeSize;
    }

    /**
     * Get the population size of the algorithm.
     *
     * @return the population size of {@code this}
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * Set the population size of the algorithm. As a convention,
     * <pre>
     *     (population size) >= 2, and
     *     (population size) % (number children) = 0.
     * </pre>
     * Ignoring this convention could lead to unpredictable results.
     *
     * @param populationSize
     *      the new population size for {@code this}
     */
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    /**
     * Get the mutation rate of the algorithm.
     *
     * @return the mutation rate of {@code this}
     */
    public double getMutationRate() {
        return mutationRate;
    }

    /**
     * Set the mutation rate of the algorithm.
     *
     * @param mutationRate
     *      the new mutation rate for {@code this}
     */
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    /**
     * Get the number of children of the algorithm.
     *
     * @return the number of children of {@code this}
     */
    public int getNumChildren() {
        return numChildren;
    }

    /**
     * Set the number of children of the algorithm. As a convention,
     * <pre>
     *     2 <= (number children) <= (population size), and
     *     (population size) % (number children) = 0
     * </pre>
     * Ignoring this convention could lead to unpredictable results.
     *
     * @param numChildren
     *      the new number of children for {@code this}
     */
    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    /**
     * Get the number of crossover points of the algorithm.
     *
     * @return the number of crossover points of {@code this}.
     */
    public int getNumCrossoverPoints() {
        return numCrossoverPoints;
    }

    /**
     * Set the number of crossover points of the algorithm.
     *
     * @param numCrossoverPoints
     *      the new number of crossover points for {@code this}
     */
    public void setNumCrossoverPoints(int numCrossoverPoints) {
        this.numCrossoverPoints = numCrossoverPoints;
    }

    /**
     * Get the tournament size of the algorithm.
     *
     * @return the tournament size of {@code this}
     */
    public int getTournamentSize() {
        return tournamentSize;
    }

    /**
     * Set the tournament size of the algorithm.
     *
     * @param tournamentSize
     *      the new tournament size for {@code this}
     */
    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    /**
     * Set the number of generations to skip between printing generation stats. 0 signifies no printing.
     *
     * @param printEvery
     *      the number of generations to skip between printing
     */
    public void printEvery(int printEvery) {
        this.printEvery = printEvery;
    }

    /**
     * Runs the genetic algorithm for the given number of generations, using the current parameter settings.
     *
     * @param numGenerations
     *      the number of generations to run
     *
     * @return a {@link Results} object holding information on the algorithm's run. The generation of the results will
     *      be where the first occurrence of an Individual with the highest fitness was.
     */
    public Results run(int numGenerations) {

        // Initializing a population factory to use for the run
        PopulationFactory<T> pf = new PopulationFactory<>(this.geneGenerator);

        // Finding number of parents for each generation
        int numParents = (this.populationSize / this.numChildren) * 2;

        // Generating the first population and computing the individuals' fitnesses
        Population<T> population = pf.generatePopulation(this.populationSize, this.chromosomeSize);
        List<IndividualFitnessPair<T>> fitnesses = computePopulationFitnesses(population);

        // Initializing the best solution found so far
        IndividualFitnessPair<T> bestSoFar = findBestSolution(fitnesses);
        int generationOfBest = 1;

        // Iterating through each generation
        int generation;
        for (generation = 1; generation < numGenerations; generation++) {
            if (this.printEvery != 0 && generation % this.printEvery == 0) {
                printStats(fitnesses, generation, bestSoFar);
            }
            List<Individual<T>> parents = selectParents(fitnesses, numParents);
            population = pf.produceNextGeneration(parents, this.numChildren, this.numCrossoverPoints, this.mutationRate);
            fitnesses = computePopulationFitnesses(population);
            IndividualFitnessPair<T> bestIndividual = findBestSolution(fitnesses);
            if (bestIndividual.fitness > bestSoFar.fitness) {
                bestSoFar = bestIndividual;
                generationOfBest = generation;
            }
        }

        // Returning results
        return new Results(bestSoFar.individual, bestSoFar.fitness, generationOfBest, generation, null);

    }

    /**
     * Runs the genetic algorithm until the given convergence criteria is met, using the current parameter settings.
     *
     * @param fitnessThreshold
     *      the fitness of the best solution of the current generation must be equal to or above this value before
     *      stopping
     * @param changeThreshold
     *      the difference between the fitness of the best solution of the current generation and the fitness of the
     *      best solution so far must be below this value before stopping
     * @param percentAverageThreshold
     *      the difference between the fitness of the best solution of the current generation and the average fitness
     *      of the current generation must be less than the product of the average fitness and this value before
     *      stopping
     * @param maxGenerations
     *      the maximum number of generations to run the algorithm if the other convergence criteria is not met
     *
     * @return a {@link Results} object holding information on the algorithm's run. The generation of the results will
     *      be where the most recent occurrence of an Individual with the highest fitness was.
     */
    public Results run(double fitnessThreshold, double changeThreshold, double percentAverageThreshold, int maxGenerations) {

        // Initializing a population factory to use for the run
        PopulationFactory<T> pf = new PopulationFactory<>(this.geneGenerator);

        // Finding number of parents for each generation
        int numParents = (this.populationSize / this.numChildren) * 2;

        // Generating the first generation and computing the individuals' fitnesses
        Population<T> population = pf.generatePopulation(this.populationSize, this.chromosomeSize);
        List<IndividualFitnessPair<T>> fitnesses = computePopulationFitnesses(population);

        // Initializing the best solution found so far
        IndividualFitnessPair<T> bestSoFar = findBestSolution(fitnesses);
        int generationOfBest = 1;

        // Iterating through each generation
        int generation;
        for (generation = 1; generation < maxGenerations; generation++) {
            if (this.printEvery != 0 && generation % this.printEvery == 0) {
                printStats(fitnesses, generation, bestSoFar);
            }
            List<Individual<T>> parents = selectParents(fitnesses, numParents);
            population = pf.produceNextGeneration(parents, this.numChildren, this.numCrossoverPoints, this.mutationRate);
            fitnesses = computePopulationFitnesses(population);
            IndividualFitnessPair<T> bestIndividual = findBestSolution(fitnesses);

            // Checking convergence criteria
            double averageFitness = computeAverageFitness(fitnesses);
            if (bestIndividual.fitness >= fitnessThreshold &&
                    Math.abs(bestSoFar.fitness - bestIndividual.fitness) <= changeThreshold &&
                    Math.abs(bestIndividual.fitness - averageFitness) <= Math.abs(percentAverageThreshold * averageFitness)) {
                if (bestIndividual.fitness >= bestSoFar.fitness) {
                    bestSoFar = bestIndividual;
                    generationOfBest = generation;
                }
                break;
            }

            // Updating bestSoFar
            if (bestIndividual.fitness >= bestSoFar.fitness) {
                bestSoFar = bestIndividual;
                generationOfBest = generation;
            }
        }

        // Returning results
        return new Results(bestSoFar.individual, bestSoFar.fitness, generationOfBest, generation, null);

    }

}

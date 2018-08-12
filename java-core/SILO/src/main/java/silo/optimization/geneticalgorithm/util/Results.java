package silo.optimization.geneticalgorithm.util;

import silo.optimization.geneticalgorithm.individual.Individual;

import java.time.Duration;
import java.util.Objects;

/**
 * Object to store the results of a run of a GeneticAlgorithm.
 * <p>
 * The statistics stored in this object are:
 * <ul>
 *     <li>solution - the best solution found from the run of the genetic algorithm
 *     <li>fitness - the fitness score of the best solution found
 *     <li>generation - the generation in which the best solution was found
 *     <li>generationsRan - the total number of generations ran during the genetic algorithm run
 *     <li>duration - the total time taken to run the genetic algorithm
 * </ul>
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180713
 */
public final class Results {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The best solution found.
     */
    private Individual solution;

    /**
     * The fitness of the best solution.
     */
    private double fitness;

    /**
     * The generation the best solution was found in.
     */
    private int generation;

    /**
     * The total number of generations ran before convergence.
     */
    private int generationsRan;

    /**
     * The total duration of the algorithm run.
     */
    private Duration duration;


    /*
     * PRIVATE METHODS -------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes the members of {@code this} to their default values.
     */
    private void init() {
        this.solution = null;
        this.fitness = 0;
        this.generation = 0;
        this.generationsRan = 0;
        this.duration = null;
    }


    /*
     * METHODS INHERITED FROM OBJECT -----------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Results results = (Results) o;
        return Double.compare(results.fitness, fitness) == 0 &&
                generation == results.generation &&
                generationsRan == results.generationsRan &&
                Objects.equals(solution, results.solution) &&
                Objects.equals(duration, results.duration);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(solution, fitness, generation, generationsRan, duration);
    }

    @Override
    public final String toString() {
        return "Results{" +
                "solution=" + solution +
                ", fitness=" + fitness +
                ", generation=" + generation +
                ", generationsRan=" + generationsRan +
                ", duration=" + duration +
                '}';
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Constructs a new {@link Results} object with default member values.
     * <p>
     * The default values for the members are as follows:
     * <ul>
     *     <li>solution = null
     *     <li>fitness = 0
     *     <li>generation = 0
     *     <li>generationsRan = 0
     *     <li>duration = null
     * </ul>
     */
    public Results() {
        init();
    }

    /**
     * Constructs a new {@link Results} object with the given member values.
     *
     * @param solution
     *      the best solution found during the algorithm run
     * @param fitness
     *      the fitness of the best solution found
     * @param generation
     *
     *      the generation in which the best solution was found
     * @param generationsRan
     *      the total number of generations ran during the run of the genetic algorithm
     * @param duration
     *      the total duration of the genetic algorithm run
     */
    public Results(Individual solution, double fitness, int generation, int generationsRan, Duration duration) {
        this.solution = solution;
        this.fitness = fitness;
        this.generation = generation;
        this.generationsRan = generationsRan;
        this.duration = duration;
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Get the best solution of the genetic algorithm.
     *
     * @return the best solution
     */
    public final Individual getSolution() {
        return this.solution;
    }

    /**
     * Get the fitness of the best solution of the genetic algorithm.
     *
     * @return the fitness of the best solution
     */
    public final double getFitness() {
        return this.fitness;
    }

    /**
     * Get the generation of the genetic algorithm in which the best solution was found.
     *
     * @return the generation of the best solution
     */
    public final int getGeneration() {
        return this.generation;
    }

    /**
     * Get the total number of generations ran during the genetic algorithm.
     *
     * @return the total number of generations ran
     */
    public final int getGenerationsRan() {
        return this.generationsRan;
    }

    /**
     * Get the total time the run of the genetic algorithm took before converging.
     *
     * @return the duration of the genetic algorithm run
     */
    public final Duration getDuration() {
        return this.duration;
    }

}

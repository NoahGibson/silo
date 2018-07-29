package org.teamgobbu.geneticalgorithm.population;

import org.teamgobbu.geneticalgorithm.individual.Individual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Class to represent a population of {@link Individual}s within a generation of a genetic algorithm.
 * <p>
 * A population is represented by:
 * <ul>
 *     <li>members - it's list of members
 * </ul>
 *
 * @param <T>
 *      the type of the genes of the individuals in the population
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180721
 */
public class Population<T> implements Iterable<Individual<T>> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The list of members of the population.
     */
    private List<Individual<T>> members;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_MEMBERS_MSG = "members must not be null";

    private static final String NULL_MEMBER_MSG = "member must not be null";

    private static final String NULL_POP_MSG = "population must not be null";


    /*
     * IMPLEMENTED METHODS ---------------------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Population<?> that = (Population<?>) o;
        return Objects.equals(members, that.members);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(members);
    }

    @Override
    public final String toString() {
        return "Population{" +
                "members=" + members +
                '}';
    }

    @Override
    public final Iterator<Individual<T>> iterator() {
        return members.iterator();
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes a new {@link Population} with no members.
     */
    public Population() {
        this.members = new ArrayList<>();
    }

    /**
     * Initializes a new {@link Population} with the given members.
     *
     * @param members
     *      the members of the population
     *
     * @throws IllegalArgumentException if the {@code members} is null
     */
    public Population(List<Individual<T>> members) throws IllegalArgumentException {
        if (members != null) {
            this.members = new ArrayList<>(members);
        } else {
            throw new IllegalArgumentException(NULL_MEMBERS_MSG);
        }
    }

    /**
     * Initializes a new {@link Population} with the same members copied from the given population.
     *
     * @param population
     *      the population to copy the members from
     *
     * @throws IllegalArgumentException if the {@code population} is null
     */
    public Population(Population<T> population) throws  IllegalArgumentException {
        if (population != null) {
            this.members = new ArrayList<>(population.members);
        } else {
            throw new IllegalArgumentException(NULL_POP_MSG);
        }
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Get the members of the population.
     *
     * @return the members of {@code this}
     */
    public final List<Individual<T>> getMembers() {
        return members;
    }

    /**
     * Set the members of the population.
     *
     * @param members
     *      the new members of {@code this}
     *
     * @throws IllegalArgumentException if the {@code members} is null
     */
    public final void setMembers(List<Individual<T>> members) throws IllegalArgumentException {
        if (members != null) {
            this.members = members;
        } else {
            throw new IllegalArgumentException(NULL_MEMBERS_MSG);
        }
    }

    /**
     * Adds the given member to the population.
     *
     * @param member
     *      the new member to add to {@code this}
     *
     * @throws IllegalArgumentException if the {@code member} is null
     */
    public final void addMember(Individual<T> member) throws IllegalArgumentException {
        if (member != null) {
            this.members.add(member);
        } else {
            throw new IllegalArgumentException(NULL_MEMBER_MSG);
        }
    }

    /**
     * Adds the given members to the population.
     *
     * @param members
     *      the new members to add to {@code this}
     *
     * @throws IllegalArgumentException if the {@code members} is null
     */
    public final void addMembers(List<Individual<T>> members) throws IllegalArgumentException {
        if (members != null) {
            this.members.addAll(members);
        } else {
            throw new IllegalArgumentException(NULL_MEMBERS_MSG);
        }
    }

    /**
     * Returns the size of the population.
     *
     * @return the size of {@code this}
     */
    public final int size() {
        return this.members.size();
    }

    /**
     * Ages each individual in the population by 1 step.
     */
    public final void age() {
        for (int i = 0; i < this.members.size(); i++) {
            this.members.get(i).ageStep();
        }
    }

    /**
     * Ages each individual in the population by the given number of steps.
     *
     * @param steps
     *      the number of steps to age each individual of {@code this} by
     */
    public final void age(int steps) {
        for (int i = 0; i < this.members.size(); i++) {
            this.members.get(i).ageStep(steps);
        }
    }

}

package imal;

import java.util.*;

/**
 * Created by tomas on 18.04.2017.
 */
public class ClonalgMethod {

    Antibody[] population;
    Antibody[] bestClones;

    private static int populationSize = 100;
    private static int bestSize = 10;
    private static int bestClonesSize = 6;
    private static int clonesSize = bestSize * bestClonesSize;
    private static int maxGenerations = 100;
    private static int mutationPercent = 25;

    private int antibodySize;
    private int antibodyValueSize;
    private CalcAffinity calcAffinity;

    ClonalgMethod(int antibodySize, int antibodyValueSize, CalcAffinity calcAffinity) {
        this.population = new Antibody[populationSize];
        //this.bestPopulation = new Antibody[bestSize];
        this.bestClones = new Antibody[clonesSize];

        this.antibodySize = antibodySize;
        this.antibodyValueSize = antibodyValueSize;
        this.calcAffinity = calcAffinity;

    }

    private void initialize() {
        for (int i = 0; i < this.populationSize; i++) {
            this.population[i] = new Antibody(this.antibodyValueSize, this.antibodySize);
        }
    }

    private void affinity(Antibody[] population) {
        for (int i = 0; i < this.populationSize; i++) {
            this.population[i].fittnes = this.calcAffinity.calcAffinity(this.population[i].getIndices());
        }
    }

    private void cloning(int count) {
        this.sortPopulation(this.population);

        for (int i = 0; i < bestSize; i++) {
            for (int j = 0; j < bestClonesSize; j++) {
                this.bestClones[i * bestClonesSize + j] = new Antibody(this.population[i]);
            }
        }

    }

    private void hypermutate(Antibody[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i].hypermutate((int)(this.antibodySize * (mutationPercent / 100.0)));
        }
    }

    private void sortPopulation(Antibody[] population) {
        Arrays.sort(population, (element1, element2) -> (element1.fittnes > element2.fittnes) ? -1 : 1);
    }

    private void apoptosis() {
        int offset = populationSize - clonesSize;
        for (int i = 0; i < clonesSize; i++) {
            this.population[i + offset] = this.bestClones[i];
        }
        this.sortPopulation(this.population);
    }


    public void run() {
        this.initialize();

        for (int i = 0; i < maxGenerations; i++) {
            this.affinity(this.population);
            this.cloning(bestSize);
            this.hypermutate(this.bestClones);
            this.affinity(this.bestClones);
            this.apoptosis();
            System.out.println("Genaration: " + i);
            System.out.println("Best f1: " + this.population[0].fittnes);
        }
        System.out.println("Best antibody: ");
        System.out.println(this.population[0]);
    }

}

interface CalcAffinity {
    double calcAffinity(Set<Integer> indices);
}

class Antibody {
    static Random randomGenerator = new Random();
    boolean[] values;
    double fittnes = 0.0;

    Antibody(int valueSize, int antibodySize) {
        if (antibodySize < valueSize) {
            throw new IllegalArgumentException("Value size must be lower than antibody size.");
        }

        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < valueSize) {
            Integer next = Antibody.randomGenerator.nextInt(antibodySize);
            generated.add(next);
        }

        this.values = new boolean[antibodySize];
        for (int i = 0; i < antibodySize; i++) {
            this.values[i] = generated.contains(i);
        }
    }

    Antibody(Antibody pattern) {
        this.values = new boolean[pattern.values.length];
        System.arraycopy(pattern.values, 0, this.values, 0, pattern.values.length);
        this.fittnes = pattern.fittnes;
    }

    public void hypermutate(int mutations) {
        int max = this.values.length;
        for (int i = 0; i < mutations; i++) {
            int first = randomGenerator.nextInt(max);
            int second = randomGenerator.nextInt(max);
            boolean temp = this.values[second];
            this.values[second] = this.values[first];
            this.values[first] = temp;
        }
    }

    public Set<Integer> getIndices() {
        Set<Integer> indices = new HashSet<>();
        for (int i = 0; i < this.values.length; i++) {
            if (this.values[i]) {
                indices.add(i);
            }
        }

        return indices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.values.length; i++) {
            sb.append((this.values[i] ? 1 : 0));
        }
        return sb.toString();
    }
}

package com.dungeoncode.ca.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.dungeoncode.ca.core.Constants.DESCRIPTORS_FILE_CONFS;
import static com.dungeoncode.ca.core.Constants.DESCRIPTORS_FILE_RULES;

/**
 * Repository for managing cellular automaton components including configurations,
 * rules, and their descriptors. Provides functionality to track and manage unique IDs.
 */
public class Repository<C extends Cell<S>, S extends CellState<?>> {

    private final List<Configuration<C, S>> configurations;
    private final List<Rule<C, S>> rules;
    private final Map<Integer, Descriptor> ruleDescriptors;
    private final Map<Integer, Descriptor> confDescriptors;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new Repository with empty lists for configurations, rules, and descriptors.
     */
    public Repository() {
        this.configurations = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.ruleDescriptors = new HashMap<>();
        this.confDescriptors = new HashMap<>();

        this.objectMapper = new ObjectMapper();
        loadDescriptors(DESCRIPTORS_FILE_RULES, ruleDescriptors);
        loadDescriptors(DESCRIPTORS_FILE_CONFS, confDescriptors);
    }

    /**
     * Loads descriptors from the JSON file.
     *
     * @throws IllegalStateException if duplicate descriptor IDs are found in the JSON file
     */
    private void loadDescriptors(String descriptorsFileRules, Map<Integer, Descriptor> descriptors) {
        try {
            File file = new File(descriptorsFileRules);
            if (file.exists()) {
                CollectionType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Descriptor.class);
                List<Descriptor> loadedDescriptors = objectMapper.readValue(file, listType);

                // Check for duplicate IDs
                Set<Integer> loadedIds = new HashSet<>();
                for (Descriptor descriptor : loadedDescriptors) {
                    if (!loadedIds.add(descriptor.getId())) {
                        throw new IllegalStateException(
                                String.format("Duplicate descriptor ID found: %d", descriptor.getId())
                        );
                    }
                }

                // If no duplicates, add all descriptors to the map
                for (Descriptor descriptor : loadedDescriptors) {
                    descriptors.put(descriptor.getId(), descriptor);
                }
            } else {
                System.out.println("No descriptors file found at: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error loading descriptors: " + e.getMessage());
        }
    }

    /**
     * Returns the list of configurations in the repository.
     *
     * @return list of configurations
     */
    public List<Configuration<C, S>> getConfigurations() {
        return new ArrayList<>(configurations);
    }

    /**
     * Returns the list of rules in the repository.
     *
     * @return list of rules
     */
    public List<Rule<C, S>> getRules() {
        return new ArrayList<>(rules);
    }

    /**
     * Returns a descriptor that matches the given rule ID.
     *
     * @param ruleId the ID of the rule to match
     * @return the matching descriptor, or null if not found
     */
    public Descriptor getDescriptorByRuleId(int ruleId) {
        return ruleDescriptors.get(ruleId);
    }

    /**
     * Returns a descriptor that matches the given configuration ID.
     *
     * @param confId the ID of the configuration to match
     * @return the matching descriptor, or null if not found
     */
    public Descriptor getDescriptorByConfId(int confId) {
        return confDescriptors.get(confId);
    }

    public void addConfiguration(Configuration<C, S> configuration) {
        this.configurations.add(configuration);
    }

    public void addRule(Rule<C, S> rule) {
        this.rules.add(rule);
    }

}
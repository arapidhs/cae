package com.dungeoncode.cae.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.dungeoncode.cae.core.Constants.*;

/**
 * Manages a repository of cellular automaton components, including configurations, rules, grid initializers, and their descriptors.
 * Provides methods to add components, retrieve them, and load descriptors from JSON files, ensuring unique IDs for all components.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class Repository<C extends Cell<S>, S extends CellState<?>> {

    /**
     * The list of configurations in the repository.
     */
    private final List<Configuration<C, S>> configurations;

    /**
     * The list of rules in the repository.
     */
    private final List<Rule<C, S>> rules;

    /**
     * The list of grid initializers in the repository.
     */
    private final List<GridInitializer<C, S>> initializers;

    /**
     * The map of rule descriptors, keyed by rule ID.
     */
    private final Map<Integer, Descriptor> ruleDescriptors;

    /**
     * The map of configuration descriptors, keyed by configuration ID.
     */
    private final Map<Integer, Descriptor> confDescriptors;

    /**
     * The map of initializer descriptors, keyed by initializer ID.
     */
    private final Map<Integer, Descriptor> initDescriptors;

    /**
     * The Jackson ObjectMapper for JSON serialization/deserialization.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new repository with empty lists for configurations, rules, and initializers,
     * and empty maps for descriptors. Loads descriptors from JSON files specified in {@link Constants}.
     */
    public Repository() {
        this.configurations = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.initializers = new ArrayList<>();
        this.ruleDescriptors = new HashMap<>();
        this.confDescriptors = new HashMap<>();
        this.initDescriptors = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        loadDescriptors(DESCRIPTORS_FILE_RULES, ruleDescriptors);
        loadDescriptors(DESCRIPTORS_FILE_CONFS, confDescriptors);
        loadDescriptors(DESCRIPTORS_FILE_INITS, initDescriptors);
    }

    /**
     * Loads descriptors from the specified JSON resource file into the given descriptor map.
     *
     * @param resourcePath the path to the JSON resource file containing descriptors
     * @param descriptors  the map to store loaded descriptors, keyed by ID
     * @throws IllegalStateException if duplicate descriptor IDs are found in the JSON file
     * @throws RuntimeException      if an I/O error occurs while loading the resource
     */
    public void loadDescriptors(String resourcePath, Map<Integer, Descriptor> descriptors) {
        try (InputStream in = Repository.class.getResourceAsStream(resourcePath)) {
            if (in != null) {
                CollectionType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Descriptor.class);
                List<Descriptor> loadedDescriptors = objectMapper.readValue(in, listType);

                // Check for duplicate IDs
                Set<Integer> loadedIds = new HashSet<>();
                for (Descriptor descriptor : loadedDescriptors) {
                    if (descriptor == null || !loadedIds.add(descriptor.getId())) {
                        throw new IllegalStateException(
                                String.format("Invalid or duplicate descriptor ID found: %d", descriptor != null ? descriptor.getId() : -1)
                        );
                    }
                }

                // Add descriptors to the map
                for (Descriptor descriptor : loadedDescriptors) {
                    descriptors.put(descriptor.getId(), descriptor);
                }
            } else {
                System.out.println("No descriptors resource found at: " + resourcePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading descriptors from " + resourcePath + ": " + e.getMessage(), e);
        }
    }

    /**
     * Returns a copy of the list of configurations in the repository.
     *
     * @return a new list of {@link Configuration} objects
     */
    public List<Configuration<C, S>> getConfigurations() {
        return new ArrayList<>(configurations);
    }

    /**
     * Returns a copy of the list of rules in the repository.
     *
     * @return a new list of {@link Rule} objects
     */
    public List<Rule<C, S>> getRules() {
        return new ArrayList<>(rules);
    }

    /**
     * Returns a copy of the list of grid initializers in the repository.
     *
     * @return a new list of {@link GridInitializer} objects
     */
    public List<GridInitializer<C, S>> getInitializers() {
        return new ArrayList<>(initializers);
    }

    /**
     * Returns the descriptor for the specified rule ID.
     *
     * @param ruleId the ID of the rule
     * @return the matching {@link Descriptor}, or null if not found
     */
    public Descriptor getDescriptorByRuleId(int ruleId) {
        return ruleDescriptors.get(ruleId);
    }

    /**
     * Returns the descriptor for the specified configuration ID.
     *
     * @param confId the ID of the configuration
     * @return the matching {@link Descriptor}, or null if not found
     */
    public Descriptor getDescriptorByConfId(int confId) {
        return confDescriptors.get(confId);
    }

    /**
     * Returns the descriptor for the specified initializer ID.
     *
     * @param initId the ID of the initializer
     * @return the matching {@link Descriptor}, or null if not found
     */
    public Descriptor getDescriptorByInitId(int initId) {
        return initDescriptors.get(initId);
    }

    /**
     * Adds a configuration to the repository.
     *
     * @param configuration the {@link Configuration} to add, must not be null
     * @throws NullPointerException if configuration is null
     */
    public void addConfiguration(Configuration<C, S> configuration) {
        Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.configurations.add(configuration);
    }

    /**
     * Adds a rule to the repository.
     *
     * @param rule the {@link Rule} to add, must not be null
     * @throws NullPointerException if rule is null
     */
    public void addRule(Rule<C, S> rule) {
        Objects.requireNonNull(rule, "Rule cannot be null");
        this.rules.add(rule);
    }

    /**
     * Adds a grid initializer to the repository.
     *
     * @param initializer the {@link GridInitializer} to add, must not be null
     * @throws NullPointerException if initializer is null
     */
    public void addInitializer(GridInitializer<C, S> initializer) {
        Objects.requireNonNull(initializer, "Initializer cannot be null");
        this.initializers.add(initializer);
    }

    public Collection<Descriptor> getConfDescriptors() {
        return confDescriptors.values();
    }

    public Collection<Descriptor> getRuleDescriptors() {
        return ruleDescriptors.values();
    }

}
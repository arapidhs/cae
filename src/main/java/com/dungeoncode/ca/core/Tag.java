package com.dungeoncode.ca.core;

import java.util.EnumSet;

/**
 * Categorization system for cellular automata rules and configurations, aligned with <i>Cellular Automata Machines</i> (MIT Press).
 * Organizes tags into categories using nested EnumSets for rule types, neighborhood types, operation types, behavior types, and source types.
 */
public enum Tag {
    // Rule Types
    DETERMINISTIC("Deterministic", "Rules with fixed, predictable state transitions based on current configuration"),
    PROBABILISTIC("Probabilistic", "Rules incorporating stochastic elements for state transitions"),
    SECOND_ORDER("Second Order", "Rules using previous states (via echo) alongside current states"),
    TOTALISTIC("Totalistic", "Rules where transitions depend on the sum of neighbor states"),
    REVERSIBLE("Reversible", "Rules allowing backward state reconstruction without information loss"),

    // Neighborhood Types
    VON_NEUMANN("Von Neumann", "Four orthogonal neighbors (North, South, East, West)"),
    MOORE("Moore", "Eight adjacent neighbors including diagonals"),
    EXTENDED_NEIGHBORHOOD("Extended", "Non-standard or user-defined neighborhood patterns"),

    // Operation Types
    COUNTING("Counting", "Rules based on summing or counting neighbor states (e.g., Majority, Anneal)"),
    LOGICAL("Logical", "Rules applying Boolean operations like AND, OR, XOR on neighbor states"),
    LOGICAL_OR("Logical OR", "Rules using OR operation to combine neighbor states"),
    LOGICAL_AND("Logical AND", "Rules using AND operation to combine neighbor states"),
    LOGICAL_XOR("Logical XOR", "Rules using XOR operation to combine neighbor states"),
    LOGICAL_NOT("Logical NOT", "Rules using NOT operation to invert neighbor states"),
    LOGICAL_NAND("Logical NAND", "Rules using NAND operation to combine neighbor states"),
    LOGICAL_NOR("Logical NOR", "Rules using NOR operation to combine neighbor states"),
    BITWISE("Bitwise", "Rules using bitwise operations to compute state transitions"),
    LOOKUP_TABLE("Lookup", "Rules using predefined tables to map neighbor states to next states"),
    VOTING("Voting", "Rules driven by majority or threshold-based neighbor state decisions"),
    DIFFUSION("Diffusion", "Rules modeling particle spread or interaction across the grid"),
    STATE_MACHINE("State Machine", "Rules that implement state machine-like behavior with distinct states and transitions"),
    SPECIES("Species", "Rules that implement species, types, or identity-based systems"),
    HANDSHAKE("Handshake", "Rules that implement handshake protocols for state transitions"),
    CONSERVATION("Conservation", "Rules that preserve particle or state counts"),
    GRAVITY("Gravity", "Rules that implement gravity-like or directional movement"),
    SPREAD("Spread", "Rules that implement pattern spreading or growth"),
    DEATH("Death", "Rules that include specific conditions for cell death or deactivation"),
    RANDOM("Random", "Rules that incorporate random or stochastic elements in state transitions or initialization"),
    STRUCTURED("Structured", "Rules that create organized, computable patterns suitable for computation"),

    // Behavior Types
    GROWTH("Growth", "Patterns that expand or evolve from initial seeds"),
    OSCILLATION("Oscillation", "Patterns cycling through repeating configurations"),
    CHAOTIC("Chaotic", "Complex, unpredictable patterns with sensitive dependence on initial conditions"),
    STABLE("Stable", "Patterns converging to fixed or minimally changing states"),
    WAVE("Wave", "Patterns propagating like waves across the grid"),
    PATTERN_SHAPING("Pattern Shaping", "Patterns modified by filling gaps or removing protrusions, such as filling pockets or erasing corners"),
    NEURAL("Neural", "Patterns simulating neural network-like behavior with firing and recovery states"),
    DECAY("Decay", "Patterns undergoing decay, erosion, or gradual state changes"),
    MONOTONIC("Monotonic", "Patterns with unidirectional, non-reversible growth"),
    HOLLOW("Hollow", "Patterns with empty or hollow interiors"),
    ACCUMULATION("Accumulation", "Patterns that accumulate or pile up states"),
    ORGANIC("Organic", "Patterns with natural, irregular, or organic-looking forms"),
    COMPETITIVE("Competitive", "Patterns with competitive growth or survival mechanisms"),
    DOMAIN("Domain", "Patterns forming distinct, interpenetrating domains"),
    FRACTAL("Fractal", "Patterns with self-similar structures repeating at different scales"),

    // Source Types
    BOOK("Book", "Configurations sourced from a book or academic publication"),
    PAPER("Paper", "Configurations sourced from a research paper"),
    URL("URL", "Configurations sourced from a web resource"),
    USER_CRAFTED("User Crafted", "Configurations created by a user"),
    DYNAMIC("Dynamic", "Configurations that can be modified during runtime"),
    EXPERIMENTAL("Experimental", "Configurations used for testing or experimentation"),
    COMMUNITY("Community", "Configurations shared by the community"),
    CLASSIC("Classic", "Well-known, fundamental configurations"),
    MODIFIED("Modified", "Variations of existing configurations"),
    HYBRID("Hybrid", "Combinations of multiple configurations or rules"),
    FOUNDATIONAL("Foundational", "Fundamental rules forming the basis of cellular automata study");

    // EnumSet Categories
    /**
     * Tags for rule types, defining the nature of state transitions.
     */
    public static final EnumSet<Tag> RULE_TYPES = EnumSet.of(
            DETERMINISTIC, PROBABILISTIC, SECOND_ORDER, TOTALISTIC, REVERSIBLE
    );

    /**
     * Tags for neighborhood types, defining the scope of neighbor interactions.
     */
    public static final EnumSet<Tag> NEIGHBORHOOD_TYPES = EnumSet.of(
            VON_NEUMANN, MOORE, EXTENDED_NEIGHBORHOOD
    );

    /**
     * Tags for operation types, defining the computational logic of rules.
     */
    public static final EnumSet<Tag> OPERATION_TYPES = EnumSet.of(
            COUNTING, LOGICAL, LOGICAL_OR, LOGICAL_AND, LOGICAL_XOR, LOGICAL_NOT, LOGICAL_NAND, LOGICAL_NOR,
            BITWISE, LOOKUP_TABLE, VOTING, DIFFUSION, STATE_MACHINE, SPECIES, HANDSHAKE, CONSERVATION,
            GRAVITY, SPREAD, DEATH, RANDOM, STRUCTURED
    );

    /**
     * Tags for behavior types, defining the emergent patterns of the automaton.
     */
    public static final EnumSet<Tag> BEHAVIOR_TYPES = EnumSet.of(
            GROWTH, OSCILLATION, CHAOTIC, STABLE, WAVE, PATTERN_SHAPING, NEURAL, DECAY, MONOTONIC,
            HOLLOW, ACCUMULATION, ORGANIC, COMPETITIVE, DOMAIN, FRACTAL
    );

    /**
     * Tags for source types, defining the origin of the configuration or rule.
     */
    public static final EnumSet<Tag> SOURCE_TYPES = EnumSet.of(
            BOOK, PAPER, URL, USER_CRAFTED, DYNAMIC, EXPERIMENTAL, COMMUNITY, CLASSIC, MODIFIED,
            HYBRID, FOUNDATIONAL
    );

    /**
     * The display name for UI or human-readable contexts.
     */
    private final String displayName;

    /**
     * The description of the tag's purpose or behavior.
     */
    private final String description;

    /**
     * Constructs a tag with the specified display name and description.
     *
     * @param displayName the human-readable name of the tag
     * @param description the description of the tag's purpose or behavior
     */
    Tag(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Returns the human-readable display name of the tag.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the description of the tag's purpose or behavior.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
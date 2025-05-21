package com.dungeoncode.ca.core;

import java.util.EnumSet;

/**
 * Categorization system for cellular automata rules, aligned with Cellular Automata Machines (MIT Press).
 * Uses a single enum with nested EnumSets for sub-categorization.
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
    RANDOM("Random", "Rules that incorporate random or stochastic elements in their state transitions or initialization"),
    LOGICAL_OR("Logical OR", "Rules using OR operation to combine neighbor states"),
    LOGICAL_AND("Logical AND", "Rules using AND operation to combine neighbor states"),
    LOGICAL_XOR("Logical XOR", "Rules using XOR operation to combine neighbor states"),
    LOGICAL_NOT("Logical NOT", "Rules using NOT operation to invert neighbor states"),
    LOGICAL_NAND("Logical NAND", "Rules using NAND operation to combine neighbor states"),
    LOGICAL_NOR("Logical NOR", "Rules using NOR operation to combine neighbor states"),
    BITWISE("Bitwise", "Rules using bitwise operations to compute state transitions"),

    // Behavior Types
    GROWTH("Growth", "Patterns that expand or evolve from initial seeds"),
    OSCILLATION("Oscillation", "Patterns cycling through repeating configurations"),
    CHAOTIC("Chaos", "Complex, unpredictable patterns with sensitive dependence on initial conditions"),
    STABLE("Stable", "Patterns converging to fixed or minimally changing states"),
    WAVE("Wave", "Patterns propagating like waves across the grid"),
    PATTERN_SHAPING("Pattern Shaping", "Rules that modify patterns by filling gaps or removing protrusions, such as filling pockets or erasing corners"),
    STRUCTURED("Structured", "Rules that create organized, computable patterns suitable for computation"),
    NEURAL("Neural", "Rules that simulate neural network-like behavior with firing and recovery states"),
    DECAY("Decay", "Rules that implement decay, erosion, or gradual state changes"),
    MONOTONIC("Monotonic", "Rules with unidirectional, non-reversible growth patterns"),
    HOLLOW("Hollow", "Rules that create patterns with empty or hollow interiors"),
    ACCUMULATION("Accumulation", "Rules that accumulate or pile up states in patterns"),
    ORGANIC("Organic", "Rules that create natural, irregular, or organic-looking patterns"),
    COMPETITIVE("Competitive", "Rules that implement competitive growth or survival mechanisms"),
    DOMAIN("Domain", "Rules that create distinct, interpenetrating domains"),
    FRACTAL("Fractal", "Rules that create self-similar patterns that repeat at different scales"),

    // Source Types
    BOOK("Book", "Configuration sourced from a book or academic publication"),
    PAPER("Paper", "Configuration sourced from a research paper"),
    URL("URL", "Configuration sourced from a web resource"),
    USER_CRAFTED("User Crafted", "Configuration created by a user"),
    DYNAMIC("Dynamic", "Configuration that can be modified during runtime"),
    EXPERIMENTAL("Experimental", "Configuration used for testing or experimentation"),
    COMMUNITY("Community", "Configuration shared by the community"),
    CLASSIC("Classic", "Well-known, fundamental configuration"),
    MODIFIED("Modified", "Variation of an existing configuration"),
    HYBRID("Hybrid", "Combination of multiple configurations or rules"),
    FOUNDATIONAL("Foundational", "Fundamental, well-known rules that form the basis of cellular automata study");

    // Predefined EnumSets for categories
    public static final EnumSet<Tag> RULE_TYPES = EnumSet.of(DETERMINISTIC, PROBABILISTIC, SECOND_ORDER, TOTALISTIC, REVERSIBLE);
    public static final EnumSet<Tag> NEIGHBORHOOD_TYPES = EnumSet.of(VON_NEUMANN, MOORE, EXTENDED_NEIGHBORHOOD);
    public static final EnumSet<Tag> OPERATION_TYPES = EnumSet.of(
            BITWISE, RANDOM, DEATH, SPREAD, GRAVITY, HANDSHAKE, CONSERVATION, STATE_MACHINE,
            COUNTING, LOGICAL, LOGICAL_OR, LOGICAL_AND, LOGICAL_XOR, LOGICAL_NOT,
            LOGICAL_NAND, LOGICAL_NOR, LOOKUP_TABLE, VOTING, DIFFUSION, STRUCTURED, SPECIES
    );
    public static final EnumSet<Tag> BEHAVIOR_TYPES = EnumSet.of(FRACTAL, DOMAIN, COMPETITIVE, ORGANIC, ACCUMULATION, MONOTONIC, DECAY, NEURAL, GROWTH, OSCILLATION, CHAOTIC, STABLE, WAVE, PATTERN_SHAPING, HOLLOW);
    public static final EnumSet<Tag> SOURCE_TYPES = EnumSet.of(BOOK, PAPER, URL, USER_CRAFTED, DYNAMIC, EXPERIMENTAL, COMMUNITY, CLASSIC, MODIFIED, HYBRID, FOUNDATIONAL);

    private final String displayName;
    private final String description;

    Tag(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

}
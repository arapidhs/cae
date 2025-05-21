package com.dungeoncode.ca.core;

/**
 * Defines constants used for configuring a cellular automaton and managing citations.
 * These constants represent keys for configuration parameters and citation fields.
 */
public class Constants {

    /**
     * Key for the grid configuration parameter.
     */
    public static final String CONF_GRID = "grid";

    /**
     * Key for the rule configuration parameter.
     */
    public static final String CONF_RULES = "rules";

    /**
     * Key for the update interval configuration parameter, in milliseconds.
     */
    public static final String CONF_INTERVAL_MILLIS = "intervalMillis";

    /**
     * Key for the citation title field.
     */
    public static final String CITATION_TITLE = "title";

    /**
     * Key for the citation subtitle field.
     */
    public static final String CITATION_SUBTITLE = "subtitle";

    /**
     * Key for the citation section field.
     */
    public static final String CITATION_SECTION = "section";

    /**
     * Key for the citation subsection field.
     */
    public static final String CITATION_SUBSECTION = "subsection";

    /**
     * File name of rules descriptors.
     */
    public static final String DESCRIPTORS_FILE_RULES = "descriptors-rules.json";

    /**
     * File name of configurations descriptors.
     */
    public static final String DESCRIPTORS_FILE_CONFS = "descriptors-confs.json";

    /**
     * File name of initializers descriptors.
     */
    public static final String DESCRIPTORS_FILE_INITS = "descriptors-inits.json";
}
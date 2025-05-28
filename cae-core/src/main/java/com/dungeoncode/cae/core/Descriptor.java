package com.dungeoncode.cae.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

/**
 * Describes a cellular automaton component, such as a rule or configuration, with metadata including
 * a unique identifier, name, description, citation, and categorization tags.
 * Supports JSON serialization and deserialization via Jackson annotations.
 */
public class Descriptor {
    /**
     * The unique identifier for this descriptor.
     */
    private final int id;

    /**
     * The name of the component.
     */
    private final String name;

    /**
     * The description of the component.
     */
    private final String description;

    /**
     * The citation for the component, if applicable.
     */
    private final Citation citation;

    /**
     * The set of tags categorizing the component.
     */
    private final EnumSet<Tag> tags;

    /**
     * Constructs a new descriptor with the specified metadata.
     *
     * @param id          the unique identifier for this descriptor
     * @param name        the name of the component, must not be null
     * @param description the description of the component, must not be null
     * @param citation    the citation for the component, or null if none
     * @param tags        the set of tags categorizing the component, or null for an empty set
     * @throws NullPointerException if name or description is null
     */
    @JsonCreator
    public Descriptor(
            @JsonProperty("id") final int id,
            @JsonProperty("name") @Nonnull final String name,
            @JsonProperty("description") @Nonnull final String description,
            @JsonProperty("citation") @Nullable final Citation citation,
            @JsonProperty("tags") @Nullable final EnumSet<Tag> tags) {

        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");

        this.id = id;
        this.name = name;
        this.description = description;
        this.citation = citation;
        this.tags = tags != null ? EnumSet.copyOf(tags) : EnumSet.noneOf(Tag.class);
    }

    /**
     * Returns the unique identifier for this descriptor.
     *
     * @return the descriptor ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the component.
     *
     * @return the component name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the component.
     *
     * @return the component description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the citation for the component.
     *
     * @return the {@link Citation}, or null if not specified
     */
    public Citation getCitation() {
        return citation;
    }

    /**
     * Returns the set of tags categorizing the component.
     *
     * @return an unmodifiable {@link EnumSet} of {@link Tag} objects, never null
     */
    @Nonnull
    public EnumSet<Tag> getTags() {
        return EnumSet.copyOf(tags);
    }

    /**
     * Returns a formatted string representation of the descriptor, including its ID, name,
     * description, citation (if present), and tags (if any).
     *
     * @return the formatted descriptor string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Description: ").append(description).append("\n");
        if (citation != null) {
            sb.append("Citation: ").append(citation).append("\n");
        }
        if (!tags.isEmpty()) {
            sb.append("Tags: ").append(tags).append("\n");
        }
        return sb.toString();
    }
}
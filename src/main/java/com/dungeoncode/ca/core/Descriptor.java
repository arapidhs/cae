package com.dungeoncode.ca.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

/**
 * Represents a descriptor for a cellular automaton component, containing its name,
 * description, citation information, and tags.
 */
public class Descriptor {
    private final int id;
    private final String name;
    private final String description;
    private final Citation citation;
    private final EnumSet<Tag> tags;

    @JsonCreator
    public Descriptor(
            @JsonProperty("id") final int id,
            @JsonProperty("name") @Nonnull final String name,
            @JsonProperty("description") @Nonnull final String description,
            @JsonProperty("citation") @Nullable final Citation citation,
            @JsonProperty("tags") @Nullable final EnumSet<Tag> tags) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(description);

        this.id = id;
        this.name = name;
        this.description = description;
        this.citation = citation;
        this.tags = tags != null ? EnumSet.copyOf(tags) : EnumSet.noneOf(Tag.class);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Citation getCitation() {
        return citation;
    }

    @Nonnull
    public EnumSet<Tag> getTags() {
        return EnumSet.copyOf(tags);
    }

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
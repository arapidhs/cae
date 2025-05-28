package com.dungeoncode.cae.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents a citation for a cellular automaton rule or configuration, storing details about authors, publication, and source location.
 * Supports formatting citations for books, papers, or online resources, and JSON serialization/deserialization via Jackson.
 */
public class Citation {
    /**
     * The list of authors for the cited work.
     */
    private final List<String> authors;

    /**
     * The map of citation details, including title, subtitle, section, and subsection.
     */
    private final Map<String, String> details;

    /**
     * The page number in the cited work.
     */
    private final int page;

    /**
     * The publication year of the cited work.
     */
    private final int year;

    /**
     * The place of publication.
     */
    private final String place;

    /**
     * The publisher of the cited work.
     */
    private final String publisher;

    /**
     * The URL referencing the cited work, if applicable.
     */
    private final String url;

    /**
     * The DOI (Digital Object Identifier) of the cited work, if applicable.
     */
    private final String doi;

    /**
     * Constructs a new citation with the specified details.
     *
     * @param title      the title of the work, must not be null
     * @param subtitle   the subtitle of the work, or null if none
     * @param section    the section number or name, or null if none
     * @param subsection the subsection number or name, or null if none
     * @param page       the page number in the work
     * @param year       the publication year
     * @param place      the place of publication, or null if none
     * @param publisher  the publisher of the work, or null if none
     * @param url        the URL referencing the work, or null if none
     * @param doi        the DOI of the work, or null if none
     * @param authors    the list of author names, or null if unspecified
     * @throws NullPointerException if title is null
     */
    @JsonCreator
    public Citation(
            @JsonProperty("title") @Nonnull String title,
            @JsonProperty("subtitle") @Nullable String subtitle,
            @JsonProperty("section") @Nullable String section,
            @JsonProperty("subsection") @Nullable String subsection,
            @JsonProperty("page") int page,
            @JsonProperty("year") int year,
            @JsonProperty("place") @Nullable String place,
            @JsonProperty("publisher") @Nullable String publisher,
            @JsonProperty("url") @Nullable String url,
            @JsonProperty("doi") @Nullable String doi,
            @JsonProperty("authors") @Nullable List<String> authors) {
        this.authors = (authors == null || authors.isEmpty())
                ? Collections.emptyList()
                : authors;
        this.page = page;
        this.year = year;
        this.place = place;
        this.publisher = publisher;
        this.url = url;
        this.doi = doi;

        this.details = new HashMap<>();
        this.details.put(Constants.CITATION_TITLE, Objects.requireNonNull(title, "Title cannot be null"));
        this.details.put(Constants.CITATION_SUBTITLE, subtitle);
        this.details.put(Constants.CITATION_SECTION, section);
        this.details.put(Constants.CITATION_SUBSECTION, subsection);
    }

    /**
     * Returns the list of authors for the cited work.
     *
     * @return an unmodifiable list of author names
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Returns the map of citation details.
     *
     * @return an unmodifiable map containing title, subtitle, section, and subsection
     */
    public Map<String, String> getDetails() {
        return Collections.unmodifiableMap(details);
    }

    /**
     * Returns the page number in the cited work.
     *
     * @return the page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Returns the publication year of the cited work.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the place of publication.
     *
     * @return the place, or null if not specified
     */
    public String getPlace() {
        return place;
    }

    /**
     * Returns the publisher of the cited work.
     *
     * @return the publisher, or null if not specified
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Returns the URL referencing the cited work.
     *
     * @return the URL, or null if not specified
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the DOI of the cited work.
     *
     * @return the DOI, or null if not specified
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Returns a formatted string representation of the citation, following a standard citation style.
     *
     * @return the formatted citation string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Add authors
        if (!authors.isEmpty()) {
            sb.append(String.join(", ", authors)).append(". ");
        }

        // Add title and subtitle
        String title = details.get(Constants.CITATION_TITLE);
        String subtitle = details.get(Constants.CITATION_SUBTITLE);
        sb.append(title);
        if (subtitle != null && !subtitle.isEmpty()) {
            sb.append(": ").append(subtitle);
        }
        sb.append(". ");

        // Add section information
        String section = details.get(Constants.CITATION_SECTION);
        String subsection = details.get(Constants.CITATION_SUBSECTION);
        if (section != null && !section.isEmpty()) {
            sb.append("Chapter ").append(section);
            if (subsection != null && !subsection.isEmpty()) {
                sb.append(", Section ").append(subsection);
            }
            sb.append(", ");
        }

        // Add page, publisher, place, and year
        sb.append("p. ").append(page).append(". ");
        if (publisher != null && !publisher.isEmpty()) {
            sb.append(publisher).append(", ");
        }
        if (place != null && !place.isEmpty()) {
            sb.append(place).append(", ");
        }
        sb.append(year).append(". ");

        // Add DOI if present
        if (doi != null && !doi.isEmpty()) {
            sb.append("https://doi.org/").append(doi);
        }
        // Add URL if present and no DOI
        else if (url != null && !url.isEmpty()) {
            sb.append(url);
        }

        return sb.toString();
    }
}
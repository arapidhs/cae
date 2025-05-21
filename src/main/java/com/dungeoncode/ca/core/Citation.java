package com.dungeoncode.ca.core;

import java.util.*;

/**
 * Represents a citation or reference for a cellular automaton configuration.
 * Stores information about authors, publication details, and location within the source.
 */
public class Citation {
    private final List<String> authors;
    private final Map<String, String> details;
    private final int page;
    private final int year;
    private final String place;
    private final String publisher;
    private final String url;
    private final String doi;

    /**
     * Constructs a new Citation with the specified details.
     *
     * @param title      The title of the work
     * @param subtitle   Optional subtitle of the work
     * @param section    The section number or name
     * @param subsection The subsection number or name
     * @param page       The page number
     * @param year       The publication year
     * @param place      The place of publication
     * @param publisher  The publisher of the work
     * @param url        Optional URL reference
     * @param doi        Optional DOI reference
     * @param authors    Variable number of author names
     */
    public Citation(String title, String subtitle,
                    String section, String subsection, int page, int year,
                    String place, String publisher, String url, String doi,
                    String... authors) {
        this.authors = (authors == null || authors.length == 0)
                ? Collections.emptyList()
                : Arrays.asList(authors);
        this.page = page;
        this.year = year;
        this.place = place;
        this.publisher = publisher;
        this.url = url;
        this.doi = doi;

        this.details = new HashMap<>();
        this.details.put(Constants.CITATION_TITLE, title);
        this.details.put(Constants.CITATION_SUBTITLE, subtitle);
        this.details.put(Constants.CITATION_SECTION, section);
        this.details.put(Constants.CITATION_SUBSECTION, subsection);
    }

    /**
     * Returns the list of authors.
     *
     * @return List of author names
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Returns the citation details map.
     *
     * @return Map containing title, subtitle, section, and subsection
     */
    public Map<String, String> getDetails() {
        return details;
    }

    /**
     * Returns the page number.
     *
     * @return The page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Returns the publication year.
     *
     * @return The year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the place of publication.
     *
     * @return The place
     */
    public String getPlace() {
        return place;
    }

    /**
     * Returns the publisher.
     *
     * @return The publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Returns the URL reference.
     *
     * @return The URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the DOI reference.
     *
     * @return The DOI
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Returns a formatted string representation of the citation.
     *
     * @return Formatted citation string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Add authors
        if (!authors.isEmpty()) {
            sb.append(String.join(", ", authors));
            sb.append(". ");
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
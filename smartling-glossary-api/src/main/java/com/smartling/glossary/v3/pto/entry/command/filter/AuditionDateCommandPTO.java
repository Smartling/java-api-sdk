package com.smartling.glossary.v3.pto.entry.command.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Audition date/time filtering.
 */

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuditionDateCommandPTO.ExactDate.class, name = AuditionDateCommandPTO.EXACT_DATE),
    @JsonSubTypes.Type(value = AuditionDateCommandPTO.BeforeDate.class, name = AuditionDateCommandPTO.BEFORE),
    @JsonSubTypes.Type(value = AuditionDateCommandPTO.AfterDate.class, name = AuditionDateCommandPTO.AFTER),
    @JsonSubTypes.Type(value = AuditionDateCommandPTO.DateRange.class, name = AuditionDateCommandPTO.DATE_RANGE)
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditionDateCommandPTO {
    /**
     * Exact date-time.
     */
    static final String EXACT_DATE = "exact";
    /**
     * From date-time to date-time.
     */
    static final String DATE_RANGE = "date_range";
    /**
     * Before date-time.
     */
    static final String BEFORE = "before";
    /**
     * After date-time.
     */
    static final String AFTER = "after";

    /**
     * Allow defining on which level ( entry / translation ) filtering should be performed.
     * USE  values:
     * - ENTRY - to filter on glossary entry level,
     * - LOCALE - to filter on glossary entry translation level,
     * - ANY - to filter on both levels ( this is default ).
     */
    private String level;

    /**
     * Command type, for  deserialization goals.
     */
    @JsonProperty("type")
    protected abstract String getType();

    /**
     * Exact date filter.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class ExactDate extends AuditionDateCommandPTO {
        /**
         * Exact date to match.
         */
        private String date;

        @Override
        protected String getType() {
            return EXACT_DATE;
        }
    }

    /**
     * Before date filter.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class BeforeDate extends AuditionDateCommandPTO {
        /**
         * Before date value.
         */
        private String date;

        @Override
        protected String getType() {
            return BEFORE;
        }
    }

    /**
     * After date filter.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class AfterDate extends AuditionDateCommandPTO {
        /**
         * After date value.
         */
        private String date;

        @Override
        protected String getType() {
            return AFTER;
        }
    }

    /**
     * DateRange filter.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class DateRange extends AuditionDateCommandPTO {
        /**
         * Range start.
         */
        private String from;
        /**
         * Range end.
         */
        private String to;

        @Override
        protected String getType() {
            return DATE_RANGE;
        }
    }

}

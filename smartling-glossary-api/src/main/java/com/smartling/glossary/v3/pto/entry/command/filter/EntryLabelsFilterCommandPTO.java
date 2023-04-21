package com.smartling.glossary.v3.pto.entry.command.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * Labels search filters in scope of entry.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EntryLabelsFilterCommandPTO.AssociatedFilter.class, name = EntryLabelsFilterCommandPTO.ASSOCIATED_LABELS),
    @JsonSubTypes.Type(value = EntryLabelsFilterCommandPTO.NoFilter.class, name = EntryLabelsFilterCommandPTO.NO_LABELS),
    @JsonSubTypes.Type(value = EntryLabelsFilterCommandPTO.AnyFilter.class, name = EntryLabelsFilterCommandPTO.ANY_LABELS)
})
@ToString
@EqualsAndHashCode
public abstract class EntryLabelsFilterCommandPTO {
    /**
     * Associated labels filter.
     */
    public static final String ASSOCIATED_LABELS = "associated";
    /**
     * No labels filter.
     */
    public static final String NO_LABELS = "empty";
    /**
     * Any labels filter.
     */
    public static final String ANY_LABELS = "any";

    @JsonProperty("type")
    protected abstract String getType();

    /**
     * Associated labels filter.
     */
    @NoArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class AssociatedFilter extends EntryLabelsFilterCommandPTO {
        /**
         * LabelUids to be used.
         */
        @Getter
        @Setter
        private List<UUID> labelUids;

        @Override
        protected String getType() {
            return ASSOCIATED_LABELS;
        }
    }

    /**
     * No labels filter.
     */
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class NoFilter extends EntryLabelsFilterCommandPTO {

        @Override
        protected String getType() {
            return NO_LABELS;
        }
    }

    /**
     * Any labels filter.
     */
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class AnyFilter extends EntryLabelsFilterCommandPTO {

        @Override
        protected String getType() {
            return ANY_LABELS;
        }
    }
}

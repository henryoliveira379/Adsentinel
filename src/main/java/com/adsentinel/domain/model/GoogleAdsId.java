package com.adsentinel.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.regex.Pattern;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor // For JPA
public class GoogleAdsId {

    private static final String ADS_ID_REGEX = "^\\d{3}-\\d{3}-\\d{4}$";
    private static final Pattern PATTERN = Pattern.compile(ADS_ID_REGEX);

    private String value;

    public GoogleAdsId(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid Google Ads ID format (expected 123-456-7890): " + value);
        }
        this.value = value;
    }
}

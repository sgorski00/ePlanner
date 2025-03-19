package pl.sgorski.EPlanner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {
    NOT_COMPLETED("Not completed"),
    COMPLETED("Completed"),
    ARCHIVED("Archived");

    private final String prettyName;
}

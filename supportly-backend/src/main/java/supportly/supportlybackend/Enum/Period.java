package supportly.supportlybackend.Enum;

import lombok.Getter;

@Getter
public enum Period {
    MONTHLY(1), QUARTERLY(3), HALF_YEARLY(6), YEARLY(12);;
    private final int month;

    Period(int month) {
        this.month = month;
    }

}

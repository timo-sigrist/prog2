package ch.zhaw.prog2.functional.streaming.finance;

public enum PaymentsPerYear {
    TWELVE(12),
    THIRTEEN(13);

    private final int value;

    PaymentsPerYear(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

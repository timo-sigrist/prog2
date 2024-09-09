package ch.zhaw.prog2.functional.streaming.finance;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void isIbanAccepted_valid() {
        List<String> accepted = List.of("BE71 0961 2345 6769", "FR76 3000 6000 0112 3456 7890 189" ,
            "DE91 1000 0000 0123 4567 89", "CH10 00230 00A109822346");
        accepted.forEach(iban ->
            assertTrue(BankAccount.isIbanAccepted(iban), iban + " is a valid IBAN"));
    }

    @Test
    void isIbanAccepted_invalid() {
        List<String> invalid = List.of("BE71 0961 2345 6769 9999 8888 7777 6666 123", "BE71", "CH10");
        invalid.forEach(iban ->
            assertFalse(BankAccount.isIbanAccepted(iban), iban + " is not a valid IBAN"));
    }

}

package ch.zhaw.prog2.heartbeat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 * Test Class for Pacemaker
 */
public class PacemakerTest {

    Pacemaker pacemaker;

    @Mock
    Heart mockHeart;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockHeart.setHeartRate(15)).thenReturn(false);
        when(mockHeart.setHeartRate(240)).thenReturn(false);

        when(mockHeart.setHeartRate(80)).thenReturn(true);
        when(mockHeart.setHeartRate(140)).thenReturn(true);


        pacemaker = new Pacemaker(mockHeart);
    }

    /**
     * Test if setHeartRate does throw correct exception when rate is rejected (because frequency is out of range)
     */
    @Test
    void testSetHeartRateRejectsFrequenciesOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> pacemaker.setHeartRate(15));
        assertThrows(IllegalArgumentException.class, () -> pacemaker.setHeartRate(240));
    }


    /**
     * Test if setHeartRate does correctly set the rate when frequency is inside range
     */
    @Test
    void testSetHeartRateAppliesFrequenciesInsideRange() {
        when(mockHeart.getHeartRate()).thenReturn(80);
        assertEquals(80,pacemaker.setHeartRate(80));
        when(mockHeart.getHeartRate()).thenReturn(140);
        assertEquals(140,pacemaker.setHeartRate(140));


    }

}

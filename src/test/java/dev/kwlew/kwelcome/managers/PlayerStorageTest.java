package dev.kwlew.kwelcome.managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerStorageTest {

    @Test
    void recognizesAPlayersFirstServerJoin() {
        assertTrue(PlayerStorage.determineFirstJoin(-1L, true));
    }

    @Test
    void doesNotTreatExistingPlayersAsNewAfterInstallingThePlugin() {
        assertFalse(PlayerStorage.determineFirstJoin(-1L, false));
    }

    @Test
    void doesNotRepeatTheFirstJoinMessageForStoredPlayers() {
        assertFalse(PlayerStorage.determineFirstJoin(1_700_000_000_000L, true));
    }
}

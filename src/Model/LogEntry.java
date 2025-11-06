package Model;

/**
 * A string message. These will be built based on the messages in each Locale. Many of these will compose the entire log
 * which will be recorded iin the gamestate and readable to the player.
 *
 */
public class LogEntry {
    private String logEntry;

    /**
     * Builds a new entry for adding it to the player's log. Will be built off premade messages/
     * notifications in the resource bundle. Kept this way in case we decide to allow players to create logs.
     * @param logEntry
     */
    public LogEntry(String logEntry) {
        this.logEntry = logEntry;
    }

}

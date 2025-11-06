package Model;

import java.util.ArrayList;

/**
 * Record of log entries for each player.
 */
public class PlayerLog {
    private ArrayList<LogEntry> log;

    public void addLog(LogEntry logentry){
        log.add(logentry);
    }
}

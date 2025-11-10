package Model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * Record of log entries for each player.
 */
public class PlayerLog {
    private ArrayList<LogEntry> log;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addLog(LogEntry logentry){
        log.add(logentry);
    }
}

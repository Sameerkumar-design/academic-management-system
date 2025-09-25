package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Thread-safe Singleton for application configuration.
 */
public final class AppConfig {
    private static volatile AppConfig instance;

    private Path databasePath = Paths.get("data");
    private int maxCreditsPerStudent = 18;
    private Path backupLocation = Paths.get("backups");

    private AppConfig() {}

    public static AppConfig getInstance() {
        AppConfig result = instance;
        if (result == null) {
            synchronized (AppConfig.class) {
                result = instance;
                if (result == null) {
                    result = new AppConfig();
                    instance = result;
                }
            }
        }
        return result;
    }

    public Path getDatabasePath() { return databasePath; }
    public void setDatabasePath(Path databasePath) { this.databasePath = databasePath; }

    public int getMaxCreditsPerStudent() { return maxCreditsPerStudent; }
    public void setMaxCreditsPerStudent(int maxCreditsPerStudent) { this.maxCreditsPerStudent = maxCreditsPerStudent; }

    public Path getBackupLocation() { return backupLocation; }
    public void setBackupLocation(Path backupLocation) { this.backupLocation = backupLocation; }
}



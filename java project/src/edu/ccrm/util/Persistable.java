package edu.ccrm.util;

import java.io.IOException;

/**
 * Persistable interface as an abstraction for save/load responsibilities.
 */
public interface Persistable {
    void save() throws IOException;
    void load() throws IOException;
}



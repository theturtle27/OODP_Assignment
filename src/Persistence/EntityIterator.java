package Persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import Persistence.Entity;

/**
 * EntityIterator represents a pointer to an open file with a collection of entity.
 * EntityIterator lazily loads Entity matching the given predicate, ensuring that unused
 * entity will not be loaded in the process. Close this EntityIterator when not in use to prevent
 * a resource leak.
 * @author YingHao
 *
 * @param <T>
 */
public class EntityIterator<T extends Entity> implements Iterator<T> {
    private final Persistence persistence;
    private BufferedReader reader;
    private T entity;

    public EntityIterator(Persistence persistence, File file) throws Exception {
        this.persistence = persistence;
        try {
            this.reader = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException e) {
            this.reader = null;
        }
    }

    @Override
    public boolean hasNext() {
        this.entity = null;

        if(this.entity == null && reader != null) {
            try {
                String entityString = null;
                while(this.entity == null && ((entityString = reader.readLine()) != null)) {
                    T next = persistence.deserialize(entityString, false);
                            this.entity = next;
                }

                // Reached the end of the file, close iterator.
                if(this.entity == null && entityString == null)
                    this.close();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        return this.entity != null;
    }

    @Override
    public T next() {
        T entity = this.entity;
        this.entity = null;

        return entity;
    }

    public void close() throws Exception {
        reader.close();
        entity = null;
    }

}

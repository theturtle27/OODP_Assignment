package Persistence;

import java.io.File;

public class EntityIterable<T extends Entity> implements Iterable<T> {
    private final Persistence persistence;
    private final File file;

    public EntityIterable(Persistence persistence, File file) {
        this.persistence = persistence;
        this.file = file;
    }

    @Override
    public EntityIterator<T> iterator() {
        try {
            return new EntityIterator<T>(persistence, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


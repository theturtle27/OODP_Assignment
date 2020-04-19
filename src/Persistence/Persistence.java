package Persistence;

import Model.Guest.Guest;
import Model.Menu.MenuItem;

import java.io.*;
import java.lang.ref.SoftReference;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Pattern;

public class Persistence {

    public final static String FIELD_DELIMITER = "|";
    public final static String KV_DELIMITER = ":";
    public final static String ARRAY_DELIMITER = ",";
    public final static String DATA_DIR = "data";
    public final static String DATA_EXT = ".d";
    public final static String TMP_DIR = "tmp";
    public final static String TMP_EXT = ".t";
    public final static String KEY_AUTO_ID = "{type}.auto-id";
    public final static String AUTO_ID_TYPE_REGEX = "{type}";

    private final Map<Class<?>, Field[]> fCache;
    private final Map<Class<?>, Map<Long, SoftReference<Entity>>> entityCache;
    private final Map<Class<?>, ArrayList<SoftReference<Entity>>> entityCacheArray;
    private final Properties configuration;
    private final File configurationFile;
    public final static Properties DEFAULT_CONFIGURATION;

    static{
        DEFAULT_CONFIGURATION = new Properties();
    }

    public Persistence(File configurationFile) throws Exception{
        this.fCache = new HashMap<Class<?>, Field[]>();
        this.entityCache = new HashMap<Class<?>, Map<Long, SoftReference<Entity>>>();
        this.entityCacheArray = new HashMap<Class<?>, ArrayList<SoftReference<Entity>>>();
        this.configurationFile = configurationFile;
        this.configuration = new Properties(DEFAULT_CONFIGURATION);

        AutoCloseable stream = null;
        try {
            if(this.configurationFile.createNewFile()) {
                FileOutputStream out = new FileOutputStream(this.configurationFile);
                stream = out;

                // Initializes configuration file with the defaults
                DEFAULT_CONFIGURATION.store(out, null);
            }
            else {
                FileInputStream in = new FileInputStream(this.configurationFile);
                stream = in;

                // Loads configurations from the configuration file
                this.configuration.load(in);
            }
        } finally {
            if(stream != null)
                stream.close();
        }


        File dataDir = new File(DATA_DIR);
        File tmpDir = new File(TMP_DIR);

        dataDir.mkdir();
        tmpDir.mkdir();
        loadAllData();
    }

    private void loadAllData() throws Exception{
        loadCacheArray(MenuItem.class);
    }

    private <T extends Entity> void loadCacheArray(Class<T> type) throws Exception{
        File dataFile = this.getDataFile(type);
        dataFile.createNewFile();

        T entity = null;

        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        try {
            String entityString = null;
            while((entityString = reader.readLine()) != null) {
                entity = this.deserialize(entityString, false);
                addEntityToCacheArray(type,entity);
            }
        } finally {
            reader.close();
        }
    }

    public <T extends Entity> void writeAllData() throws Exception{
        for (Map.Entry entry : this.entityCacheArray.entrySet()) {
            Class<T> type = (Class<T>)entry.getKey();
            File dataFile = this.getDataFile(type);
            dataFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getDataFile(type), true));
            try {
                // Writes the serialized entity into the data file
                for (SoftReference<Entity> e : (ArrayList<SoftReference<Entity>>)entry.getValue()) {
                    writer.write(this.serialize(e.get()).toString());
                    writer.newLine();
                }
            } catch(IOException e) {
                throw e;
            } finally {
                writer.close();
            }

        }
    }

    public <T extends Entity> T createCache(T entity, Class<T> type) throws Exception{
        Field idField = getFieldsForType(Entity.class)[0];
        String idKey = KEY_AUTO_ID.replace(AUTO_ID_TYPE_REGEX, type.getName().toLowerCase());
        long identifier = Long.parseLong(this.configuration.getProperty(idKey, Long.toString(0))) + 1;
        idField.set(entity, identifier);

        addEntityToCacheArray(type,entity);

        this.configuration.setProperty(idKey, Long.toString(identifier));

        FileOutputStream out = new FileOutputStream(this.configurationFile);
        try {
            this.configuration.store(out, null);
        } finally {
            out.close();
        }

        return entity;
    }

    public <T extends Entity> boolean updateCache(T entity,int index, Class<T> type) throws Exception {
        boolean success = false;
        try{
            updateEntityToCacheArray(type,index,entity);
            success = true;
        }catch (Exception e){

        }

        return success;
    }

    public <T extends Entity> T retrieveByIndex(int index, Class<T> type) throws Exception {
        T entity = null;

        entity = getEntityFromCacheArray(type,index);

        return entity;
    }

    public <T extends Entity> ArrayList<SoftReference<Entity>> retrieveAll(int index, Class<T> type) throws Exception {
        ArrayList<SoftReference<Entity>> entityArray = this.entityCacheArray.get(type);
        return entityArray;
    }

    public <T extends Entity> boolean deleteCache(T entity,int index, Class<T> type) throws Exception {
        deleteEntityToCacheArray(type,index);
        return true;
    }

    private <T extends Entity> T getEntityFromCacheArray(Class<T> type, int index) {
        T entity = null;

        if(this.entityCache.containsKey(type)) {
            ArrayList<SoftReference<Entity>> entityArray = this.entityCacheArray.get(type);

            // Attempts to retrieve entity from soft reference
            entity = (T) entityArray.get(index).get();
            // Remove soft reference from cache if it gets invalid
            if(entity == null)
                entityArray.remove(index);

        }

        return entity;
    }

    private <T extends Entity> void addEntityToCacheArray(Class<T> type , T entity) {
        ArrayList<SoftReference<Entity>> entityArray;

        if(this.entityCacheArray.containsKey(type))
            entityArray = this.entityCacheArray.get(type);
        else {
            // Creates a new map for the specific type.
            entityArray = new ArrayList<SoftReference<Entity>>();
            this.entityCacheArray.put(type, entityArray);
        }

        // Adds a new soft reference for the entity into cache.
        entityArray.add(new SoftReference<Entity>(entity));
    }

    private <T extends Entity> void updateEntityToCacheArray(Class<T> type ,int index, T entity) {
        ArrayList<SoftReference<Entity>> entityArray;

        if(this.entityCacheArray.containsKey(type))
            entityArray = this.entityCacheArray.get(type);
        else {
            // Creates a new map for the specific type.
            entityArray = new ArrayList<SoftReference<Entity>>();
            this.entityCacheArray.put(type, entityArray);
        }

        // Adds a new soft reference for the entity into cache.
        entityArray.set(index,new SoftReference<Entity>(entity));
    }

    private <T extends Entity> void deleteEntityToCacheArray(Class<T> type ,int index) {
        ArrayList<SoftReference<Entity>> entityArray;

        if(this.entityCacheArray.containsKey(type))
            entityArray = this.entityCacheArray.get(type);
        else {
            // Creates a new map for the specific type.
            entityArray = new ArrayList<SoftReference<Entity>>();
            this.entityCacheArray.put(type, entityArray);
        }

        // Adds a new soft reference for the entity into cache.
        entityArray.remove(index);
    }


    public <T extends Entity> T create(T entity, Class<T> type) throws Exception{
        Field idField = getFieldsForType(Entity.class)[0];
        String idKey = KEY_AUTO_ID.replace(AUTO_ID_TYPE_REGEX, type.getName().toLowerCase());
        long identifier = Long.parseLong(this.configuration.getProperty(idKey, Long.toString(0))) + 1;
        idField.set(entity, identifier);

        // Obtain a reference to the data file and attempts to create it.
        File dataFile = this.getDataFile(type);
        dataFile.createNewFile();

        // Obtain a writer for the data file
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getDataFile(type), true));
        try {
            // Writes the serialized entity into the data file
            writer.write(this.serialize(entity).toString());
            writer.newLine();
        } catch(IOException e) {
            // Unmanage entity and rethrow exception
            idField.set(entity, Long.MIN_VALUE);
            throw e;
        } finally {
            writer.close();
        }

        // Save auto generated identifier in configuration file
        this.configuration.setProperty(idKey, Long.toString(identifier));
        // Obtain output stream for configuration file and save configurations
        FileOutputStream out = new FileOutputStream(this.configurationFile);
        try {
            this.configuration.store(out, null);
        } finally {
            out.close();
        }

        return entity;
    }


    private <T extends Entity> File getDataFile(Class<T> type) {
        return new File(DATA_DIR +
                File.separator + type.getName().toLowerCase().replace(".", "-") +
                DATA_EXT);
    }

    /**
     * Gets the temporary file for the specified type.
     * @param type
     * @return Temporary file for the specified type.
     */
    private <T extends Entity> File getTemporaryFile(Class<T> type) {
        return new File(TMP_DIR +
                File.separator + type.getName().toLowerCase().replace(".", "-") +
                TMP_EXT);
    }

    public <T extends Entity> boolean update(T entity, Class<T> type) throws Exception {
        boolean success = false;

        if(entity.isManaged()) {
            // Get references to data and temporary file for the specified type
            File dataFile = this.getDataFile(type);
            File tmpFile = this.getTemporaryFile(type);

            // Move and rename data file to temporary file
            Files.move(dataFile.toPath(), tmpFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

            // Create new file and write data from temporary file to new file. Replace entity matching the specified
            // entity's identifier with serialized data.
            dataFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            try {
                String entityString = null;
                while((entityString = reader.readLine()) != null) {
                    long _id = Long.parseLong(entityString.split(Pattern.quote(FIELD_DELIMITER))[1]
                            .split(KV_DELIMITER)[1]);
                    if(_id == entity.getIdentifier()) {
                        writer.write(this.serialize(entity).toString());
                        success = true;
                    }
                    else {
                        writer.write(entityString);
                    }

                    writer.newLine();
                }
            } finally {
                reader.close();
                writer.close();
            }

            // Delete temporary file
            tmpFile.delete();
        }

        return success;
    }

    public <T extends Entity> Iterable<T> search(Class<T> type) throws Exception{
        return new EntityIterable(this, this.getDataFile(type));
    }

    public <T extends Entity> boolean delete(T entity, Class<T> type){
        return true;
    }


    public <T extends Entity> T retrieveByID(long id, Class<T> type) throws Exception {
        T entity = null;

        // Read data file and find matching IDs to deserialize
        File dataFile = this.getDataFile(type);
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));
        try {
            String entityString = null;
            while((entityString = reader.readLine()) != null) {
                long _id = Long.parseLong(entityString.split(Pattern.quote(FIELD_DELIMITER))[1]
                        .split(KV_DELIMITER)[1]);
                if(_id == id) {
                    // Deserialize data into entity for matching IDs.
                    entity = this.deserialize(entityString, false);
                    break;
                }
            }
        } finally {
            reader.close();
        }

        return entity;
    }



    private StringBuilder serialize(Class type, Object value, StringBuilder builder) throws Exception {
        if(type.isPrimitive() || type.equals(String.class) || Enum.class.isAssignableFrom(type)) {
            if(type.equals(String.class))
                value = ((String)value).trim();
            builder.append(value);
        }
        else if(Date.class.isAssignableFrom(type))
            builder.append(((Date) value).getTime());
        else if(Entity.class.isAssignableFrom(type)) {
            // Persisting an entity reference
            Entity reference = (Entity) value;

            if(reference.isManaged()) {
                update(reference, type);
            }
            else {
                create(reference, type);
            }

            // Persist identifier of Entity reference
            builder.append(reference.getIdentifier());
        }
        else if(type.isArray() || List.class.isAssignableFrom(type)) {
            String arrDelimiter = ",";
            Class componentType = type.getComponentType();

            // Cast the references as a List
            List items = null;
            if(type.isArray())
                items = Arrays.asList((Object[]) value);
            else
                items = (List) value;

            if(items.size() > 0) {
                for(Object item: items)
                    if(item != null)
                        this.serialize(componentType, item, builder).append(arrDelimiter);

                // Remove trailing array delimiter
                builder.setLength(builder.length() - arrDelimiter.length());
            }
        }

        return builder;
    }

    private <T extends Entity> StringBuilder serialize(T entity) throws Exception {
        Class<?> actualType = entity.getClass();
        StringBuilder builder = new StringBuilder(actualType.getName());

        Field[] fields = this.getFieldsForType(actualType);
        String fieldDelimiter = FIELD_DELIMITER;
        String kvDelimiter = KV_DELIMITER;
        // Loop through all the fields and serialize
        for(Field field: fields) {
            Object value = field.get(entity);
            try {
                builder.append(fieldDelimiter);
                builder.append(field.getName()).append(kvDelimiter);
                this.serialize(field.getType(), value, builder);
            } catch(Exception e) {

            }
        }

        return builder;
    }

    private Object deserialize(Class genericType, Class type,String valueString) throws NumberFormatException, Exception {
        Object value = null;

        if(type.equals(String.class))
            value = valueString;
        else if(type.equals(Byte.TYPE) || type.equals(Byte.class))
            value = Byte.parseByte(valueString);
        else if(type.equals(Short.TYPE) || type.equals(Short.class))
            value = Short.parseShort(valueString);
        else if(type.equals(Integer.TYPE) || type.equals(Integer.class))
            value = Integer.parseInt(valueString);
        else if(type.equals(Long.TYPE) || type.equals(Long.class))
            value = Long.parseLong(valueString);
        else if(type.equals(Float.TYPE) || type.equals(Float.class))
            value = Float.parseFloat(valueString);
        else if(type.equals(Double.TYPE) || type.equals(Double.class))
            value = Double.parseDouble(valueString);
        else if(type.equals(Boolean.TYPE) || type.equals(Boolean.class))
            value = Boolean.parseBoolean(valueString);
        else if(type.equals(Character.TYPE) || type.equals(Character.class))
            value = valueString.charAt(0);
        else if(Date.class.isAssignableFrom(type))
            value = new Date(Long.parseLong(valueString));
        else if(Enum.class.isAssignableFrom(type)) {
            if(Enum.class.equals(type))
                value = Enum.valueOf(genericType, valueString);
            else
                value = Enum.valueOf(type, valueString);
        }
        else {
            if(Entity.class.isAssignableFrom(type)) {
                long id = Long.parseLong(valueString);

                // Attempts to load entity from cache and will retrieve from data file as a fallback
                synchronized(this.entityCache) {
                    value = this.getEntityFromCache(type, id);
                    if(value == null)
                        value = this.retrieveByID(id, type);
                }
            }
            else if(type.isArray() || List.class.isAssignableFrom(type)) {
                Class componentType = type.getComponentType();

                // Split the data string into the array elements
                String[] arrString = valueString.split(Pattern.quote(ARRAY_DELIMITER));
                Object[] array = null;
                if(arrString.length == 1 && arrString[0].length() == 0)
                    array = (Object[]) Array.newInstance(componentType, 0);
                else {
                    array = (Object[]) Array.newInstance(componentType, arrString.length);
                    // Loop through the array and deserialize each component
                    for(int i = 0; i < array.length; i++)
                        array[i] = deserialize(genericType, componentType,arrString[i]);
                }

                // Cast to appropriate type
                if(type.isArray())
                    value = array;
                else
                    value = new ArrayList(Arrays.asList(array));

            }
        }

        return value;
    }

    <T extends Entity> T deserialize(String entityString, boolean partial) throws NumberFormatException, IllegalArgumentException, Exception {
        String[] arrString = entityString.split(Pattern.quote(FIELD_DELIMITER));
        Class type = Class.forName(arrString[0]);
        Field[] fields = this.getFieldsForType(type);

        // Generate a mapping of key to value
        Map<String, String> kvMap = new HashMap<String, String>();
        for(int i = 1; i < arrString.length; i++) {
            String[] kvPair = arrString[i].split(Pattern.quote(KV_DELIMITER));
            kvMap.put(kvPair[0], kvPair.length == 2? kvPair[1]: "");
        }

        long id = Long.parseLong(kvMap.get("_id"));
        // Search cache for entity or create a new entity in cache
        T entity = null;
        synchronized(this.entityCache) {
            entity = (T) this.getEntityFromCache(type, id);
            if(entity == null) {
                entity = (T) this.createEntity(type, id);
                this.setEntityToCache(type, entity);
            }
        }

        // Probe for generics and retrieve the actual types whenever it contains generics. Supports up to one generic only.
        Class genericType = null;
        Type gType = type.getGenericSuperclass();
        if(gType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) gType;
            genericType = (Class) pType.getActualTypeArguments()[0];
        }
        // Loop through all fields and attempt to initialize them if they are present in kvMap
        for(Field field: fields) {
            if(kvMap.containsKey(field.getName())) {
                if(!partial || field.get(entity) == null) {
                    field.set(entity, this.deserialize(genericType, field.getType(), kvMap.get(field.getName())));
                }
            }
        }

        return entity;
    }

    private <T> Field[] getFieldsForType(Class<T> type) throws NoSuchMethodException, SecurityException {
        Field[] fields;

        if(fCache.containsKey(type)) {
            // Attempts to get the fields from cache
            fields = fCache.get(type);
        }
        else {
            // Build the cache for the fields for the specified type
            Field[] local = type.getDeclaredFields();

            Class<?> supertype = type.getSuperclass();
            if(supertype == null)
                fields = local;
            else {
                // Get fields from superclass
                Field[] inherited = getFieldsForType(supertype);

                // Store the fields in a new array
                fields = new Field[inherited.length + local.length];

                System.arraycopy(inherited, 0, fields, 0, inherited.length);
                System.arraycopy(local, 0, fields, inherited.length, local.length);

                // Sets the retrieved fields to be accessible via reflection
                for(Field field: fields)
                    field.setAccessible(true);

                // Cache the retrieved field array
                fCache.put(type, fields);
            }
        }

        return fields;
    }

    private <T extends Entity> T createEntity(Class<T> type, long id) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        T entity;

        Field idField = this.getFieldsForType(Entity.class)[0];

        // Enable no-args constructor
        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        entity = (T) constructor.newInstance();
        idField.set(entity, id);

        return entity;
    }

    private <T extends Entity> T getEntityFromCache(Class<T> type, long id) {
        T entity = null;

        if(this.entityCache.containsKey(type)) {
            Map<Long, SoftReference<Entity>> idMap = this.entityCache.get(type);

            if(idMap.containsKey(id)) {
                // Attempts to retrieve entity from soft reference
                entity = (T) idMap.get(id).get();
                // Remove soft reference from cache if it gets invalid
                if(entity == null)
                    idMap.remove(id);
            }
        }

        return entity;
    }

    private <T extends Entity> void setEntityToCache(Class<T> type , T entity) {
        Map<Long, SoftReference<Entity>> idMap;

        if(this.entityCache.containsKey(type))
            idMap = this.entityCache.get(type);
        else {
            // Creates a new map for the specific type.
            idMap = new HashMap<Long, SoftReference<Entity>>();
            this.entityCache.put(type, idMap);
        }

        // Adds a new soft reference for the entity into cache.
        idMap.put(entity.getIdentifier(), new SoftReference<Entity>(entity));
    }


}
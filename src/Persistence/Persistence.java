package Persistence;

import Model.Guest.Guest;

import java.io.*;
import java.lang.ref.SoftReference;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Pattern;

public class Persistence {
    public Persistence(){

    }

    public <T extends Entity> boolean create(T entity, Class<T> type){
        return true;
    }

    public <T extends Entity> boolean update(T entity, Class<T> type){
        return true;
    }
    public <T extends Entity> Iterable<T> search(Class<T> type){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return null;
            }
        };
    }

    public <T extends Entity> boolean delete(T entity, Class<T> type){
        return true;
    }


    public <T extends Entity> T retrieveByID(long id, Class<T> type) throws Exception {
        T entity = null;


        return entity;
    }
}
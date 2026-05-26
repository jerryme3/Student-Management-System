package com.jerme.sis.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class Repository<T> {

    public abstract void createTable();

    public abstract Optional<T> insert(T entity);
    public abstract Optional<T> findById(int id); //returns an optional entity
    public abstract Optional<T> findByEmail(String email);
    public abstract List<T> read(String query);

    public abstract boolean update(T entity);
    public abstract boolean delete(int id);
    public abstract boolean existsById(int id); //checks if an entity exists in db
    public abstract boolean existsByEmail(String email);

    protected abstract T mapRow(ResultSet rs) throws SQLException;
}

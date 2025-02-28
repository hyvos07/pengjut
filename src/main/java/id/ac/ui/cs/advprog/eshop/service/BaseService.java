package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;

public interface BaseService<T> {
  public T create(T model);

  public T findById(String modelId);

  public List<T> findAll();

  public void update(String id, T model);

  public void delete(String modelId);
}
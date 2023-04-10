package riddler.repository;

import riddler.domain.Entity;

public interface Repository<TID, E extends Entity<TID>> {
    void add(E elem);
    void delete(E elem);
    void update(E elem, TID id);
    E findById(TID id);
    Iterable<E> findAll();
}

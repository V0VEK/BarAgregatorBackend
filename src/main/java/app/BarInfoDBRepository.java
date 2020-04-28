package app;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BarInfoDBRepository extends CrudRepository<BarInfoDB, Integer> {
    public List<BarInfoDB> findByBarID(Integer barID);

    public List<BarInfoDB> findByWaiterID(Integer waiterID);
}

package app;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WaiterInfoDBRepository extends CrudRepository<WaiterInfoDB, Integer> {
    public List<WaiterInfoDB> findByWaiterID(Integer waiterID);

    public List<WaiterInfoDB> findByBarID(Integer barID);
}

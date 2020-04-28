package app;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BarProductsDBRepository extends CrudRepository<BarProductsDB, Integer> {
    public List<BarProductsDB> findByBarID(Integer barID);

    public List<BarProductsDB> findByProductID(Integer productID);
}

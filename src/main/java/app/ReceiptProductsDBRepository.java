package app;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceiptProductsDBRepository extends CrudRepository<ReceiptProductsDB, Integer> {

    public List<ReceiptProductsDB> findByUniqueReceiptID(Integer uniqueID);

    public List<ReceiptProductsDB> findByUniqueReceiptIDAndProductID(Integer uniqueID, Integer productID);

    public List<ReceiptProductsDB> findByWaiterID(Integer waiterID);
}

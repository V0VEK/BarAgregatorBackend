package app;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceiptInfoDBRepository extends CrudRepository<ReceiptInfoDB, Integer> {
    public List<ReceiptInfoDB> findByBarReceiptIDAndWaiterID(Integer barReceiptID, Integer waiterID);

    public List<ReceiptInfoDB> findByBarIDAndBarReceiptID(Integer barID, Integer barReceiptID);

    public List<ReceiptInfoDB> findByUniqueReceiptID(Integer uniqueReceiptID);

    public List<ReceiptInfoDB> findByWaiterID(Integer waiterID);
}

package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dverkask.springapp.domain.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}

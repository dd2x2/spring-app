package ru.dverkask.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dverkask.springapp.domain.Check;

public interface CheckRepository extends JpaRepository<Check, Long> {
}

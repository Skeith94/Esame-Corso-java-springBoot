package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository  extends JpaRepository<Staff,Long> {
}

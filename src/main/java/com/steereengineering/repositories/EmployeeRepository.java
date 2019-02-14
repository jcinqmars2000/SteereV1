package com.steereengineering.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.steereengineering.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query(value = "select * from employee order by firstname", nativeQuery = true)
	 public List<Employee> getOrderByFirstname();
	@Query(value = "select * from employee where resettoken = ?1", nativeQuery = true)
	 public Employee findByResetToken(String resettoken);
	 Employee findByEmail(String email);
	 @Query(value = "update employee set password = ?0  where employee_id = ?1", nativeQuery = true)
	 public void updatePassword(String password, Long userId);
}

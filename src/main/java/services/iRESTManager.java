package services;

import model.Department;
import model.Employee;

import java.sql.Connection;
import java.util.List;

public interface iRESTManager {
  List<Employee> getAllEmployees();

  List<Department> getAllDepartments();

  void addEmployee(Employee employee);

  void editEmployee(Employee employee);

  void delete(int id);
}

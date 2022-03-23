package services;
import model.Employee;
import java.util.*;
import java.util.stream.Collectors;

//Вспомогательный класс. Используется для обработки данных из базы данных.
public class DataHandler {

    //Создаем объект RESTmanager и ссылку на него, для использования REST сервиса
    //и получения данных из БД.
    RESTmanager rest= new RESTmanager();

    //Метод для выборки сотрудников по дате рождения в диапазоне от "from" до "to".
    public List<Employee> filterDate(Date from, Date to)
    {
        //Получаем список всех сотрудников через REST
        List<Employee> employeeList=rest.getAllEmployees();

        //Фильтруем полученный список, используя StreamAPI и сразу возвращаем.
        return employeeList.stream()
                //Фильтруем
                .filter(e -> e.getDate().getTime() >= from.getTime() &&  e.getDate().getTime() <= to.getTime())
                //Полученные значения собираем в новый список и сразу его возвращаем.
                .collect(Collectors.toList());
    }

    //Метод для выборки сотрудников по дате рождения с конкретной дате рождения "exactDate".
    public List<Employee> filterDate(Date exactDate)
    {
        //Получаем список всех сотрудников через REST.
        List<Employee> employeeList=rest.getAllEmployees();

        //Фильтруем полученный список, используя StreamAPI и сразу возвращаем.
        return employeeList.stream()
                //Фильтруем
                .filter(e ->e.getDate().equals(exactDate))
                //Полученные значения собираем в новый список и сразу его возвращаем.
                .collect(Collectors.toList());
    }

    //Получение средней зарплаты всех сотрудников для отдела department.
    public String AverageSalaryByDepartment(String department)
    {
        //Получаем список всех сотрудников через REST.
        List<Employee> employeeList=rest.getAllEmployees();

        //Самое простое-это получить среднее арифметическое из StreamAPI это
        //использовать метод average(). Он возвращает объект OptionalDouble
        //Создаем поток из сотрудников.
        OptionalDouble sumSalary= employeeList.stream()
                                     //Фильтруем и оставляем только тех сотрудников, которые работают в нужном департаменте.
                                     .filter(e -> e.getDepartment().equals(department))
                                     //Преобразуем поток сотрудников в поток зарплат
                                     .map(Employee::getSalary)
                                     //Требуется применения метода mapToLongчтобы воспользоваться функцией average()
                                     .mapToLong(Long::longValue)
                                     //Получаем среднюю зарплату на отдел
                                     .average();

        //Если мыполучили какое-то значение (В отделе есть хоть один сотрудник с зарплатой)
        //то возвращаем среднюю зарплату в формает String.
        if(sumSalary.isPresent())
            //Форматируем так, чтобы тыбо 2 точки после запятой.
            return String.format("%.2f", sumSalary.getAsDouble());
        else
            return "--";
    }

}

package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.text.SimpleDateFormat;
import java.util.Date;

    /*
        Создаем класс Employee для создания сотрудника.
        Поля совпадают с полями в БД. Используем lombok
        для избежания шаблонного кода.
    */

@Setter
@Getter
@ToString
@XmlRootElement    //Необходим для отправки/получения объекта в/из JSON через REST.
@NoArgsConstructor //Необходим для создания объекта из JSON через REST.
@EqualsAndHashCode
public class Employee {
    private long id;
    private String department;
    private String name;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Yekaterinburg")
    private Date date;
    private long salary;

    //Кроме REST, конструктор используется для редактирования существующего пользователя /servlet/edit
    public Employee(long id, String department, String name, Date date, long salary) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.date = date;
        this.salary = salary;
    }

    //Кроме REST, конструктор используется для создания нового пользователя /servlet/new.
    //id создается автоматически в базе данных (primary key, autoincrement).
    public Employee( String department, String name, Date date, long salary) {
        this.department = department;
        this.name = name;
        this.date = date;
        this.salary = salary;
    }


    //Метод используется для вывода даты в jsp страницах в формате "yyyy-MM-dd".
    @JsonIgnore
    public String getStringDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }
}

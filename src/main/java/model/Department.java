package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
    /*
        Создаем класс Department для создания отдела.
        Поля совпадают с полями в БД. Используем lombok
        для избежания шаблонного кода.
    */
@Getter
@Setter
@ToString
@NoArgsConstructor //Необходим для создания/отправки/получения объекта в/из JSON через REST.
@XmlRootElement    //Необходим для создания объекта из JSON через REST
public class Department {
    private long id;
    private String name;

    //Используется в основном в REST.
    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }
}

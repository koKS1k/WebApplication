
<!DOCTYPE html>
<html>

<head>
    <title>Employee	Management App</title>
    <!-- Импорт CSS -->
    <link href="../css/main-page-style.css" rel="stylesheet" type="text/css">
    <!-- Подключаем JSTL-core -->
    <%@ taglib prefix = "c"  uri ="http://java.sun.com/jsp/jstl/core" %>
</head>

<body class="body">

<!--Заголовок-->
<header>
    <div class="header">
        <p class="header_text" style="margin-left:20px"> Employee	Management App </p>
    </div>
</header>

<!-- Блок с двумя ссылками, оформленными как кнопки-->
<div>
    <!--Ссылка на получение списка всех сотрудников-->
    <a  href="<c:url value="/servlet/update">
              <c:param name="purpose" value="employees"/>
              </c:url>"
        class="main_button size_1"> Employees
    </a>
    <!--Ссылка на получение списка всех отделов-->
    <a  href="<c:url value="/servlet/update">
              <c:param name="purpose" value="departments"/>
              </c:url>"
        class="main_button size_2"> Departments
    </a>

</div>

<!--Если в applicationScope заполнен атрибут 'departmentsList' то выводим таблицу с отделами-->
<c:if test="${not empty applicationScope['departmentsList']}">
    <div class="table_div">
        <table class="table">
            <!-- Названия столбцов таблицы -->
            <thead>
            <tr>
                <th style="width: 200px">Department</th>
                <th style="width: 150px">Average Salary</th>
                <th style="width: 60px"></th>

            </tr>
            </thead>
            <tbody>
            <!-- в цикле выводим все отделы в строках-->
            <c:forEach var="department" items="${applicationScope['departmentsList']}">

            <c:set var='name' value="${department.name}" />
                <tr>
                    <!-- Выводим имя отдела-->
                    <td> <c:out value="${name}"/> </td>
                    <!-- Выводим среднюю зарплату. Она установлена как атрибут в запросе-->
                    <td> <c:out value="${requestScope[name]}"/> </td>
                    <td>
                        <!--Ссылка, оформленная под кнопку, для получения всех списка всех сотрудников отдела-->
                        <a  href="<c:url value="/servlet/departmentStaff">
                                  <c:param name="department" value="${department.name}"/>
                                  </c:url>"
                            class="btn btn_type1">
                            Staff
                        </a>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

</body>
</html>

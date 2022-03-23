<!DOCTYPE html>
<html>
<head>
    <title>Employee	Management App</title>
    <link href="../css/error-form.css" rel="stylesheet" type="text/css">

    <%@ taglib prefix = "c"  uri ="http://java.sun.com/jsp/jstl/core" %>
</head>
<header>
    <div class="header">
        <p class="header_text" style="margin-left:20px"> Employee	Management App </p>
    </div>
</header>
<body class="body">
<div class="error_form">

    <div class="error_title">
        ERROR!
    </div>

    <div class="text1">
        Uh Oh! SOMETHING WRONG!<br>
        An error has occurred. Our team 0f 5000000 engeneer is working on it. for now try this:
    </div>

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

</body>
</html>

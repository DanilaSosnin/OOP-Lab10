<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Репетиторы</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<c:if test="${empty tutors and empty param.redirected}">
    <c:redirect url="/tutors?redirected=true"/>
</c:if>
<h1>Система управления репетиторами</h1>

<!-- Форма добавления -->
<div class="form-section">
    <h2>Добавить репетитора</h2>
    <form action="tutors" method="post">
        <table class="non-border-table">
            <tr>
                <td>
                    Имя
                </td>
                <td>
                    Фамилия
                </td>
                <td>
                    Предмет
                </td>
                <td>
                    Опыт работы
                </td>
                <td>
                    Цена в час
                </td>
                <td>
                    Номер телефона
                </td>
            </tr>
            <tr>
                <td>
                    <input type="text" name="firstName" placeholder="Имя" required>
                </td>
                <td>
                    <input type="text" name="lastName" placeholder="Фамилия" required>
                </td>
                <td>
                    <input type="text" name="subject" placeholder="Предмет" required>
                </td>
                <td>
                    <input type="number" name="experience" placeholder="Стаж (в годах)" required>
                </td>
                <td>
                    <input type="number" step="0.01" name="price" placeholder="Цена услуги за час" required>
                </td>
                <td>
                    <input type="text" name="phoneNumber" placeholder="Номер телефона" required>
                </td>
            </tr>
            <tr>
                <td colspan="7">
                    <button type="submit" class="btn add-btn">Добавить репетитора</button>
                </td>
            </tr>
        </table>
    </form>
</div>

<c:if test="${not empty editTutor}">
    <div class="form-section">
        <form action="${pageContext.request.contextPath}/tutors" method="post">
            <table class="non-border-table">
                <tr>
                    <td colspan="7">
                        <h2>Редактировать запись</h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        Имя
                    </td>
                    <td>
                        Фамилия
                    </td>
                    <td>
                        Предмет
                    </td>
                    <td>
                        Опыт работы
                    </td>
                    <td>
                        Цена в час
                    </td>
                    <td>
                        Номер телефона
                    </td>
                    <td>
                        <button type="submit" class="btn add-btn">Обновить</button>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="_method" value="PUT">
                        <input type="hidden" name="id" value="${editTutor.id}">
                        <input type="text" name="firstName" value="${editTutor.firstName}" required>
                    </td>
                    <td>
                        <input type="text" name="lastName" value="${editTutor.lastName}" required>
                    </td>
                    <td>
                        <input type="text" name="subject" value="${editTutor.subject}" required>
                    </td>
                    <td>
                        <input type="number" name="experience" value="${editTutor.experience}" required>
                    </td>
                    <td>
                        <input type="number" step="0.01" name="price" value="${editTutor.price}" required>
                    </td>
                    <td>
                        <input type="text" name="phoneNumber" value="${editTutor.phoneNumber}" required>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/tutors"><button class="btn">Отмена</button></a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</c:if>

<div class="form-section">
<h2>Список репетиторов</h2>
    <table>
        <tr>
            <th class="bordered-cells thead">ID</th>
            <th class="bordered-cells thead">Имя</th>
            <th class="bordered-cells thead">Фамилия</th>
            <th class="bordered-cells thead">Предмет</th>
            <th class="bordered-cells thead">Стаж работы</th>
            <th class="bordered-cells thead">Цена</th>
            <th class="bordered-cells thead">Номер телефона</th>
            <th class="bordered-cells thead">Действие</th>
        </tr>
        <c:forEach items="${tutors}" var="tutor">
            <tr>
                <td class="bordered-cells">${tutor.id}</td>
                <td class="bordered-cells">${tutor.firstName}</td>
                <td class="bordered-cells">${tutor.lastName}</td>
                <td class="bordered-cells">${tutor.subject}</td>
                <td class="bordered-cells">${tutor.experience}</td>
                <td class="bordered-cells">${tutor.price}</td>
                <td class="bordered-cells">${tutor.phoneNumber}</td>
                <td class="bordered-cells">
                    <a href="tutors?action=edit&id=${tutor.id}"><button class="btn">Редактировать запись</button></a>
                    <form action="tutors" method="post" style="display:inline;">
                        <input type="hidden" name="_method" value="DELETE">
                        <input type="hidden" name="id" value="${tutor.id}">
                        <button type="submit" class="btn del-btn">Удалить запись</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
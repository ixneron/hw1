<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>start</title>
</head>
<body>
    <h5>Приложение запущено!</h5>
</body>
<script type="text/javascript">
    function OnLoad()
    {
        httpRequest = new XMLHttpRequest();
        httpRequest.open("GET", "C:/JAVA/ibm_sphera/profiles/AppSrv03/logs/testFileQ.log", true);

        httpRequest.send(null);
    }

</script>
</html>

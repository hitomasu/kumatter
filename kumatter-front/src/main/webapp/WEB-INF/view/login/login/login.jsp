<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;ログイン</title>
</head>
<body>
<div>ログインページ</div>
<html:errors/>
<s:form method="POST">
<table>
    <tr>
        <th>ID：</th>
        <td><html:text property="id"/></td>
    </tr>
    <tr>
        <th>パスワード：</th>
        <td><html:password property="pass"/></td>
    </tr>
    <tr>
        <th colspan="2"><s:submit property="login" value="ログイン" /></th>
    </tr>
</table>
</s:form>
</body>
</html>
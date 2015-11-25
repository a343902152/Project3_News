@@ -0,0 +1,39 @@
<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 2015/11/24
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
</head>
<body>
<div id="content"></div>

<script type="text/javascript" src="JS/jquery-1.9.1.js"></script>
<!--<script type="text/javascript" src="JS/jquery.mobile-1.4.4.js"></script>-->
<script type="text/javascript">
  $(document).ready(function() {
    $.ajax({
      url:"controller",
      type:"GET",
      data:{
        "action":"getNewsById",
        "newsId":localStorage.newsId
      },
      success:function(data){
        $("#content").html(data);
      },
      error: function(){
        alert("请求失败，请刷新重试");
      }
    });
  });
</script>

</body>
</html>
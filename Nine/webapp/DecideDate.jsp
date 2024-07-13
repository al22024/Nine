<!--
  File Name: DecideDate.jsp
  Version: V1.0
  Designer: 菅原幹太
  Date: 07/12
  Purpose: 	Schedule_Adjust.javaより送られてきた日程候補群を提示し選択させる
  			選択ができていなければエラー表示を出し選択ユーザーの選択を待つ
  			選ばれた結果をSchedule_Adjust.javaに渡す
-->
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule Confirmation</title>
    <style>
        .container {
            display: flex;
            justify-content: space-between;
            padding: 20px;
        }
        .box {
            border: 1px solid black;
            padding: 20px;
            width: 45%;
        }
        .date-box {
            border: 1px solid green;
            padding: 10px;
            margin-top: 10px;
        }
        .hidden {
            display: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="box">
            <h2>予定候補</h2>
            <div class="date-box">
                <% 
                    List<LocalDate> freedays = (List<LocalDate>) request.getAttribute("freedays");
                    if (freedays != null && !freedays.isEmpty()) {
                        for (LocalDate freeDay : freedays) {
                %>
                    <label>
                        <input type="radio" name="selectedDate" value="<%= freeDay %>" id="date_<%= freeDay %>" onchange="updateSelectedDate('<%= freeDay %>')">
                        <%= freeDay %>
                    </label><br>
                <%
                        }
                    } else {
                %>
                    <p>No available dates found.</p>
                <%
                    }
                %>
            </div>
        </div>
        <div class="box">
            <form action="${pageContext.request.contextPath}/servlet/Schedule_Adjust/" method="post" id="scheduleForm" onsubmit="return validateForm()">
                <div class="date-box">
                    <p id="selectDateMessage">日程を選択してください。</p>
                    <!-- 選択された日付を送信する隠しフィールド -->
                    <input type="txt" name="selectedDate" id="selectedDateInput">
                </div>
                <button type="submit">決定ボタン</button>
            </form>
            <div id="error-message" class="hidden" style="color: red;">日程を選択してください。</div>
        </div>
    </div>
    
    <script>
        // フォームのバリデーション
        function validateForm() {
            var selectedDate = document.getElementById('selectedDateInput').value;
            if (selectedDate === '') {
                document.getElementById('error-message').classList.remove('hidden');
                return false;
            }
            return true;
        }
    
        // 選択された日程を表示する処理
        function updateSelectedDate(date) {
            document.getElementById('selectedDateInput').value = date; // 選択された日程を隠しフィールドに設定
            document.getElementById('selectDateMessage').textContent = "以下の日程でよろしいですか？"; // 日程を選択してくださいメッセージを更新
            document.getElementById('error-message').classList.add('hidden'); // エラーメッセージを隠す
        }
    </script>
    
</body>
</html>

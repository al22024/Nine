/*******************************************************************
*** File Name : AccessTokenServlet.java
*** Version : V1.2
*** Designer : 村田 悠真
*** Date : 2024/07/12
*** Purpose : accessTokenをPostリクエストから受信，nickName登録画面に遷移する
***
*******************************************************************/
/*
*** Revision :
*** V1.0 : 村田悠真, 2024.06.27
*** V1.1 : 村田悠真, 2024.07.02 doGet
*** V1.2 : 村田悠真，2024.07.12 doPost
*/

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* accessTokenを取得，DB登録，nickName登録画面への遷移を行うクラス */
public class AccessTokenServlet extends HttpServlet {

	/* accessTokenを含んだPostリクエストを受信するメソッド */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		// Postリクエストから取得したaccessToken
		String accessToken = request.getParameter("accessToken");
		String nickName = "";

		try {
			/* ユーザID，email，accessTokenをDBに登録する */
			UserInfoAccess userInfoAccess = new UserInfoAccess();
			int userID = userInfoAccess.userInfoReg(accessToken);
			/* 今後のDBアクセスで利用するため，userIDをセッションに格納 */
			HttpSession session = request.getSession();
			session.setAttribute("userID", userID);

			nickName = userInfoAccess.getNickName(userID);
		}
		/* DBアクセスが失敗した時の例外処理 */
		catch (Exception e) {
			
			// エラー画面のURL
			String errorUrl = "/Nine/loginError.html";
			response.sendRedirect(errorUrl);
		}
		
		// nickName登録画面のURL
		String nickNameUrl = "/Nine/nickName.html";
		/* 登録済の場合URLにnickNameパラメータを加えて送信 */
		if (nickName != "") {
			nickNameUrl = nickNameUrl + "?nickName=" + URLEncoder.encode(nickName, "UTF-8");
		}

		response.sendRedirect(nickNameUrl);
	}
}
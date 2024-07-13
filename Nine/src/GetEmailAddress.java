/*******************************************************************
*** File Name : CalendarInfoReg.java
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/07/03
*** Purpose : accessTokenを用いてGoogleCalendarに予定の入っている日を取得する
***
*******************************************************************/
/*
*** Revision :
*** V1.0 : 村田悠真, 2024.06.27
*** V1.1 : 村田悠真, 2024.07.02 userInfoReg, getEmailAddress
*** V1.2 : 村田悠真, 2024.07.03 getCalenderInfo
*** V1.3 : 村田悠真，2024.07.03 userInfoReg, getNickName, nickNameReg
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEmailAddress {
	/* GoogleAPIを用いてemailを取得する関数 */
	public static String getEmailAddress(String accessToken) throws Exception {
		// emailを格納する
		String email = "";
		// GmailAddressを取得するAPIのURL
		String url = "https://www.googleapis.com/oauth2/v2/userinfo";
		url = url + "?access_token=" + URLEncoder.encode(accessToken, "UTF-8");

		/* APIから値の取得 */
		HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
		conn.setRequestMethod("GET");

		// レスポンスを格納する
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line;

		// レスポンスからemailを取得するためのパターン
		Pattern emailPattern = Pattern.compile("\"email\"\\s*:\\s*\"([^\"]+)\"");

		/* レスポンスからemailを取得 */
		while ((line = br.readLine()) != null) {
			Matcher matcher = emailPattern.matcher(line);
			if (matcher.find()) {
				email = matcher.group(1);
				break;
			}
		}
		br.close();
		conn.disconnect();
		return email;
	}
}

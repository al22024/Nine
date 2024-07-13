/*******************************************************************
*** File Name : UserInfoAccess.java
*** Version : V1.3
*** Designer : 村田 悠真
*** Date : 2024/07/12
*** Purpose : ログイン時，userID，email，accessTokenのDBアクセスメソッドを呼び出す
***
*******************************************************************/
/*
*** Revision :
*** V1.0 : 村田悠真, 2024.06.27
*** V1.1 : 村田悠真, 2024.07.02 userInfoReg, getEmailAddress
*** V1.2 : 村田悠真, 2024.07.03 getCalenderInfo
*** V1.3 : 村田悠真，2024.07.12 userInfoReg, getNickName, nickNameReg
*/

import dbtest.UserInfo;

/* ログイン時，userID，email，accessTokenのDB登録を行うクラス */
public class UserInfoAccess {
	/* userID，email，accessTokenのDB登録を行うメソッド */
	public int userInfoReg(String accessToken) throws Exception{
		int userID = -1;
		
		// emailを格納する
		String email = GetEmailAddress.getEmailAddress(accessToken);

		String calendarInfo = GetCalendarInfo.getCalenderInfo(accessToken);
		
		//emailでUserID DB問い合わせ
		//DBプログラムが完成後，要更新
		UserInfo userInfo = new UserInfo();
		String userIdStr = userInfo.getUserInfo("UserID", "Email", email);

		//登録済:userID，accessToken, calenderInfo DB更新
		if (userIdStr != null) {
			userID = Integer.parseInt(userIdStr);
			userInfo.updateUserInfo(userID, "AuthToken", accessToken);
			userInfo.updateUserInfo(userID, "CalendarInfo", calendarInfo);
		}

		//未登録:最大のUserID DB問い合わせ
		//UserID割り当て
		//UserID，email，accessToken DB挿入
		else {
			userID = userInfo.getMaxID() + 1;
			userInfo.userID = userID;
			userInfo.authToken = accessToken;
			userInfo.email = email;
			userInfo.calendarInfo = calendarInfo;
			userInfo.setUserInfo();
		}
		return userID;
	}

	public String getNickName(int userID) {
		// userIDでNickName問い合わせ
		// DBに登録済のニックネーム　未登録の場合は""
		UserInfo userInfo = new UserInfo();
		String nickName = userInfo.getUserInfo("DisplayName", "UserId", String.valueOf(userID));
		if (nickName == null) {
			nickName = "";
		}
		return nickName;
	}
	

	public void nickNameReg(int userID, String nickName)  throws Exception{	
        //userIDでNickName更新
		UserInfo userInfo = new UserInfo();
		userInfo.updateUserInfo(userID, "DisplayName", nickName);
	}
}

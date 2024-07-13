/* ページ読み込み時，URLから取得したaccessTokenを送信する処理 */
window.onload = function() {
	
	// 表示中のページのURLからクエリパラメータを取得
	const urlParams = new URLSearchParams(window.location.search);

	// URLから取得したパラメータ
	const userID = urlParams.get('userID');
	const displayName = urlParams.get('displayName');

	sessionStorage.setItem('userID', userID);
	sessionStorage.setItem('displayName', displayName);

	window.location.replace("/Nine/Home.html");
	
}

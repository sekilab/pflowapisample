package jp.ac.ut.csis.pflow.webapi.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 動線解析WebAPIの認証用サンプル
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public abstract class PFlowSample_Auth {
	
	/** 動線解析PFWEBAPIのURL */	public static final String API_URL = System.getProperty("pflow.webapi.url",
																						"http://pflow-api.csis.u-tokyo.ac.jp/webapi/");

	// ID用
	/** cookie 用			*/	private String cookie;
	/** session ID 			*/	private String sessionID;	
	
	/**
	 * ユーザIDとパスワードを指定してセッションを作成
	 * @param userid ユーザID
	 * @param password パスワード
	 * @return セッション作成完了ならば1、それ以外はすべてエラー(エラーコードは仕様書参照)
	 */
	public int createSession(String userid, String password) {
		// セッション作成済み
		if( isAuthed() ) { return -1; }
		try {
			// WebAPI URLのセット
			URL url = new URL(API_URL + "CreateSession");
			HttpURLConnection con = HttpURLConnection.class.cast(url.openConnection());
			
			// POST送信
			con.setDoOutput(true);
			PrintWriter out = new PrintWriter(con.getOutputStream());
			out.print("UserID="+ userid + "&" + "Password=" + password);
			out.close();
			
			// レスポンスの処理
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line_tokens[] = in.readLine().split(",");
			in.close();
			con.disconnect();
			
			// セッションID取得できず
			if( line_tokens.length == 1 ) { return Integer.parseInt(line_tokens[0]); } 
			
			// ログイン完了:セッションIDとクッキー情報を保持
			sessionID = line_tokens[1];
			StringBuffer buf = new StringBuffer();
			List<String> cookies = con.getHeaderFields().get("Set-Cookie");
			for(String c : cookies) { buf.append(c).append(";"); }
			cookie = buf.substring(0, buf.length()-1);
			
			// ステータスコードを返す
			return Integer.parseInt(line_tokens[0]);
		}
		catch(IOException exp) { exp.printStackTrace(); return -1; }
	}
	
	/**
	 * 取得しているセッションを破棄します
	 * @return セッション破棄完了ならば1、それ以外はすべてエラー(エラーコードは仕様書参照)
	 */
	public int destroySession() {
		// セッション作成していない場合
		if( !isAuthed() ) { return -1; }
		
		try {
			// WebAPI URLのセット
			URL url = new URL(API_URL + "DestroySession");
			HttpURLConnection con = HttpURLConnection.class.cast(url.openConnection());
			
			// POST送信の有効化
			con.setDoOutput(true);
			con.setRequestProperty("Cookie", cookie);
		
			PrintWriter out = new PrintWriter(con.getOutputStream());
			out.print("SessionID=" + sessionID);
			out.close();

			// レスポンスを取得
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line_tokens = in.readLine();
			in.close();
			con.disconnect();
			
			// 初期化
			sessionID = null;
			cookie    = null;
			// ステータス値を返す
			return Integer.parseInt(line_tokens);
		}
		catch(IOException exp) { exp.printStackTrace();  return -1; }
	}
	
	/**
	 * WebAPIパラメータを指定して、HTTP接続を開く
	 * @param parameters WebAPIパラメータ
	 * @return HTTP接続。CreateSessionしていない場合はnull
	 * @throws IOException IO例外
	 */
	public HttpURLConnection openHttpConnection(Map<String, Object> parameters) throws IOException {
		// 認証済み確認
		if( !isAuthed() ) { return null; }
		
		// WebAPI URL作成
		URL url = new URL(API_URL + getAPIName());
		HttpURLConnection con = HttpURLConnection.class.cast(url.openConnection());

		// POST送信の有効化
		con.setDoOutput(true);
		con.setRequestProperty("Cookie", getCookie());
		
		// パラメータ作成
		StringBuffer buf = new StringBuffer();
		for(String key : parameters.keySet()) {
			String v = parameters.get(key).toString();
			String val = URLEncoder.encode(v, "SJIS");
			buf.append(key).append('=').append(val).append('&');
		}
		PrintWriter out = new PrintWriter(con.getOutputStream());
		out.print(	buf.substring(0, buf.length()-1));
		out.close();
		
		return con;
	}
	
	/**
	 * 認証が完了し、セッションIDが取得できているかどうかを判定
	 * @return 認証済みであればtrue、そうでなければfalse
	 */
	protected boolean isAuthed() {
		return sessionID != null && cookie != null;
	}
	
	/**
	 * セッションIDを取得
	 * @return セッションID
	 */
	protected String getSessionID() {
		return (sessionID == null) ? "" : sessionID;
	}
	
	/**
	 * クッキー情報を取得
	 * @return クッキー文字列
	 */
	protected String getCookie() {
		return (cookie == null) ? "" : cookie;
	}
	
	/**
	 * 各APIの実行用
	 * @param parameter パラメータ
	 * @return ステータスコード
	 */
	public abstract int exec(Map<String,Object> parameter);
	
	/**
	 * WebAPIの名称を取得
	 * @return WebAPI名称
	 */
	public abstract String getAPIName();
}

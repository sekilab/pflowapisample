package jp.ac.ut.csis.pflow.webapi.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>GetMixedRouteTimeを使うサンプル</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetMixedRouteTime</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetMixedRouteTime USERID USERPW</dd>
 * <dd>※　USERID, USERPDはご自身の登録されものをご利用ください</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2012-04-12
 */
public class PFlowSample_GetMixedRouteTime extends PFlowSample_Auth {
	/**
	 * サンプルの実行
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) {
		// ID/PW
		String userid = args[0];
		String passwd = args[1];
		// パラメータの作成
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2               ); // 座標単位　1:度分秒表記、2:度表記
		parameter.put("StartLatitude",  35.90084483     ); // 始点緯度　東大柏キャンパス
		parameter.put("StartLongitude", 139.93689537    ); // 始点経度
		parameter.put("GoalLatitude",   35.7078869      ); // 終点緯度　東大本郷キャンパス
		parameter.put("GoalLongitude",  139.76246595    ); // 終点経度
//		parameter.put("WayLatitude",    0               ); // 緯度(任意）
//		parameter.put("WayLongitude",   0               ); // 経度(任意）
		
		PFlowSample_GetMixedRouteTime sample = new PFlowSample_GetMixedRouteTime();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetMixedRouteTimeの実行：1が表示されれば成功
		System.out.println("GetMixedRouteTime : " + sample.exec(parameter));
		// 結果の出力
		for(String line[] : sample.getResult()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		// セッションIDを破棄:1が表示されれば成功
		System.out.println("DestroySession : " + sample.destroySession());
	}
	
	
	// 出力結果用
	/** response data	*/	private List<String[]>  result = new ArrayList<String[]>();
	
	/**
	 * 検索結果を取得
	 * @return 検索結果
	 */
	public List<String[]> getResult() {
		return result;
	}

	/**
	 * API名称を取得
	 * @return API名称
	 */
	public String getAPIName() {
		return "GetMixedRouteTime";
	}
	
	/**
	 * GetMixedRouteの実行
	 * @param parameters GetMixedRouteのパラメータ
	 * @return ステータスコード
	 */
	public int exec( Map<String, Object> parameters ) {
		// 初期化
		result.clear();
		// セッション作成していない場合
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP接続
			HttpURLConnection con = openHttpConnection(parameters);
			// レスポンスを取得
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "SJIS"));
			
			// 1行目：ステータスコード
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) return status;
			
			// 2行目～：該当データ
			String line = null;
			while( (line = in.readLine()) != null )	{
				result.add(line.split(","));
			}
			in.close();
			con.disconnect();
			
			// ステータス値を返す
			return status;
		}
		catch(IOException exp) {
			exp.printStackTrace(); 
			return -1;
		}
	}
}

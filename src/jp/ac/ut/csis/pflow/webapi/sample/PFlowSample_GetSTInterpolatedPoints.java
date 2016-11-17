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
 * <b>GetSTInterpolatedPointsのクライアントサンプル</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_STInterpolatedPoints</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_STInterpolatedPoints USERID USERPW</dd>
 * <dd>※　USERID, USERPDはご自身の登録されものをご利用ください</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetSTInterpolatedPoints extends PFlowSample_Auth {
	/**
	 * サンプルの実行(ユーザIDとパスワードの指定が必要）
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) {
		// ID/PW
		String userid = args[0];
		String passwd = args[1];
		
		// パラメータの作成
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2               ); // 座標単位　1:度分秒表記、2:度表記
		parameter.put("DtStart",        "20120622090000"); // 出発日時(2010-07-15　10:00:00)
		parameter.put("DtEnd",          "20120622091500"); // 到着日時(2010-07-15　12:00:00)
		parameter.put("NetworkOption",  1               ); // 経路座標指定フラグ　1:道路＋鉄道複合検索、2:道路検索、3:鉄道検索、4:任意ネットワーク
		parameter.put("StartLatitude",  35.90084483     ); // 始点緯度　東大柏キャンパス
		parameter.put("StartLongitude", 139.93689537    ); // 始点経度
		parameter.put("GoalLatitude",   35.7078869      ); // 終点緯度　東大本郷キャンパス
		parameter.put("GoalLongitude",  139.76246595    ); // 終点経度
//		parameter.put("Resolution",     300             ); // 時間解像度（任意）
//		parameter.put("SubPointOption", 1               ); // 補間点有無フラグ　1:有り、2:無し
		
		// サンプルインスタンスの生成
		PFlowSample_GetSTInterpolatedPoints sample = new PFlowSample_GetSTInterpolatedPoints();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetSTInterpolatedPointsの実行：1が表示されれば成功
		System.out.println("GetSTInterpolatedPoints : " + sample.exec(parameter));
		// 結果の出力
		System.out.println("time,lon,lat");
		for(String line[] : sample.getResult()) {
			StringBuffer buf = new StringBuffer();
			for(String str : line) buf.append(",").append(str);
			System.out.println(buf.substring(1));
		}
		// セッションIDを破棄:1が表示されれば成功
		System.out.println("DestroySession : " + sample.destroySession());
	}
	
	
	
	// 出力結果用
	private List<String[]>  result = new ArrayList<String[]>();
	
	/**
	 * 結果配列
	 * @return 結果
	 */
	public List<String[]> getResult() {
		return result;
	}
	
	/**
	 * WebAPIの名称を取得
	 * @return WebAPI名称
	 */
	public String getAPIName() {
		return "GetSTInterpolatedPoints";
	}
	
	/**
	 * GetSTInterpolatedPointsの実行
	 * @param parameters GetSTInterpolatedPointsのパラメータ
	 * @return ステータスコード
	 */
	public int exec( Map<String, Object> parameters ) {
		// 初期化
		result.clear();
		
		// セッション作成（ログイン）していない場合
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP接続
			HttpURLConnection con = openHttpConnection(parameters);
			// レスポンスを取得
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			// 1行目：ステータスコード
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) { return status; }

			// 2行目～：該当データ
			String line = null;
			while( (line = in.readLine()) != null ) {
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

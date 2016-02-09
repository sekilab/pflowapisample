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
 * <b>GetFlowDataのクライアントサンプル</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetFlowData</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetFlowData USERID USERPW</dd>
 * <dd>※　USERID, USERPDはご自身の登録されものをご利用ください</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetFlowData extends PFlowSample_Auth {
	/**
	 * サンプルの実行
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) throws Exception {
		// ID/PW
		String userid = args[0];
		String passwd = args[1];
		
		// パラメータの作成
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("ResultTypeCode",     1           ); // 検索結果取得方法　1:動的、2:静的
		parameter.put("ResultMaxLine",      1000000     ); // 取得するデータ行数
		parameter.put("ResearchID",         "98TKY"     ); // 調査ID （時空間データ提供サービスの申請で取得）
		parameter.put("PersonID",           "356953"       ); // パーソンID
		parameter.put("StartDate",          "19981001"  ); // 開始日（調査IDに依存して変動）
		parameter.put("StartTime",          "0730"      ); // 開始時刻
		parameter.put("GoalDate",           "19981001"  ); // 終了日
		parameter.put("GoalTime",           "0830"      ); // 終了時刻
		parameter.put("UnitTypeCode",       2           ); // 座標単位　1:度分秒、2:度表記
		
		// サンプルインスタンスの生成
		PFlowSample_GetFlowData sample = new PFlowSample_GetFlowData();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetFlowDataの実行：1が表示されれば成功
		System.out.println("GetFlowData : " + sample.exec(parameter));
		// 結果の出力
		for(String line[] : sample.getResult()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		for(String line[] : sample.getDuration()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		// セッションIDを破棄:1が表示されれば成功
		System.out.println("DestroySession : " + sample.destroySession());
	}

	
	
	// 出力結果用
	/** response data	*/	List<String[]>  result = new ArrayList<String[]>();		// データ
	/** response time	*/	List<String[]>	duration = new ArrayList<String[]>();	// 処理時間
	
	
	/**
	 * 検索結果を取得
	 * @return 検索結果
	 */
	public List<String[]> getResult() {
		return result;
	}
	
	/**
	 * 処理時間情報を取得
	 * @return 処理時間情報
	 */
	public List<String[]> getDuration() {
		return duration;
	}
	
	/**
	 * API名称の取得
	 * @return API名称
	 */
	public String getAPIName() {
		return "GetFlowData";
	}
	
	/**
	 * GetFlowDataの実行
	 * @param parameters GetFlowDataのパラメータ
	 * @return ステータスコード
	 */
	public int exec( Map<String, Object> parameters ) {
		// 初期化
		result.clear();
		duration.clear();
		// セッション作成していない場合
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP接続
			HttpURLConnection con = openHttpConnection(parameters);
			// レスポンスを取得
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			// 1行目：ステータスコード
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) return status;
			
			// 2行目～：該当データ
			String line = null;
			while( (line = in.readLine()) != null ) {
				if( line.isEmpty() ) break;
				result.add(line.split(","));
			}
			// 処理時間データ
			while( (line = in.readLine()) != null ) {
				duration.add(line.split(","));
			}
			in.close();
			con.disconnect();
			
			// ステータス値を返す
			return status;
		}
		catch(IOException exp){
			exp.printStackTrace(); 
			return -1;
		}
	}
}

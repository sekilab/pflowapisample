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
 * <b>GetDistributionDataのクライアントサンプル</b><br/>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetDistributionData</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetDistributionData USERID USERPW</dd>
 * <dd>※　USERID, USERPDはご自身の登録されものをご利用ください</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetDistributionData extends PFlowSample_Auth {
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
		parameter.put("ResultTypeCode",             1           ); // 検索結果取得方法　1:動的、2:静的
		parameter.put("ResultMaxLine",              1000000     ); // 取得するデータ行数
		parameter.put("UnitTypeCode",               2           ); // 座標単位　1:度分秒、2:度表記
		parameter.put("ResearchID",                 "XXXXX"     ); // 調査ID （データごとに異なるため、仕様書を確認）
		parameter.put("GeoOptionCode",              1           ); // 1：空間指定を行なわい	2：空間指定を行 う（中心座標指 定）　３：空間指定を行う（左上、右下 座標指定）
		// for GeoOptionCode=2
		parameter.put("CenterLongitude",            139.76246595); // 中心経度
		parameter.put("CenterLatitude",	            35.7078869  ); // 中心緯度
		parameter.put("DistanceTypeCode",           1           ); // 距離単位　1:km、2:m
		parameter.put("DistanceLong",               1           ); // 縦距離
		parameter.put("DistanceWide",               1           ); // 横距離
		// for GeoOptionCode=3
		parameter.put("LeftTopCornerLongitude",	    139.75246595); // 左上経度
		parameter.put("LeftTopCornerLatitude",	    35.7178869  ); // 左上緯度
		parameter.put("RightBottomCornerLongitude",	139.77246595); // 右下経度
		parameter.put("RightBottomCornerLatitude",  35.6978869  ); // 右下緯度
		// other parameters
		parameter.put("StartDate",                  "19981001"  ); // 日付（調査IDに依存して変動する。仕様書を確認）
		parameter.put("StartTime",                  "0900"      ); // 時刻
		parameter.put("TransportOptionCode",        1           ); // 交通手段指定　1:しない、2:する
		parameter.put("TransportCode",              "1,2,3,4,5" ); // 交通手段コード（調査IDに依存して変動）
		parameter.put("SexOptionCode",              1           ); // 性別指定　1:しない、2:する
		parameter.put("SexCode",                    "1,2"       ); // 性別　1:男、2:女
		parameter.put("AgeOptionCode",              1           ); // 年齢指定　1:しない、2:する
		parameter.put("AgeCode",                    "4,5,6,7"   ); // 年齢コード（調査IDに依存して変動）
		
		// サンプルインスタンスの生成
		PFlowSample_GetDistributionData sample = new PFlowSample_GetDistributionData();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetDistributionDataの実行：1が表示されれば成功
		System.out.println("GetDistributionData : " + sample.exec(parameter));
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
	/** data 			*/	List<String[]>  result = new ArrayList<String[]>();		// データ
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
		return "GetDistributionData";
	}
	
	/**
	 * GetDistributionDataの実行
	 * @param parameters GetDistributionDataのパラメータ
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
			if( status != 1 ) { return status; }
			
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

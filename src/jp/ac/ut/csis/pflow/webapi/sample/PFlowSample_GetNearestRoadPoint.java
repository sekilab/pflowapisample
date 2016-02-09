package jp.ac.ut.csis.pflow.webapi.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>GetNearestRoadPointのクライアントサンプル</b>
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
public class PFlowSample_GetNearestRoadPoint extends PFlowSample_Auth {
	/**
	 * サンプルの実行
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) {
		// ID/PWを設定
		String userid = args[0];
		String passwd = args[1];
		
		// パラメータを生成
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2            ); // 座標単位　1:度分秒表記、2:度表記
		parameter.put("PosLongitude",	139.93689537 ); // 経度
		parameter.put("PosLatitude",	35.90084483  ); // 緯度
		// parameter.put("RoadKindCode",	9);		// 道路種別 任意
		// parameter.put("RoadNo",			0);		// 道路番号 任意
		
		// サンプルインスタンスの生成
		PFlowSample_GetNearestRoadPoint sample = new PFlowSample_GetNearestRoadPoint();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));

		// GetSTInterpolatedPointsの実行：1が表示されれば成功
		System.out.println("GetNearestRoadPoint : " + sample.exec(parameter));
		// 結果の出力
		System.out.println(" roadKindCode : " + sample.getRoadKindCode()   );
		System.out.println(" roadNo       : " + sample.getRoadNo()         );
		System.out.println(" meshCode     : " + sample.getMeshCode()       );
		System.out.println(" longitude    : " + sample.getMatchingLongitude());
		System.out.println(" latitude     : " + sample.getMatchingLatitude());

		// セッションIDを破棄:1が表示されれば成功
		System.out.println("DestroySession : " + sample.destroySession());
	}
	
	
	
	// 取得結果用
	/** road type	*/	int roadKindCode = 0;
	/** road no		*/	int roadNo = 0;
	/** mesh code	*/	String meshCode;
	/** result lon	*/	double matchingLongitude;
	/** result lat	*/	double matchingLatitude;

	
	/**
	 * WebAPIの名称を取得
	 * @return WebAPI名称
	 */
	public String getAPIName() {
		return "GetNearestRoadPoint";
	}
	
	/**
	 * GetNearestRoadPointの実行
	 * @param parameters GetNearestRoadPointのパラメータ
	 * @return ステータスコード
	 */
	public int exec( Map<String, Object> parameters ) {
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
			
			// 2行目：1.道路種別コード,2.道路番号,3.2次メッシュコード
			String tokens[] = in.readLine().split(",");
			roadKindCode= Integer.parseInt(tokens[0]);
			roadNo 		= Integer.parseInt(tokens[1]);
			meshCode	= tokens[2];
			
			// 3行目：1.近傍経度, 2.近傍緯度
			tokens = in.readLine().split(",");
			matchingLongitude = Double.parseDouble(tokens[0]);
			matchingLatitude = Double.parseDouble(tokens[1]);
			
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
	
	/**
	 * マッチした路線種別のコードを取得
	 * @return 路線種別コード
	 */
	public int getRoadKindCode() {
		return roadKindCode;
	}
	
	/**
	 * マッチした路線番号を取得
	 * @return 路線番号
	 */
	public int getRoadNo() {
		return roadNo;
	}
	
	/**
	 * マッチした位置の2次メッシュコードを取得
	 * @return 2次メッシュコード
	 */
	public String getMeshCode() {
		return meshCode;
	}
	
	/**
	 * 近傍座標の経度
	 * @return 経度
	 */
	public double getMatchingLongitude() {
		return matchingLongitude;
	}
	
	/**
	 * 近傍座標の緯度
	 * @return 緯度
	 */
	public double getMatchingLatitude() {
		return matchingLatitude;
	}
}

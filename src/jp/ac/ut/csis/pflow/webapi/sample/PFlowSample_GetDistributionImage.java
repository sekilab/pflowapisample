package jp.ac.ut.csis.pflow.webapi.sample;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * <p>GetDistributionImageのクライアントサンプル</p>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetDistributionImage</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetDistributionImage USERID USERPW</dd>
 * <dd>※　USERID, USERPDはご自身の登録されものをご利用ください</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetDistributionImage extends PFlowSample_Auth {
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
		parameter.put("ResearchID",         "XXXXX"     ); // 調査ID （時空間データ提供サービスの申請で取得）
		parameter.put("UnitTypeCode",       2           ); // 座標単位　1:度分秒、2:度表記
		parameter.put("CenterLongitude",	139.76246595); // 中心経度
		parameter.put("CenterLatitude",		35.7078869  ); // 中心緯度
		parameter.put("DistanceTypeCode",   1           ); // 距離単位　1:km、2:m
		parameter.put("DistanceLong",       1           ); // 縦距離
		parameter.put("DistanceWide",       1           ); // 横距離
		parameter.put("AppDate",            "19980101"  ); // 日付（調査IDに依存して変動）
		parameter.put("AppTime",            "0800"      ); // 時刻
		parameter.put("TransportOptionCode",1           ); // 交通手段指定　1:しない、2:する
		parameter.put("TransportCode",      "1,2,3,4,5" ); // 交通手段コード（調査IDに依存して変動）
		parameter.put("SexOptionCode",      1           ); // 性別指定　1:しない、2:する
		parameter.put("SexCode",            "1,2"       ); // 性別　1:男、2:女
		parameter.put("AgeOptionCode",      1           ); // 年齢指定　1:しない、2:する
		parameter.put("AgeCode",            "4,5,6,7"   ); // 年齢コード（調査IDに依存して変動）
		
		// サンプルインスタンスの生成
		PFlowSample_GetDistributionImage sample = new PFlowSample_GetDistributionImage();
		// セッションの生成（ログイン）：1が表示されれば成功
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetDistributionImageの実行：1が表示されれば成功
		System.out.println("GetDistributionImage : " + sample.exec(parameter));
		// 結果の出力
		Image img = sample.getImage();
		if( img != null ) {
			JFrame frame = new JFrame();
			frame.setContentPane(new JLabel(new ImageIcon(img)));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		}
		// セッションIDを破棄:1が表示されれば成功
		System.out.println("DestroySession : " + sample.destroySession());
	}


	// 結果画像用
	/** response image	*/	Image image = null;
	
	
	/**
	 * 結果画像取得用
	 * @return 取得画像
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * API名称の取得
	 * @return API名称
	 */
	public String getAPIName() {
		return "GetDistributionImage";
	}
	
	/**
	 * GetDistributionImageの実行
	 * @param parameters GetDistributionImageのパラメータ
	 * @return ステータスコード
	 */
	public int exec( Map<String, Object> parameters ) {
		// 初期化
		image = null;
		// セッション作成していない場合
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP接続
			HttpURLConnection con = openHttpConnection(parameters);
			
			int status = -1;
			// エラー
			if( con.getHeaderField("Content-Type").equals("text/plain") ) {
				image = null;
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				status = Integer.parseInt(in.readLine());
				in.close();
			}
			// 正常
			else {
				image = ImageIO.read(con.getInputStream());
				status = 1;
			}
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

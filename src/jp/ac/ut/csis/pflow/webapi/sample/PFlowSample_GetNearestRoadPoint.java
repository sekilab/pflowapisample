package jp.ac.ut.csis.pflow.webapi.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>GetNearestRoadPoint�̃N���C�A���g�T���v��</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_STInterpolatedPoints</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_STInterpolatedPoints USERID USERPW</dd>
 * <dd>���@USERID, USERPD�͂����g�̓o�^������̂������p��������</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetNearestRoadPoint extends PFlowSample_Auth {
	/**
	 * �T���v���̎��s
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) {
		// ID/PW��ݒ�
		String userid = args[0];
		String passwd = args[1];
		
		// �p�����[�^�𐶐�
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2            ); // ���W�P�ʁ@1:�x���b�\�L�A2:�x�\�L
		parameter.put("PosLongitude",	139.93689537 ); // �o�x
		parameter.put("PosLatitude",	35.90084483  ); // �ܓx
		// parameter.put("RoadKindCode",	9);		// ���H��� �C��
		// parameter.put("RoadNo",			0);		// ���H�ԍ� �C��
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetNearestRoadPoint sample = new PFlowSample_GetNearestRoadPoint();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));

		// GetSTInterpolatedPoints�̎��s�F1���\�������ΐ���
		System.out.println("GetNearestRoadPoint : " + sample.exec(parameter));
		// ���ʂ̏o��
		System.out.println(" roadKindCode : " + sample.getRoadKindCode()   );
		System.out.println(" roadNo       : " + sample.getRoadNo()         );
		System.out.println(" meshCode     : " + sample.getMeshCode()       );
		System.out.println(" longitude    : " + sample.getMatchingLongitude());
		System.out.println(" latitude     : " + sample.getMatchingLatitude());

		// �Z�b�V����ID��j��:1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroySession());
	}
	
	
	
	// �擾���ʗp
	/** road type	*/	int roadKindCode = 0;
	/** road no		*/	int roadNo = 0;
	/** mesh code	*/	String meshCode;
	/** result lon	*/	double matchingLongitude;
	/** result lat	*/	double matchingLatitude;

	
	/**
	 * WebAPI�̖��̂��擾
	 * @return WebAPI����
	 */
	public String getAPIName() {
		return "GetNearestRoadPoint";
	}
	
	/**
	 * GetNearestRoadPoint�̎��s
	 * @param parameters GetNearestRoadPoint�̃p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public int exec( Map<String, Object> parameters ) {
		// �Z�b�V�����쐬���Ă��Ȃ��ꍇ
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP�ڑ�
			HttpURLConnection con = openHttpConnection(parameters);
			// ���X�|���X���擾
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			// 1�s�ځF�X�e�[�^�X�R�[�h
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) return status;
			
			// 2�s�ځF1.���H��ʃR�[�h,2.���H�ԍ�,3.2�����b�V���R�[�h
			String tokens[] = in.readLine().split(",");
			roadKindCode= Integer.parseInt(tokens[0]);
			roadNo 		= Integer.parseInt(tokens[1]);
			meshCode	= tokens[2];
			
			// 3�s�ځF1.�ߖT�o�x, 2.�ߖT�ܓx
			tokens = in.readLine().split(",");
			matchingLongitude = Double.parseDouble(tokens[0]);
			matchingLatitude = Double.parseDouble(tokens[1]);
			
			in.close();
			con.disconnect();
			
			// �X�e�[�^�X�l��Ԃ�
			return status;
		}
		catch(IOException exp) {
			exp.printStackTrace(); 
			return -1;
		}
	}
	
	/**
	 * �}�b�`�����H����ʂ̃R�[�h���擾
	 * @return �H����ʃR�[�h
	 */
	public int getRoadKindCode() {
		return roadKindCode;
	}
	
	/**
	 * �}�b�`�����H���ԍ����擾
	 * @return �H���ԍ�
	 */
	public int getRoadNo() {
		return roadNo;
	}
	
	/**
	 * �}�b�`�����ʒu��2�����b�V���R�[�h���擾
	 * @return 2�����b�V���R�[�h
	 */
	public String getMeshCode() {
		return meshCode;
	}
	
	/**
	 * �ߖT���W�̌o�x
	 * @return �o�x
	 */
	public double getMatchingLongitude() {
		return matchingLongitude;
	}
	
	/**
	 * �ߖT���W�̈ܓx
	 * @return �ܓx
	 */
	public double getMatchingLatitude() {
		return matchingLatitude;
	}
}

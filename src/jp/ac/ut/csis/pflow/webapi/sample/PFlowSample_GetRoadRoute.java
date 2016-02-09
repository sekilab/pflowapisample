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
 * <b>GetRoadRoute�̃N���C�A���g�T���v��</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetRoadRoute</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetRoadRoute USERID USERPW</dd>
 * <dd>���@USERID, USERPD�͂����g�̓o�^������̂������p��������</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetRoadRoute extends PFlowSample_Auth {
	/**
	 * �T���v���̎��s
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) {
		// ID/PW
		String userid = args[0];
		String passwd = args[1];
		
		// �p�����[�^�̍쐬
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2               ); // ���W�P�ʁ@1:�x���b�\�L�A2:�x�\�L
		parameter.put("StartLatitude",  35.90084483     ); // �n�_�ܓx�@���唐�L�����p�X
		parameter.put("StartLongitude", 139.93689537    ); // �n�_�o�x
		parameter.put("GoalLatitude",   35.7078869      ); // �I�_�ܓx�@����{���L�����p�X
		parameter.put("GoalLongitude",  139.76246595    ); // �I�_�o�x
//		parameter.put("WayLatitude",	0               ); // �o�R�_�ܓx(�C��)
//		parameter.put("WayLongitude",	0               ); // �o�R�_�o�x(�C��)
//		parameter.put("RoadKindCode",   3               ); // ���H���(�C��)
//		parameter.put("RoadNo",         0               ); // �H���ԍ�(�C��)
//		parameter.put("TransportCode",  6               ); // ��ʎ�i(�C��)
//		parameter.put("OutputNum",      2               ); // �o�͌o�H��(�C��)
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetRoadRoute sample = new PFlowSample_GetRoadRoute();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetRoadRoute�̎��s�F1���\�������ΐ���
		System.out.println("GetRoadRoute : " + sample.exec(parameter));
		// ���ʂ̏o��
		for(String line[] : sample.getResult()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		// �Z�b�V����ID��j��:1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroySession());
	}
	
	
	
	// �o�͌��ʗp
	/** response data	*/	private List<String[]>  result = new ArrayList<String[]>();
	
	/**
	 * �������ʎ擾
	 * @return ��������
	 */
	public List<String[]> getResult() {
		return result;
	}
	
	/**
	 * API���̂̎擾
	 * @return API����
	 */
	public String getAPIName() {
		return "GetRoadRoute";
	}
	
	/**
	 * GetRoadRoute�̎��s
	 * @param parameters GetRoadRoute�̃p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public int exec( Map<String, Object> parameters ) {	
		// ������
		result.clear();
		// �Z�b�V�����쐬�i���O�C���j���Ă��Ȃ��ꍇ
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP�ڑ�
			HttpURLConnection con = openHttpConnection(parameters);
			// ���X�|���X���擾
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			// 1�s�ځF�X�e�[�^�X�R�[�h
			int status = Integer.parseInt(in.readLine());
			if( status != 1 ) { return status; }
			
			// 2�s�ځ`�F�Y���f�[�^
			String line = null;
			while( (line = in.readLine()) != null ) {
				result.add(line.split(","));
			}
			
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
}

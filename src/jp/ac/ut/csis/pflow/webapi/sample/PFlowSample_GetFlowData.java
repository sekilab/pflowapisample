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
 * <b>GetFlowData�̃N���C�A���g�T���v��</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetFlowData</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetFlowData USERID USERPW</dd>
 * <dd>���@USERID, USERPD�͂����g�̓o�^������̂������p��������</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetFlowData extends PFlowSample_Auth {
	/**
	 * �T���v���̎��s
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) throws Exception {
		// ID/PW
		String userid = args[0];
		String passwd = args[1];
		
		// �p�����[�^�̍쐬
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("ResultTypeCode",     1           ); // �������ʎ擾���@�@1:���I�A2:�ÓI
		parameter.put("ResultMaxLine",      1000000     ); // �擾����f�[�^�s��
		parameter.put("ResearchID",         "98TKY"     ); // ����ID �i����ԃf�[�^�񋟃T�[�r�X�̐\���Ŏ擾�j
		parameter.put("PersonID",           "356953"       ); // �p�[�\��ID
		parameter.put("StartDate",          "19981001"  ); // �J�n���i����ID�Ɉˑ����ĕϓ��j
		parameter.put("StartTime",          "0730"      ); // �J�n����
		parameter.put("GoalDate",           "19981001"  ); // �I����
		parameter.put("GoalTime",           "0830"      ); // �I������
		parameter.put("UnitTypeCode",       2           ); // ���W�P�ʁ@1:�x���b�A2:�x�\�L
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetFlowData sample = new PFlowSample_GetFlowData();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetFlowData�̎��s�F1���\�������ΐ���
		System.out.println("GetFlowData : " + sample.exec(parameter));
		// ���ʂ̏o��
		for(String line[] : sample.getResult()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		for(String line[] : sample.getDuration()) {
			for(String str : line) System.out.print(str + ",");
			System.out.println();
		}
		// �Z�b�V����ID��j��:1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroySession());
	}

	
	
	// �o�͌��ʗp
	/** response data	*/	List<String[]>  result = new ArrayList<String[]>();		// �f�[�^
	/** response time	*/	List<String[]>	duration = new ArrayList<String[]>();	// ��������
	
	
	/**
	 * �������ʂ��擾
	 * @return ��������
	 */
	public List<String[]> getResult() {
		return result;
	}
	
	/**
	 * �������ԏ����擾
	 * @return �������ԏ��
	 */
	public List<String[]> getDuration() {
		return duration;
	}
	
	/**
	 * API���̂̎擾
	 * @return API����
	 */
	public String getAPIName() {
		return "GetFlowData";
	}
	
	/**
	 * GetFlowData�̎��s
	 * @param parameters GetFlowData�̃p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public int exec( Map<String, Object> parameters ) {
		// ������
		result.clear();
		duration.clear();
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
			
			// 2�s�ځ`�F�Y���f�[�^
			String line = null;
			while( (line = in.readLine()) != null ) {
				if( line.isEmpty() ) break;
				result.add(line.split(","));
			}
			// �������ԃf�[�^
			while( (line = in.readLine()) != null ) {
				duration.add(line.split(","));
			}
			in.close();
			con.disconnect();
			
			// �X�e�[�^�X�l��Ԃ�
			return status;
		}
		catch(IOException exp){
			exp.printStackTrace(); 
			return -1;
		}
	}
}

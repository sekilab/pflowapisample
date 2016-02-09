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
 * <b>GetPIDList�̃N���C�A���g�T���v��</b>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetPIDList</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetPIDList USERID USERPW</dd>
 * <dd>���@USERID, USERPD�͂����g�̓o�^������̂������p��������</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetPIDList extends PFlowSample_Auth {
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
		parameter.put("ResearchID",         "98TKY"     ); // ����ID �i����ԃf�[�^�񋟃T�[�r�X�̐\���Ŏ擾�j
		parameter.put("GeoOptionCode",      2           ); // ��Ԏw��@1:���Ȃ��A2:���W�w��A3:���b�V���w��A4:���̎w��i���Ή��j
		parameter.put("GeoOption",          1           ); // ��ԏ����@1:�ʉ߁A2:�؍�
		parameter.put("UnitTypeCode",       2           ); // ���W�P�ʁ@1:�x���b�A2:�x�\�L
		parameter.put("CenterLongitude",	139.76246595); // ���S�o�x
		parameter.put("CenterLatitude",		35.7078869  ); // ���S�ܓx
		parameter.put("DistanceTypeCode",   1           ); // �����P�ʁ@1:km�A2:m
		parameter.put("DistanceLong",       1           ); // �c����
		parameter.put("DistanceWide",       1           ); // ������
//		parameter.put("CenterName",         ""          ); // ���S���́i���Ή��j
//		parameter.put("MeshCode",           ""          ); // ���b�V���R�[�h�i3�����b�V���܂Łj
		parameter.put("StartDate",          "19981001"  ); // �J�n���i����ID�Ɉˑ����ĕϓ��j
		parameter.put("StartTime",          "0800"      ); // �J�n����
		parameter.put("GoalDate",           "19981001"  ); // �I����
		parameter.put("GoalTime",           "1200"      ); // �I������
		parameter.put("RandomCode",         2           ); // �����_���w��@1:���Ȃ��A2:����
		parameter.put("GetPersonRatio",     50          ); // �����_���擾����(%)
		parameter.put("TransportOptionCode",1           ); // ��ʎ�i�w��@1:���Ȃ��A2:����
		parameter.put("TransportCode",      "1,2,3,4,5" ); // ��ʎ�i�R�[�h�i����ID�Ɉˑ����ĕϓ��j
		parameter.put("SexOptionCode",      1           ); // ���ʎw��@1:���Ȃ��A2:����
		parameter.put("SexCode",            "1,2"       ); // ���ʁ@1:�j�A2:��
		parameter.put("AgeOptionCode",      1           ); // �N��w��@1:���Ȃ��A2:����
		parameter.put("AgeCode",            "4,5,6,7"   ); // �N��R�[�h�i����ID�Ɉˑ����ĕϓ��j
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetPIDList sample = new PFlowSample_GetPIDList();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetPIDList�̎��s�F1���\�������ΐ���
		System.out.println("GetPIDList : " + sample.exec(parameter));
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
	/** response time 	*/	List<String[]>	duration = new ArrayList<String[]>();	// ��������
	
	
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
		return "GetPIDList";
	}
	
	/**
	 * GetPIDList�̎��s
	 * @param parameters GetPIDList�̃p�����[�^
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

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
 * <b>GetSTInterpolatedPoints�̃N���C�A���g�T���v��</b>
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
public class PFlowSample_GetSTInterpolatedPoints extends PFlowSample_Auth {
	/**
	 * �T���v���̎��s(���[�UID�ƃp�X���[�h�̎w�肪�K�v�j
	 * @param args 0:userid, 1:password
	 */
	public static void main(String[] args) {
		// ID/PW
		String userid = args[0];
		String passwd = args[1];
		
		// �p�����[�^�̍쐬
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("UnitTypeCode",   2               ); // ���W�P�ʁ@1:�x���b�\�L�A2:�x�\�L
		parameter.put("DtStart",        "20120622090000"); // �o������(2010-07-15�@10:00:00)
		parameter.put("DtEnd",          "20120622091500"); // ��������(2010-07-15�@12:00:00)
		parameter.put("NetworkOption",  1               ); // �o�H���W�w��t���O�@1:���H�{�S�����������A2:���H�����A3:�S�������A4:�C�Ӄl�b�g���[�N
		parameter.put("StartLatitude",  35.90084483     ); // �n�_�ܓx�@���唐�L�����p�X
		parameter.put("StartLongitude", 139.93689537    ); // �n�_�o�x
		parameter.put("GoalLatitude",   35.7078869      ); // �I�_�ܓx�@����{���L�����p�X
		parameter.put("GoalLongitude",  139.76246595    ); // �I�_�o�x
//		parameter.put("Resolution",     300             ); // ���ԉ𑜓x�i�C�Ӂj
//		parameter.put("SubPointOption", 1               ); // ��ԓ_�L���t���O�@1:�L��A2:����
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetSTInterpolatedPoints sample = new PFlowSample_GetSTInterpolatedPoints();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetSTInterpolatedPoints�̎��s�F1���\�������ΐ���
		System.out.println("GetSTInterpolatedPoints : " + sample.exec(parameter));
		// ���ʂ̏o��
		System.out.println("time,lon,lat");
		for(String line[] : sample.getResult()) {
			StringBuffer buf = new StringBuffer();
			for(String str : line) buf.append(",").append(str);
			System.out.println(buf.substring(1));
		}
		// �Z�b�V����ID��j��:1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroySession());
	}
	
	
	
	// �o�͌��ʗp
	private List<String[]>  result = new ArrayList<String[]>();
	
	/**
	 * ���ʔz��
	 * @return ����
	 */
	public List<String[]> getResult() {
		return result;
	}
	
	/**
	 * WebAPI�̖��̂��擾
	 * @return WebAPI����
	 */
	public String getAPIName() {
		return "GetSTInterpolatedPoints";
	}
	
	/**
	 * GetSTInterpolatedPoints�̎��s
	 * @param parameters GetSTInterpolatedPoints�̃p�����[�^
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

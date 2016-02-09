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
 * <b>GetDistributionData�̃N���C�A���g�T���v��</b><br/>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetDistributionData</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetDistributionData USERID USERPW</dd>
 * <dd>���@USERID, USERPD�͂����g�̓o�^������̂������p��������</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetDistributionData extends PFlowSample_Auth {
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
		parameter.put("ResultTypeCode",             1           ); // �������ʎ擾���@�@1:���I�A2:�ÓI
		parameter.put("ResultMaxLine",              1000000     ); // �擾����f�[�^�s��
		parameter.put("UnitTypeCode",               2           ); // ���W�P�ʁ@1:�x���b�A2:�x�\�L
		parameter.put("ResearchID",                 "XXXXX"     ); // ����ID �i�f�[�^���ƂɈقȂ邽�߁A�d�l�����m�F�j
		parameter.put("GeoOptionCode",              1           ); // 1�F��Ԏw����s�Ȃ킢	2�F��Ԏw����s ���i���S���W�w ��j�@�R�F��Ԏw����s���i����A�E�� ���W�w��j
		// for GeoOptionCode=2
		parameter.put("CenterLongitude",            139.76246595); // ���S�o�x
		parameter.put("CenterLatitude",	            35.7078869  ); // ���S�ܓx
		parameter.put("DistanceTypeCode",           1           ); // �����P�ʁ@1:km�A2:m
		parameter.put("DistanceLong",               1           ); // �c����
		parameter.put("DistanceWide",               1           ); // ������
		// for GeoOptionCode=3
		parameter.put("LeftTopCornerLongitude",	    139.75246595); // ����o�x
		parameter.put("LeftTopCornerLatitude",	    35.7178869  ); // ����ܓx
		parameter.put("RightBottomCornerLongitude",	139.77246595); // �E���o�x
		parameter.put("RightBottomCornerLatitude",  35.6978869  ); // �E���ܓx
		// other parameters
		parameter.put("StartDate",                  "19981001"  ); // ���t�i����ID�Ɉˑ����ĕϓ�����B�d�l�����m�F�j
		parameter.put("StartTime",                  "0900"      ); // ����
		parameter.put("TransportOptionCode",        1           ); // ��ʎ�i�w��@1:���Ȃ��A2:����
		parameter.put("TransportCode",              "1,2,3,4,5" ); // ��ʎ�i�R�[�h�i����ID�Ɉˑ����ĕϓ��j
		parameter.put("SexOptionCode",              1           ); // ���ʎw��@1:���Ȃ��A2:����
		parameter.put("SexCode",                    "1,2"       ); // ���ʁ@1:�j�A2:��
		parameter.put("AgeOptionCode",              1           ); // �N��w��@1:���Ȃ��A2:����
		parameter.put("AgeCode",                    "4,5,6,7"   ); // �N��R�[�h�i����ID�Ɉˑ����ĕϓ��j
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetDistributionData sample = new PFlowSample_GetDistributionData();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetDistributionData�̎��s�F1���\�������ΐ���
		System.out.println("GetDistributionData : " + sample.exec(parameter));
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
	/** data 			*/	List<String[]>  result = new ArrayList<String[]>();		// �f�[�^
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
		return "GetDistributionData";
	}
	
	/**
	 * GetDistributionData�̎��s
	 * @param parameters GetDistributionData�̃p�����[�^
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
			if( status != 1 ) { return status; }
			
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

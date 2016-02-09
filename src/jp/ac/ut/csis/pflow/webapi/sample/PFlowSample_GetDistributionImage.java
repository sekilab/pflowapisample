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
 * <p>GetDistributionImage�̃N���C�A���g�T���v��</p>
 * <dl>
 * <dt>Build:</dt>
 * <dd>javac pflow\sample\PFlowSample_GetDistributionImage</dd>
 * <dt>Usage:</dt>
 * <dd>java pflow.sample.PFlowSample_GetDistributionImage USERID USERPW</dd>
 * <dd>���@USERID, USERPD�͂����g�̓o�^������̂������p��������</dd>
 * </dl>
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public class PFlowSample_GetDistributionImage extends PFlowSample_Auth {
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
		parameter.put("ResearchID",         "XXXXX"     ); // ����ID �i����ԃf�[�^�񋟃T�[�r�X�̐\���Ŏ擾�j
		parameter.put("UnitTypeCode",       2           ); // ���W�P�ʁ@1:�x���b�A2:�x�\�L
		parameter.put("CenterLongitude",	139.76246595); // ���S�o�x
		parameter.put("CenterLatitude",		35.7078869  ); // ���S�ܓx
		parameter.put("DistanceTypeCode",   1           ); // �����P�ʁ@1:km�A2:m
		parameter.put("DistanceLong",       1           ); // �c����
		parameter.put("DistanceWide",       1           ); // ������
		parameter.put("AppDate",            "19980101"  ); // ���t�i����ID�Ɉˑ����ĕϓ��j
		parameter.put("AppTime",            "0800"      ); // ����
		parameter.put("TransportOptionCode",1           ); // ��ʎ�i�w��@1:���Ȃ��A2:����
		parameter.put("TransportCode",      "1,2,3,4,5" ); // ��ʎ�i�R�[�h�i����ID�Ɉˑ����ĕϓ��j
		parameter.put("SexOptionCode",      1           ); // ���ʎw��@1:���Ȃ��A2:����
		parameter.put("SexCode",            "1,2"       ); // ���ʁ@1:�j�A2:��
		parameter.put("AgeOptionCode",      1           ); // �N��w��@1:���Ȃ��A2:����
		parameter.put("AgeCode",            "4,5,6,7"   ); // �N��R�[�h�i����ID�Ɉˑ����ĕϓ��j
		
		// �T���v���C���X�^���X�̐���
		PFlowSample_GetDistributionImage sample = new PFlowSample_GetDistributionImage();
		// �Z�b�V�����̐����i���O�C���j�F1���\�������ΐ���
		System.out.println("CreateSession : " + sample.createSession(userid, passwd));
		// GetDistributionImage�̎��s�F1���\�������ΐ���
		System.out.println("GetDistributionImage : " + sample.exec(parameter));
		// ���ʂ̏o��
		Image img = sample.getImage();
		if( img != null ) {
			JFrame frame = new JFrame();
			frame.setContentPane(new JLabel(new ImageIcon(img)));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		}
		// �Z�b�V����ID��j��:1���\�������ΐ���
		System.out.println("DestroySession : " + sample.destroySession());
	}


	// ���ʉ摜�p
	/** response image	*/	Image image = null;
	
	
	/**
	 * ���ʉ摜�擾�p
	 * @return �擾�摜
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * API���̂̎擾
	 * @return API����
	 */
	public String getAPIName() {
		return "GetDistributionImage";
	}
	
	/**
	 * GetDistributionImage�̎��s
	 * @param parameters GetDistributionImage�̃p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public int exec( Map<String, Object> parameters ) {
		// ������
		image = null;
		// �Z�b�V�����쐬���Ă��Ȃ��ꍇ
		if( !isAuthed() ) { return -1; }
		
		try {
			// HTTP�ڑ�
			HttpURLConnection con = openHttpConnection(parameters);
			
			int status = -1;
			// �G���[
			if( con.getHeaderField("Content-Type").equals("text/plain") ) {
				image = null;
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				status = Integer.parseInt(in.readLine());
				in.close();
			}
			// ����
			else {
				image = ImageIO.read(con.getInputStream());
				status = 1;
			}
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

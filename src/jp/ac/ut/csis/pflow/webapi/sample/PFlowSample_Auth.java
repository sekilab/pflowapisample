package jp.ac.ut.csis.pflow.webapi.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * �������WebAPI�̔F�ؗp�T���v��
 * @author PFlow Project. CSIS. UTokyo
 * @since 2010-07-15
 */
public abstract class PFlowSample_Auth {
	
	/** �������PFWEBAPI��URL */	public static final String API_URL = System.getProperty("pflow.webapi.url",
																						"http://pflow-api.csis.u-tokyo.ac.jp/webapi/");

	// ID�p
	/** cookie �p			*/	private String cookie;
	/** session ID 			*/	private String sessionID;	
	
	/**
	 * ���[�UID�ƃp�X���[�h���w�肵�ăZ�b�V�������쐬
	 * @param userid ���[�UID
	 * @param password �p�X���[�h
	 * @return �Z�b�V�����쐬�����Ȃ��1�A����ȊO�͂��ׂăG���[(�G���[�R�[�h�͎d�l���Q��)
	 */
	public int createSession(String userid, String password) {
		// �Z�b�V�����쐬�ς�
		if( isAuthed() ) { return -1; }
		try {
			// WebAPI URL�̃Z�b�g
			URL url = new URL(API_URL + "CreateSession");
			HttpURLConnection con = HttpURLConnection.class.cast(url.openConnection());
			
			// POST���M
			con.setDoOutput(true);
			PrintWriter out = new PrintWriter(con.getOutputStream());
			out.print("UserID="+ userid + "&" + "Password=" + password);
			out.close();
			
			// ���X�|���X�̏���
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line_tokens[] = in.readLine().split(",");
			in.close();
			con.disconnect();
			
			// �Z�b�V����ID�擾�ł���
			if( line_tokens.length == 1 ) { return Integer.parseInt(line_tokens[0]); } 
			
			// ���O�C������:�Z�b�V����ID�ƃN�b�L�[����ێ�
			sessionID = line_tokens[1];
			StringBuffer buf = new StringBuffer();
			List<String> cookies = con.getHeaderFields().get("Set-Cookie");
			for(String c : cookies) { buf.append(c).append(";"); }
			cookie = buf.substring(0, buf.length()-1);
			
			// �X�e�[�^�X�R�[�h��Ԃ�
			return Integer.parseInt(line_tokens[0]);
		}
		catch(IOException exp) { exp.printStackTrace(); return -1; }
	}
	
	/**
	 * �擾���Ă���Z�b�V������j�����܂�
	 * @return �Z�b�V�����j�������Ȃ��1�A����ȊO�͂��ׂăG���[(�G���[�R�[�h�͎d�l���Q��)
	 */
	public int destroySession() {
		// �Z�b�V�����쐬���Ă��Ȃ��ꍇ
		if( !isAuthed() ) { return -1; }
		
		try {
			// WebAPI URL�̃Z�b�g
			URL url = new URL(API_URL + "DestroySession");
			HttpURLConnection con = HttpURLConnection.class.cast(url.openConnection());
			
			// POST���M�̗L����
			con.setDoOutput(true);
			con.setRequestProperty("Cookie", cookie);
		
			PrintWriter out = new PrintWriter(con.getOutputStream());
			out.print("SessionID=" + sessionID);
			out.close();

			// ���X�|���X���擾
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line_tokens = in.readLine();
			in.close();
			con.disconnect();
			
			// ������
			sessionID = null;
			cookie    = null;
			// �X�e�[�^�X�l��Ԃ�
			return Integer.parseInt(line_tokens);
		}
		catch(IOException exp) { exp.printStackTrace();  return -1; }
	}
	
	/**
	 * WebAPI�p�����[�^���w�肵�āAHTTP�ڑ����J��
	 * @param parameters WebAPI�p�����[�^
	 * @return HTTP�ڑ��BCreateSession���Ă��Ȃ��ꍇ��null
	 * @throws IOException IO��O
	 */
	public HttpURLConnection openHttpConnection(Map<String, Object> parameters) throws IOException {
		// �F�؍ς݊m�F
		if( !isAuthed() ) { return null; }
		
		// WebAPI URL�쐬
		URL url = new URL(API_URL + getAPIName());
		HttpURLConnection con = HttpURLConnection.class.cast(url.openConnection());

		// POST���M�̗L����
		con.setDoOutput(true);
		con.setRequestProperty("Cookie", getCookie());
		
		// �p�����[�^�쐬
		StringBuffer buf = new StringBuffer();
		for(String key : parameters.keySet()) {
			String v = parameters.get(key).toString();
			String val = URLEncoder.encode(v, "SJIS");
			buf.append(key).append('=').append(val).append('&');
		}
		PrintWriter out = new PrintWriter(con.getOutputStream());
		out.print(	buf.substring(0, buf.length()-1));
		out.close();
		
		return con;
	}
	
	/**
	 * �F�؂��������A�Z�b�V����ID���擾�ł��Ă��邩�ǂ����𔻒�
	 * @return �F�؍ς݂ł����true�A�����łȂ����false
	 */
	protected boolean isAuthed() {
		return sessionID != null && cookie != null;
	}
	
	/**
	 * �Z�b�V����ID���擾
	 * @return �Z�b�V����ID
	 */
	protected String getSessionID() {
		return (sessionID == null) ? "" : sessionID;
	}
	
	/**
	 * �N�b�L�[�����擾
	 * @return �N�b�L�[������
	 */
	protected String getCookie() {
		return (cookie == null) ? "" : cookie;
	}
	
	/**
	 * �eAPI�̎��s�p
	 * @param parameter �p�����[�^
	 * @return �X�e�[�^�X�R�[�h
	 */
	public abstract int exec(Map<String,Object> parameter);
	
	/**
	 * WebAPI�̖��̂��擾
	 * @return WebAPI����
	 */
	public abstract String getAPIName();
}

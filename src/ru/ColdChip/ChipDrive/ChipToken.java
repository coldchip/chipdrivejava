package ru.ColdChip.ChipDrive;

import ru.ColdChip.WebServer.ChipSession.SimpleSession;
import org.JSON.JSONObject;
import org.JSON.JSONException;

public class ChipToken {
	private String token = "";
	public boolean ValidateToken(String token) {
		try {
			SimpleSession ss = new SimpleSession();
			if(ss.Validate(token) == true) {
				String sessionData = ss.GetTokenData(token);
				JSONObject sessionDataJSON = new JSONObject(sessionData);
				if(sessionDataJSON.getBoolean("logined") == true && sessionDataJSON.getBoolean("tokened") == true) {
					this.token = token;
					return true;
				}
			}
		} catch(JSONException e) {
			
		}
		return false;
	}
	public String GetUser() {
		String data = new SimpleSession().GetTokenUser(this.token);
		return data;
	}
}
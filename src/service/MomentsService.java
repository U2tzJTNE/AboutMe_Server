package service;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bean.MomentsBean;
import db.DBManager;

public class MomentsService {
	private String APOSTROPHE = "%u0027%";

	// 添加
	public boolean addMoments(MomentsBean bean) {

		String nickname = bean.getNickname();
		String avatarURL = bean.getAvatarURL();
		String date = bean.getDate();
		String message = bean.getMessage().replaceAll("'", APOSTROPHE);
		int fontSize = bean.getFontSize();
		int bgColor = bean.getBgColor();
		String userID = bean.getUserID();
		String email = bean.getEmail();

		// 获取Sql查询语句
		String addSql = "insert into moments(nickname,avatar_url,font_size,bg_color,user_id,email,date,message) values('"
				+ nickname + "','" + avatarURL + "'," + fontSize + "," + bgColor + ",'" + userID + "','" + email + "','"
				+ date + "','" + message + "') ";
		// System.out.println("-------addSql------"+addSql);
		// 获取DB对象
		DBManager sql = DBManager.createInstance();
		sql.connectDB();

		int ret = sql.executeUpdate(addSql);
		if (ret != 0) {
			sql.closeDB();
			return true;
		}
		sql.closeDB();

		return false;
	}
	// 查询

	public String getMoments(int index) {
		String getSql = "select * from moments order by id DESC limit " + index + ",20";
		System.out.println("-------getSql------" + getSql);
		// 获取DB对象
		DBManager sql = DBManager.createInstance();
		sql.connectDB();
		ResultSet rs = sql.executeQuery(getSql);
		if (rs != null) {
			ArrayList<MomentsBean> list = new ArrayList<>();
			try {
				while (rs.next()) {
					MomentsBean bean = new MomentsBean();
					bean.setAvatarURL(rs.getString("avatar_url"));
					bean.setNickname(rs.getString("nickname"));
					bean.setBgColor(rs.getInt("bg_color"));
					bean.setFontSize(rs.getInt("font_size"));
					bean.setUserID(rs.getString("user_id"));
					bean.setDate(rs.getString("date"));
					bean.setEmail(rs.getString("email"));
					bean.setMessage(rs.getString("message").replaceAll(APOSTROPHE, "'"));
					list.add(bean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<MomentsBean>>() {
            }.getType();
			return gson.toJson(list, type);
		}else {
			return null;
		}

	}
}

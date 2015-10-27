package com.haojiahong.weixin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUtil {
	public Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_yuanyuanli";
		String user = "oo23j0owlk";
		String password = "yk1xm1xm13l134i4lkhjxk0j1z342imkizxi11m4";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void releaseResource(Connection conn, PreparedStatement ps,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}

			if (ps != null) {
				ps.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveTextMessage(String openId, String content) {
		MySQLUtil mysql = new MySQLUtil();
		Connection conn = mysql.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into message_text (open_id,content,createtime)values(?,?,now())";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ps.setString(2, content);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.releaseResource(conn, ps, null);
		}
	}

	// 保存微信用户信息。
	public static void saveWeixinUser(String openId) {
		MySQLUtil mysql = new MySQLUtil();
		Connection conn = mysql.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into weixin_user (open_id,subscribe_time,subscribe_status)values(?,now(),1)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.releaseResource(conn, ps, null);
		}
	}

	// 保存微信用户签到信息。
	public static void saveWeixinSign(String openId, int signPoints) {
		MySQLUtil mysql = new MySQLUtil();
		Connection conn = mysql.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into weixin_sign (open_id,sign_time,sign_points)values(?,now(),?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ps.setInt(2, signPoints);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.releaseResource(conn, ps, null);
		}
	}

	// 更新用户积分值。
	public static void updateUserPoints(String openId, int signPoints) {
		MySQLUtil mysql = new MySQLUtil();
		Connection conn = mysql.getConnection();
		PreparedStatement ps = null;
		String sql = "update weixin_user set points =points+ ? where open_id=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, signPoints);
			ps.setString(2, openId);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.releaseResource(conn, ps, null);
		}
	}

	// 判断用户今天是否签到过
	public static boolean isTodaySigned(String openId) {

		boolean result = false;
		MySQLUtil mysql = new MySQLUtil();
		Connection conn = mysql.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) FROM weixin_sign WHERE open_id= ? "
				+ " AND DATE_FORMAT(sign_time,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			rs = ps.executeQuery(sql);
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.releaseResource(conn, ps, rs);
		}
		return result;
	}

	/** 判断用户本周是否第七次签到
	 * 
	 * @param openId
	 * @param monday 本周周一时间
	 * @return
	 */
	public static boolean isSevenSigned(String openId,String monday) {

		boolean result = false;
		MySQLUtil mysql = new MySQLUtil();
		Connection conn = mysql.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) as signCounts FROM weixin_sign WHERE open_id=? "
				+ " AND sign_time between str_to_date( ? ,'%Y-%m-%d %H:%i:%s') and now()";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ps.setString(2, monday);
			rs = ps.executeQuery(sql);
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mysql.releaseResource(conn, ps, rs);
		}
		return result;
	}
}

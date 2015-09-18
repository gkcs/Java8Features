package work;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAO {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public List<FileMaker.Record> insertRecords() {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<FileMaker.Record> list = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sinbad?user=root&password=");
            System.out.println("Creating database...");
            String sql = "insert into call_history(jid,country_code,rate,duration,chargeable_duration,caller_number,app_domain,cost,call_id,epoch_time,balance,status,call_flow, destination_number,callout_local_number,callout_ring_time,callout_api_call_wait_time,callout_session_time,callout_caller_wait_time,failure_reason,termination_reason) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Random random = new Random();
            String[] statuses = {"terminated", "init_failed"};
            String[] callflows = {"callout", "callback", "voipout"};
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < 2000000; i++) {
                stmt.setString(1, getRandomString(3) + "@atropos.voodoo.com");
                stmt.setString(2, getRandomString(1));
                stmt.setDouble(3, Math.random());
                stmt.setInt(4, random.nextInt());
                stmt.setInt(5, random.nextInt());
                stmt.setString(6, getRandomString(6));
                stmt.setString(7, "voodoo.com");
                stmt.setDouble(8, random.nextDouble());
                stmt.setString(9, getRandomString(7));
                stmt.setLong(10, System.currentTimeMillis());
                stmt.setDouble(11, random.nextDouble());
                stmt.setString(12, statuses[random.nextInt(2)]);
                stmt.setString(13, callflows[random.nextInt(3)]);
                stmt.setString(14, getRandomString(6));
                stmt.setInt(15, random.nextInt());
                stmt.setInt(16, random.nextInt());
                stmt.setInt(17, random.nextInt());
                stmt.setInt(18, random.nextInt());
                stmt.setInt(19, random.nextInt());
                stmt.setString(20, getRandomString(6));
                stmt.setString(21, getRandomString(6));
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                try {
                    conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println("Goodbye!");
        }
        return list;
    }

    public List<FileMaker.Record> getRecords() {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<FileMaker.Record> list = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sinbad?user=root&password=");
            System.out.println("Creating database...");
            String sql = "select * from call_history where call_id=? and jid=?";
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < 200000; i++) {
                stmt.setString(1, getRandomString(7));
                stmt.setString(2, getRandomString(3) + "@atropos.voodoo.com");
                ResultSet resultSet = stmt.executeQuery();
                resultSet.getFetchSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                try {
                    conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println("Goodbye!");
        }
        return list;
    }

    private String getRandomString(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            stringBuilder.append((char) ('a' + random.nextInt(26)));
        }
        return stringBuilder.toString();
    }
}
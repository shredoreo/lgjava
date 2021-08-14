package elasticjob;

public class TestData {
    public static void main(String[] args) {
        String s = "insert into resume (id, name, sex, phone, address, education, state)\n" +
                "VALUES (null, ?, ?, ?, ?, ?, ?)";

        for (int i = 1; i <= 100; i++) {
            JdbcUtil.executeUpdate(s,i,i,i,i,i,"未归档");
        }
    }

}

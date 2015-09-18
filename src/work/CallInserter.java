package work;

import work.DAO;

public class CallInserter {
    public static void main(String args[]) {
        DAO dao = new DAO();
        dao.getRecords();
    }
}

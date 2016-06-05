package dtu.dagprojekt.bankaroo.util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateQuery {

    private StringBuilder sql;
    private int updateCount;

    public UpdateQuery() {
        this.sql = new StringBuilder();
    }

    public UpdateQuery update(Schema schema){
        sql.append("UPDATE \"").append(DB.TABLE).append("\"");
        sql.append(".\"").append(schema.toString()).append("\" ");
        return this;
    }

    public UpdateQuery set(LinkedHashMap<String, Object> updates){
        sql.append("SET ");

        String separator = "";
        for(Map.Entry<String, Object> entry : updates.entrySet()){
            sql.append(separator);
            sql.append("\"").append(entry.getKey()).append("\"").append(" = ");
            sql.append("'").append(entry.getValue()).append("'");
            separator = ", ";
        }
        return this;
    }

    public UpdateQuery where(String field){
        sql.append(" WHERE ").append("\"").append(field).append("\" ");
        return this;
    }

    public UpdateQuery equal(Object value){
        sql.append("= ").append("'").append(value).append("' ");
        return this;
    }

    public String getQuery(){
        return sql.toString();
    }

    public UpdateQuery execute() throws SQLException {
        Statement statement = DB.getConnection().createStatement();
        this.updateCount = statement.executeUpdate(sql.toString());
        statement.close();
        DB.getConnection().commit();
        return this;
    }

    public UpdateQuery expect(int i) throws SQLException {
        if (updateCount != i){
            throw new SQLException("No rows were updated");
        }
        return this;
    }


}

package dtu.dagprojekt.bankaroo.util;

import com.google.gson.stream.JsonWriter;

import javax.swing.plaf.nimbus.State;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class UpdateQuery {

    private StringBuilder sql;
    private int updateCount;
    private ResultSet resultSet;

    private boolean isFirstElement = true;
    private boolean isSelectStm = false;
    private Statement statement;


    public UpdateQuery() {
        this.sql = new StringBuilder();
    }

    @Override
    public String toString() {
        return sql.toString();
    }

    public UpdateQuery call(Procedure transaction) {
        sql.append("CALL ").append(transaction);
        return this;
    }

    public UpdateQuery params(Object... param) {
        sql.append("(");
        for (Object obj : param){
            if (isFirstElement){
                isFirstElement = false;
            } else {
                sql.append(", ");
            }
            sql.append("'").append(obj).append("'");
        }
        sql.append(")");

        return this;

    }

    private StringBuilder appendValueSQL(StringBuilder sql, Object obj){
        if (obj.equals("DEFAULT")){
            sql.append(obj);
        } else {
            sql.append("'").append(obj).append("'");
        }
        return sql;
    }

    public UpdateQuery values(Object... values) {
        sql.append("VALUES(");
        String prepend = "";
        for (Object obj : values){
            sql.append(prepend);
            appendValueSQL(sql, obj);
            prepend = ", ";
        }
        sql.append(") ");
        return this;
    }

    public UpdateQuery update(Schema schema){
        sql.append("UPDATE \"").append(DB.TABLE).append("\"");
        sql.append(".\"").append(schema.toString()).append("\" ");
        isSelectStm = false;
        return this;
    }

    public UpdateQuery insert(Schema schema) {
        sql.append("INSERT INTO \"").append(DB.TABLE).append("\"");
        sql.append(".\"").append(schema.toString()).append("\" ");
        isSelectStm = false;
        return this;
    }

    public UpdateQuery delete() {
        sql.append("DELETE ");
        isSelectStm = false;
        return this;
    }

    public UpdateQuery select() {
        sql.append("SELECT ");
        isSelectStm = true;
        return this;
    }

    public UpdateQuery all() {
        sql.append("* ");
        return this;
    }

    public UpdateQuery from(Schema schema) {
        sql.append("FROM \"").append(DB.TABLE).append("\"");
        sql.append(".\"").append(schema.toString()).append("\"");
        return this;
    }

    public UpdateQuery set(Map<Enum, Object> updates){
        for(Map.Entry<Enum, Object> entry : updates.entrySet()){
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public UpdateQuery set(Enum field, Object value) {
        if (isFirstElement){
            sql.append("SET ");
            isFirstElement = false;
        } else {
            sql.append(", ");
        }
        String seperator = "";

        sql.append(seperator).append("\"").append(field.toString()).append("\"");
        sql.append(" = ").append("'").append(value).append("'");

        return this;
    }

    public UpdateQuery where(String field){
        sql.append(" WHERE ").append("\"").append(field).append("\" ");
        return this;
    }

    public UpdateQuery where(Enum field){
        return where(field.toString());
    }

    public UpdateQuery equal(Object value){
        sql.append("= ").append("'").append(value).append("' ");
        return this;
    }


    public String getQuery(){
        return sql.toString();
    }

    public UpdateQuery execute() throws SQLException {
        statement = DB.getConnection().createStatement();

        if (isSelectStm){
            this.resultSet = statement.executeQuery(sql.toString());
        } else {
            this.updateCount = statement.executeUpdate(sql.toString());
        }

        return this;
    }

    public ResultSet resultSet(){
        return resultSet;
    }

    public StreamingOutput toJson() throws SQLException {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) {
                try {
                    Writer out = new BufferedWriter(new OutputStreamWriter(os));
                    writeJson(out);
                    out.flush();
                    close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void writeJson(Writer out) throws IOException, SQLException {
        JsonWriter writer = new JsonWriter(out);

        writer.beginArray();
        while(resultSet.next()) {
            writer.beginObject();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                writer.name(metaData.getColumnLabel(i));
                writer.value(resultSet.getString(i));
            }
            writer.endObject();
        }
        writer.endArray();
        writer.flush();
    }

    public UpdateQuery close() throws SQLException {
        // Close the Statement (also closes result set)
        statement.close();

        // Connection must be on a unit-of-work boundary to allow close
        DB.getConnection().commit();
        return this;
    }


    public UpdateQuery expect(int i) throws SQLException {
        if (updateCount != i){
            throw new SQLException("Expected " + i + " row(s) to be updated but updated " + updateCount);
        }
        return this;
    }
}

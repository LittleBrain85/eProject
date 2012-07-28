/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thaichau
 */
public class DBHandler {

    private String user;
    private String pass;
    private String url;
    private SettingHandler sh;

    
    public DBHandler() {
        try {
            sh = new SettingHandler();
            String[] data = sh.getData();
            this.user = data[0];
            this.pass = data[1];
            this.url = data[2];
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public java.sql.Connection DBConnect() throws ClassNotFoundException, SQLException {
        java.lang.Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return java.sql.DriverManager.getConnection(this.url, this.user, this.pass);   
    }

    /**
     * Disconnect
     *
     * @param conn : java.sql.Connection
     * @throws SQLException
     */
    public void DBDisconnect(java.sql.Connection conn) throws SQLException {
        conn.close();
    }

    /**
     * Get column sql data type in result set
     *
     * @param rs : java.sql.ResultSet
     * @return int[] : SQL data type constant
     * @throws SQLException
     */
    public int[] getColumnDataType(java.sql.ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int tableLenght = rsmd.getColumnCount();
        int[] columnClass = new int[tableLenght];
        for (int i = 1; i <= tableLenght; i++) {
            columnClass[i - 1] = rsmd.getColumnType(i);
        }
        return columnClass;
    }

    public java.util.Vector getColumnName(java.sql.ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int colnum = rsmd.getColumnCount();
        java.util.Vector columnName = new Vector();
        for (int i = 0; i < colnum; i++) {
            columnName.add(rsmd.getColumnLabel(i + 1));
        }
        return columnName;
    }

    /**
     * Get number of column in Result set
     *
     * @param rs : java.sql.ResultSet
     * @return int : number of column
     * @throws SQLException
     */
    public int getNumberOfColumn(java.sql.ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int numOfCol = rsmd.getColumnCount();
        return numOfCol;
    }

    /**
     * Convert data from result set to vector to display in table
     *
     * @param rs : java.sql.ResultSet
     * @return java.util.Vector
     * @throws SQLException
     */
    public java.util.Vector getDataVector(java.sql.ResultSet rs) throws SQLException {
        int maxCol = this.getNumberOfColumn(rs);
        java.util.Vector v = new java.util.Vector();
        int[] colTypes = this.getColumnDataType(rs);
        while (rs.next()) {
            java.util.Vector list = new java.util.Vector();
            for (int i = 1; i <= maxCol; i++) {
                if (colTypes[i - 1] == java.sql.Types.CHAR
                        || colTypes[i - 1] == java.sql.Types.NCHAR
                        || colTypes[i - 1] == java.sql.Types.VARCHAR
                        || colTypes[i - 1] == java.sql.Types.NVARCHAR) {
                    list.add(rs.getString(i));
                }
                if (colTypes[i - 1] == java.sql.Types.INTEGER) {
                    list.add(rs.getInt(i));
                }
                if (colTypes[i - 1] == java.sql.Types.FLOAT
                        || colTypes[i - 1] == java.sql.Types.REAL
                        || colTypes[i - 1] == java.sql.Types.DOUBLE) {
                    list.add(java.text.MessageFormat.format("{0,number,#.##}", rs.getFloat(i)));
                }
                if (colTypes[i - 1] == java.sql.Types.DATE) {
                    list.add(formatDate(rs.getDate(i)));
                }
                if (colTypes[i - 1] == java.sql.Types.TIMESTAMP) {
                    list.add(formatDate(rs.getTimestamp(i)));
                }
            }
            v.add(list);
        }
        return v;
    }

    /**
     * Format Date in form dd/MM/yyyy
     *
     * @param d java.util.Date
     * @return java.lang.String
     */
    public String formatDate(java.util.Date d) {
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String res = sd.format(d);
        return res;
    }
    
    public String formatDate2(java.util.Date d) {
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String res = sd.format(d);
        return res;
    }
    
    public String quote(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(0, '\'');
        sb.insert(str.length() + 1, '\'');
        return sb.toString();
    }

    /**
     * Load Result set into DefaultTable model
     *
     * @param model DefaultTableModel
     * @param rs java.sql.ResultSet
     * @throws SQLException
     */
    public void loadTable(javax.swing.table.DefaultTableModel model, java.sql.ResultSet rs) throws SQLException {
        java.util.Vector vRows = this.getDataVector(rs);
        java.util.Vector vCols = new java.util.Vector();
        int Cols = this.getNumberOfColumn(rs);
        for (int i = 0; i < Cols; i++) {
            vCols.add(model.getColumnName(i));
        }
        model.setDataVector(vRows, vCols);
    }
}

package logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticAna {
    Connection connection;

    //Get connection
    public StatisticAna(Connection conn) {
        this.connection = conn;
    }

    public String convertForLabel(int whi, String DriverTeam, int fromYear, int toYear) throws SQLException {
        StringBuilder res;
        switch(whi){
            case 1 -> {
                ResultSet result = Driver(1,"",fromYear,0);
                res = new StringBuilder("<html><table><tr><th>Number</th><th>Rider</th><th>Points</th><th>Team</th></tr>");
                while (result.next()) {
                    res.append("<tr><th>").append(result.getString("Num")).append("</th><th>").append(result.getString("Driver")).append("</th><th>").append(result.getString("Total")).append("</th><th>").append(result.getString("Team")).append("</th></tr>");
                }
                res.append("</table></html>");
            }
            case 2 -> {
                ResultSet result = Constructor(1,"",fromYear,0);
                res = new StringBuilder("<html><table><tr><th>Team</th><th>Points</th></tr>");
                while (result.next()) {
                    res.append("<tr><th>").append(result.getString("Team")).append("</th><th>").append(result.getString("Pkt")).append("</th></tr>");
                }
                res.append("</table></html>");
            }
            default -> throw new IllegalStateException("Unexpected value: " + whi);
        }
        return  res.toString();
    }

    //Statistic of driver
    public ResultSet Driver(int which, String driver, int fromYear, int toYear) throws SQLException {
        PreparedStatement query;
        switch (which){
            case 1 -> {
                query = this.connection.prepareStatement("SELECT *, SUM(Poi) AS Total FROM Results WHERE Year = ? GROUP BY Driver ORDER BY Total DESC,Num");
                query.setString(1, String.valueOf(fromYear));
            }
            case  2 -> {
                query = this.connection.prepareStatement("SELECT Year,sum(Poi) AS SUM FROM Results WHERE Driver = ? AND Year BETWEEN ? AND ? GROUP BY Year");
                query.setString(1, driver);
                query.setString(2, String.valueOf(fromYear));
                query.setString(3, String.valueOf(toYear));
            }
            case 3 -> {
                query = this.connection.prepareStatement("SELECT count(Position) AS SUM,Position FROM Results WHERE Driver = ? AND Rok BETWEEN ? AND ? GROUP BY Position");
                query.setString(1, driver);
                query.setString(2, String.valueOf(fromYear));
                query.setString(3, String.valueOf(toYear));
            }
            case 4 -> {
                query = this.connection.prepareStatement("SELECT * FROM(SELECT ROW_NUMBER() OVER (partition by Year ORDER BY Year) AS PositionInYear,* FROM(SELECT Pkciki,Driver ,Rok FROM (SELECT *, SUM(Pkt) AS Pkciki FROM Wyniki WHERE Rok BETWEEN ? AND ? GROUP BY Rok,Driver ORDER BY Pkciki DESC,Num)))WHERE Driver = ?");
                query.setString(1, String.valueOf(fromYear));
                query.setString(2, String.valueOf(toYear));
                query.setString(3, driver);
            }
            case 5 -> {
                query = this.connection.prepareStatement("SELECT MiejsceWSezonie,count(MiejsceWSezonie) AS Ilosc,Driver,Rok FROM(SELECT * FROM(SELECT ROW_NUMBER() OVER (partition by Rok ORDER BY Rok) AS MiejsceWSezonie,* FROM(SELECT Pkciki,Driver ,Rok FROM (SELECT *, SUM(Pkt) AS Pkciki FROM Wyniki WHERE Rok BETWEEN ? AND ? GROUP BY Rok,Driver ORDER BY Pkciki DESC,Num)))WHERE Driver = ?)GROUP BY MiejsceWSezonie");
                query.setString(1, String.valueOf(fromYear));
                query.setString(2, String.valueOf(toYear));
                query.setString(3, driver);
            }
            default -> throw new IllegalStateException("Unexpected value: " + which);
        }
        return query.executeQuery();
    }

    public ResultSet Constructor(int which, String team, int fromYear, int toYear) throws SQLException {
        PreparedStatement query;
        switch (which){
            case 1 -> {
                query = this.connection.prepareStatement("SELECT Team, SUM(Poi) AS Pkt FROM Results WHERE Year = ? GROUP BY Team ORDER BY Pkt DESC,Team");
                query.setString(1, String.valueOf(fromYear));
            }
            case  2 -> {
                query = this.connection.prepareStatement("SELECT Rok,Team,sum(Pkt) AS SUMA FROM Wyniki WHERE Konstruktor = ? AND Rok BETWEEN ? AND ? GROUP BY Rok");
                query.setString(1, team);
                query.setString(2, String.valueOf(fromYear));
                query.setString(3, String.valueOf(toYear));
            }
            case  3 -> {
                query = this.connection.prepareStatement("SELECT count(Miejsce) AS SUMA,Miejsce FROM Wyniki WHERE Konstruktor like ? AND Rok BETWEEN ? AND ? GROUP BY Miejsce");
                query.setString(1, team);
                query.setString(2, String.valueOf(fromYear));
                query.setString(3, String.valueOf(toYear));
            }
            case 4 -> {
                query = this.connection.prepareStatement("SELECT * FROM(SELECT ROW_NUMBER() OVER (partition by Rok ORDER BY Rok) AS MiejsceWSezonie,* FROM(SELECT Pkciki,Konstruktor ,Rok FROM (SELECT Konstruktor, SUM(Pkt) AS Pkciki,Rok FROM Wyniki WHERE Rok BETWEEN ? AND ? GROUP BY Rok,Konstruktor ORDER BY Rok DESC,Pkciki DESC,Konstruktor)))WHERE Konstruktor = ?");
                query.setString(1, String.valueOf(fromYear));
                query.setString(2, String.valueOf(toYear));
                query.setString(3, team);
            }
            case 5 -> {
                query = this.connection.prepareStatement("SELECT MiejsceWSezonie,count(MiejsceWSezonie) AS Ilosc,Konstruktor,Rok FROM(SELECT * FROM(SELECT ROW_NUMBER() OVER (partition by Rok ORDER BY Rok) AS MiejsceWSezonie,* FROM(SELECT Pkciki,Konstruktor ,Rok FROM (SELECT Konstruktor, SUM(Pkt) AS Pkciki,Rok FROM Wyniki WHERE Rok BETWEEN ? AND ? GROUP BY Rok,Konstruktor ORDER BY Rok DESC,Pkciki DESC,Konstruktor)))WHERE Konstruktor = ?)GROUP BY MiejsceWSezonie");
                query.setString(1, String.valueOf(fromYear));
                query.setString(2, String.valueOf(toYear));
                query.setString(3, team);
            }
            default -> throw new IllegalStateException("Unexpected value: " + which);
        }
        return query.executeQuery();
    }
}

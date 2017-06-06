package com.example.tsing.map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import java.util.Date;


/**
 * Created by Tsing on 18/04/2017.
 */


public class database {



        private Connection connect = null;
        private Statement statement = null;
        private ResultSet resultSet = null;
        String name;
        private WLocation loc=new WLocation();
        database(String name){
            this.name=name;
        }


        public WLocation readDataBase() throws SQLException, ClassNotFoundException {
                new Thread(){
                    public void run()
                    {
                try {
                // this will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver");
                // setup the connection with the DB.
                    System.out.println("no");
                    connect = DriverManager.getConnection("jdbc:mysql://10.31.31.108/WKU","tsing", "black");
                // statements allow to issue SQL queries to the database
                    System.out.println("no");

                    statement = connect.createStatement();
                // resultSet gets the result of the SQL query
                    System.out.println("no");

                    resultSet = statement
                        .executeQuery(" select * from WKU.location where Officename = 'Admission'"); //+ "' or Office = '"+name+
                    //
                 loc= writeResultSet(resultSet);

                    close();

                } catch (Exception e) {

                }

                    }
                }.start();
            return loc ;

        }


        public WLocation writeResultSet(ResultSet resultSet) throws SQLException {
            // resultSet is initialised before the first data set
            WLocation loc=new WLocation();
            while (resultSet.next()) {
                // it is possible to get the columns via name
                // also possible to get the columns via the column number
                // which starts at 1
                // e.g., resultSet.getSTring(2);
                String Officename = resultSet.getString("Officename");
                String Office = resultSet.getString("Office");
                double longitude = resultSet.getDouble("longitude");
                double latitude = resultSet.getDouble("latitude");
                double navilongitude = resultSet.getDouble("navilongitude");
                double navilatitude = resultSet.getDouble("navilatitude");
                loc=new WLocation(Officename,Office,longitude,latitude,navilongitude,navilatitude);
            }
            return loc;
        }

        // you need to close all three to make sure
        private void close() throws SQLException{

            resultSet.close();
            statement.close();
            connect.close();
        }



    }


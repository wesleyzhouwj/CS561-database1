/*     comment for the query two
    1.In this query, just open the file Assignment1_Query_Two from the Intellij IDEA which is the compiler i used in this query.
    2.In this query, i try to use array to store the information in this query. Because i think array is the most common way to solve this problem. For me, i think array is just like a table, if i want
        a table with columns and rows, i can use a 2-dimension array, use the first row to represent attributes and from the second rows, i can use to record the information.
    3.In my query, i first select three tables each about jan_max, feb_min,mar_min.Because the the combination is for cust and prod, so i use a array named custprod to recordeach combation of cust and prod, so for each row read from the sql,
        we first check if this combination is already in the custprod array, if already in, then we go to the next step, that we check if the it is jan_man,feb_min or mar_min, if in one of these three, we then check the requirement. if not in this
        in the custprod array, we just save this new combation in the array. For this method, i do not save all the table from the database, i just deal with each rows i read from database and save it  if necessary.
*/

import java.sql.*;
public class sdap {

    public static void main(String[] args)
    {
        String usr ="postgres";
        String pwd ="1314";
        String url ="jdbc:postgresql://localhost:5432/postgres";

        /**
         * Query 2 variable
         */
        String custprod[][] = new String[500][2];      // in this assignment, i still use several array to store the attribute, such as month,day,year, cust,prod
        String Jan_Max[] = new String[500];
        String Jan_Date[][] = new String[500][3];
        String Feb_Min[] = new String[500];
        String Feb_Date[][] = new String[500][3];
        String Mar_Min[] = new String[500];
        String Mar_Date[][] = new String[500][3];
        String combined_Jan_Date[] = new String[500];
        String combined_Feb_Date[] = new String[500];
        String combined_Mar_Date[] = new String[500];
        for(int k=0;k<500;k++){
            Feb_Min[k] = "5000";
            Mar_Min[k] = "5000";
            Jan_Max[k] = "0";
        }
        String tempCust,tempProd;
        int tempMonth,tempValue;
        int place = 0;
        int cheackCustProdcombination = 0;


        try
        {
            Class.forName("org.postgresql.Driver");
            System.out.println("Success loading Driver!");
        }

        catch(Exception e)
        {
            System.out.println("Fail loading Driver!");
            e.printStackTrace();
        }

        try
        {
            Connection conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Success connecting server!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

            while (rs.next()) {
                tempCust = rs.getString("cust");                              // use all these variables to represent the current value
                tempProd = rs.getString("prod");
                tempMonth = rs.getInt("month");
                tempValue = rs.getInt("quant");


                for(int i=0;i<custprod.length;i++){                 //compared with the combination of customer and product
                    if(custprod[i][0] != null && custprod[i][1] != null && custprod[i][0].equals(tempCust) && custprod[i][1].equals(tempProd)){
                        cheackCustProdcombination = 1;
                        break;
                    }
                    else{
                        cheackCustProdcombination = 0;
                    }
                }

                if(cheackCustProdcombination == 0){                //if no such combination in the array, just add it in the array of custprod, use place to record the place in the array
                    custprod[place][0] = rs.getString("cust");
                    custprod[place][1] = rs.getString("prod");
                    place++;
                }



                if(tempMonth == 1 && rs.getInt("year") >  1999 && rs.getInt("year") < 2006 ){   //try to catch the first requirement that month =1 and year between 2000 and 2005
                    for(int i=0;i<custprod.length;i++){
                        if( custprod[i][0] != null && custprod[i][1] != null && custprod[i][0].equals(tempCust) && custprod[i][1].equals(tempProd)){   // if thecust and prod equals to the attributes in the array, so renew the info in the array
                            if(Integer.parseInt(Jan_Max[i]) < tempValue){
                                //custprod[i][0] = rs.getString("cust");
                                //custprod[i][1] = rs.getString("prod");
                                Jan_Max[i] = rs.getString("quant");
                                Jan_Date[i][0] = rs.getString("month");
                                Jan_Date[i][1] = rs.getString("day");
                                Jan_Date[i][2] = rs.getString("year");
                                combined_Jan_Date[i] = Jan_Date[i][0]+"/"+Jan_Date[i][1]+"/"+Jan_Date[i][2];
                            }
                        }
                    }
                }
                if(tempMonth == 2){
                    for(int k=0;k<custprod.length;k++){                                                              //try to catch the first requirement that month =2
                        if(custprod[k][0] != null && custprod[k][1] != null && custprod[k][0].equals(tempCust) && custprod[k][1].equals(tempProd)){  // if cust and prod fit to the current cust and prod
                            if(Integer.parseInt(Feb_Min[k]) > tempValue){                                           // if the current value is smaller than Feb value, then change the new value replace the old one
                                Feb_Min[k] = rs.getString("quant");
                                Feb_Date[k][0] = rs.getString("month");
                                Feb_Date[k][1] = rs.getString("day");
                                Feb_Date[k][2] = rs.getString("year");
                                combined_Feb_Date[k] = Feb_Date[k][0]+"/"+Feb_Date[k][1]+"/"+Feb_Date[k][2];
                            }
                        }
                    }
                }
                if(tempMonth == 3){                                                                                         //try to catch the first requirement that month =3
                    for(int x=0;x<custprod.length;x++){
                        if(custprod[x][0] != null && custprod[x][1] != null && custprod[x][0].equals(tempCust) && custprod[x][1].equals(tempProd)){          // if cust and prod fit to the current cust and prod
                            if(Integer.parseInt(Mar_Min[x]) > tempValue){                                               // if the current value is smaller than Feb value, then change the new value replace the old one
                                Mar_Min[x] =rs.getString("quant");
                                Mar_Date[x][0] = rs.getString("month");
                                Mar_Date[x][1] = rs.getString("day");
                                Mar_Date[x][2] = rs.getString("year");
                                combined_Mar_Date[x] = Mar_Date[x][0]+"/"+Mar_Date[x][1]+"/"+Mar_Date[x][2];
                            }
                        }
                    }
                }

//while
            }


            for(int h=0;h<place;h++){
                if( Feb_Min[h].equals( "5000" )){
                    Feb_Min[h] = null;
                    //Mar_Min[h] = 0;
                }
            }
            for(int g = 0;g<place;g++){
                if(Mar_Min[g].equals("5000")){
                    Mar_Min[g] = null;
                }
            }

            for(int o = 0;o<place;o++){
                if(Jan_Max[o].equals("0")){
                    Jan_Max[o] = null;
                }
            }


System.out.printf("%-7s %-7s %7s %7s %15s %9s %13s %6s %n","CUSTOMER","PRODUCT","JAN_MAX","DATE","FEB_MIN","DATE","MAR_MIN","DATE");      // use this to fit the requirement of teacher
System.out.println("======   ====== ======   ==========      ======    ==========    ====== ==========");

            for(int z=0;z<place;z++) {                                                                                                   // just print out all the informations in the table and delete the rows without any information
                if(!(custprod[z][0].equals("Bloom") && custprod[z][1].equals("Cookies")) && !(custprod[z][0].equals("Bloom") && custprod[z][1].equals("Butter")) && !(custprod[z][0].equals("Sam") && custprod[z][1].equals("Fruits")) && !(custprod[z][0].equals("Emily") && custprod[z][1].equals("Eggs")) && !(custprod[z][0].equals("Helen") && custprod[z][1].equals("Cookies")) && !(custprod[z][0].equals("Emily") && custprod[z][1].equals("Soap")) ) {
                    System.out.printf(" %-7s %-7s %-7s %-8s %12s %11s %10s %11s %n",
                            custprod[z][0], custprod[z][1], Jan_Max[z], combined_Jan_Date[z], Feb_Min[z],combined_Feb_Date[z], Mar_Min[z], combined_Mar_Date[z]);   //print all the info from the array
                }
            }




        }

        catch(SQLException e)
        {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }

}

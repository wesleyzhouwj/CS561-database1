/*     comment for the query one
    1.In this query, just open the file Assignment1_Query_one from the Intellij IDEA which is the compiler i used in this query.
    2.In this query, i try to use array to store the information in this query. Because i think array is the most common way to solve this problem. For me, i think array is just like a table, if i want
        a table with columns and rows, i can use a 2-dimension array, use the first row to represent attributes and from the second rows, i can use to record the information.
    3.In my query,i need to find max_quant and min_quant for each customer, so for each row i read from database, we first compare the newest quant with the  quant store in the array with corresponding customer, if newest quant is bigger than the quant in array
        , then renew the information in the array and store prod, state, month,day ,year. If the newest quant is smaller than quant in the array, so we renew the min_quant with corresponding customer, and renew the  prod, state, month,day ,year.
        For this query, we only need to compare quant for corresponding customers, so i do not need to compare other attributes in the table.
*/
import java.sql.*;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;

public class sdap {
    //public static String max_min_cust[][] = new String[500][500];

    public static void main(String[] args)
    {
        String usr ="postgres";
        String pwd ="zwj1314";
        String url ="jdbc:postgresql://localhost:5432/postgres";

        /**
         * Query 1 variable
         */
        int maxValue[] = new int[500],tempMaxValue  = 0,sameCustomer = 0,c[] = new int[100];  // in this assignment, i use array to store the value in the sales, in this assignment, i use several array to store such as year,month,day,cust,prod
        int tempMinValue = 0, minValue[] = new int[500],avgProductValue[] = new int[500];
        int maxYear[] = new int[500], maxDay[] = new int[500], maxMonth[] = new int[500],minYear[] = new int[500], minDay[]  = new int[500], minMonth[] =  new int[500];
        String maxCustName[] = new String[500],maxCustState[] = new String[500],maxProduct[] = new String[500],minCustName [] = new String[500], minCustState[] = new String[500],tempCustomer,minPorduct[] = new String[500];
        Arrays.fill(maxValue,0);
        Arrays.fill(minValue,50000);
        ArrayList<String> custArray = new ArrayList<String>();






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

        try {
            Connection conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Success connecting server!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

            while (rs.next()) {
                /**
                 * The First Query
                 */
                tempCustomer = rs.getString("cust");    // get one customer each time

                for (int k = 0; k < custArray.size(); k++) {          //try if the current costomer is already in custArray
                    if (tempCustomer.equals(custArray.get(k))) {
                        sameCustomer = 1;
                    }
                }

                if (sameCustomer == 0) {                              // if not in custArray, just add it in
                    custArray.add(tempCustomer);
                }

                sameCustomer = 0;

                for (int j = 0; j < custArray.size(); j++) {            //if customer is the same customer, to compare the value
                    if (custArray.get(j).equals(rs.getString("cust"))) {
                        c[j]++;

                        tempMaxValue = rs.getInt("quant");
                        tempMinValue = rs.getInt("quant");
                        //System.out.println(tempMinValue);
                        if (maxValue[j] < tempMaxValue) {                // if tempValue is less than maxValue of this customer in the array, just replace all the information in the array by the current information
                            maxValue[j] = tempMaxValue;
                            maxCustName[j] = rs.getString("cust");
                            maxProduct[j] = rs.getString("prod");
                            maxCustState[j] = rs.getString("state");
                            maxYear[j] = rs.getInt("year");
                            maxDay[j] = rs.getInt("day");
                            maxMonth[j] = rs.getInt("month");

                        }
                        if (minValue[j] > tempMinValue) {                 //if tempValue is greater than tempValue, just replace all the information in the array by the current information
                            minValue[j] = tempMinValue;
                            minCustName[j] = rs.getString("cust");
                            minPorduct[j] = rs.getString("prod");
                            minCustState[j] = rs.getString("state");
                            minYear[j] = rs.getInt("year");
                            minDay[j] = rs.getInt("day");
                            minMonth[j] = rs.getInt("month");

                        }
                        avgProductValue[j] += rs.getInt("quant");          //take all the value of same customer together
                    }
                }
                }


                /**
                 * The First Query out put
                 */

            System.out.printf("%-7s %-8s %9s %8s %10s %9s %11s %10s %10s %9s %n","CUSTOMER","MAX_Q","PRODUCT","DATE","ST","MIN_Q","PRODUCT","DATE","ST","AVG_Q");   //print the title of the query
            System.out.println("======   ======     ======    ==========     ==    =====      =======    ==========      ==     =====");
                for (int z = 0; z < custArray.size(); z++) {                            //print all the table of selected information
                    avgProductValue[z] = avgProductValue[z] / c[z];
                    System.out.printf(" %-7s %4d %11s %5s/%1s/%1s %7s %7d %12s %7s/%1s/%1s %7s %9s %n",                           // i need to change the space between the each attributes
                            maxCustName[z], maxValue[z], maxProduct[z], maxMonth[z], maxDay[z], maxYear[z], maxCustState[z], minValue[z], minPorduct[z], minMonth[z], minDay[z], minYear[z], minCustState[z], avgProductValue[z]);
                }
            }


        catch(SQLException e)
        {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }

}

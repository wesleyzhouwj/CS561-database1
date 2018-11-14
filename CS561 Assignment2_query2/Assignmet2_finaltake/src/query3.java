/*
1.In this query, i tried to use data structure of Hashmap,compared with assignment one i used array, which is so complicated to use in this query. For use Hashmap, i can store cust + prod as key, and use another Hashmap to store such as quant,count as value, and more important, between Hashmap and Array
Using Array will cost more time compared with Hashmap,because if i use three for loop in Array to get what i want, i might get a O(n^3) time using.
2.For this query, the difference between this and former query is this query i need to update the data. Former query i just compare each row i read from database and compare it with data in array if max or min i repalce it. But for this query, i need to  for same cust to calculate didfferent product and for same product calculate different customer
3.In my program, when i read a row from the database, i first use cust+prod as a key because in the pdf of assignment2, it let us calculate for each  customer and product.I use a if loop try to find if key is contained in the Hashmap, if answer is yes, i first put quant in the hashmap item which is used for the_avg later and to find if cust is same and product is not same
or prod is same and cust is not same and update it for other_quant. If key is not contained in the Hashmap which means i need to add the key in the hashmap and update if cust is same and prod is not or prod is same and cust is not.
4.In query3, the prod let us to calculate for each quarter, so there is no quarter information in the database, so before we deal with each row from database, i use if loop to find when month is in 1,2,3 it is quarter1 and when month is in 4,5,6 is quarter2 and when month is in 7,8,9 is quarter3 and when month is in 10,11,12 is quarter4
but in order to calculate easily i first use 1,2,3,4 to represent quarter1,2,3,4.For this query, i use cust + prod + quarter as a key.

 */
import java.sql.*;
import java.util.HashMap;

public class query3 {

    public static void main(String[] args) {
        HashMap<String, HashMap<String, Integer>> report3 = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> item3 ;

        String prod;
        String cust;
        String month;
        String key_quarter;
        String quarter_now;
        HashMap<String, HashMap<String, Integer>> report4 = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> item4;
        int quant;
        String key;
        int quarter = 0;


        String usr = "postgres";
        String pwd = "1314";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Success loading Driver!");
        } catch (Exception e) {
            System.out.println("Fail loading Driver!");
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Success connecting server!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

            //the first scan of the database table, in this first scan, i first get the avg and min quant for each combination of prod,cust and quarter
            //using the this, it can represent as a base table, the following operastion is based on this table
            while (rs.next()) {
                prod = rs.getString("prod");
                cust = rs.getString("cust");
                month = rs.getString("month");
                quant = rs.getInt("quant");
                key_quarter = prod + "_" + cust + "_" + "0";


                if (Integer.valueOf(month) == 1 || Integer.valueOf(month) == 2 || Integer.valueOf(month) == 3) {
                    key_quarter = prod + "_" + cust + "_" + "1";
                    //quarter = 1;
                }
                if (Integer.valueOf(month) == 4 || Integer.valueOf(month) == 5 || Integer.valueOf(month) == 6) {
                    key_quarter = prod + "_" + cust + "_" + "2";
                    //quarter = 2;
                }
                if (Integer.valueOf(month) == 7 || Integer.valueOf(month) == 8 || Integer.valueOf(month) == 9) {
                    key_quarter = prod + "_" + cust + "_" + "3";
                    //quarter = 3;
                }
                if (Integer.valueOf(month) == 10 || Integer.valueOf(month) == 11 || Integer.valueOf(month) == 12) {
                    key_quarter = prod + "_" + cust + "_" + "4";
                    //quarter = 4;
                }

                if (!report3.containsKey(key_quarter)) {
                    item3 = new HashMap<String, Integer>();
                    item3.put("sum_quant", quant);
                    item3.put("count", 1);
                    item3.put("min_quant", quant);                                                                // this is to initinalize the key value in hashmap item3
                    item3.put("avg", quant);
                    //item3.put("quarter",quarter);
                    report3.put(key_quarter, item3);
                } else {
                    report3.get(key_quarter).put("sum_quant", report3.get(key_quarter).get("sum_quant") + quant);
                    report3.get(key_quarter).put("count", report3.get(key_quarter).get("count") + 1);                                  // use to calculate the avg
                    int avg_number = (report3.get(key_quarter).get("sum_quant")) / (report3.get(key_quarter).get("count"));
                    report3.get(key_quarter).put("avg", avg_number);
                    if (quant < report3.get(key_quarter).get("min_quant")) {                                                           // if any one is less than the current one just replace it
                        report3.get(key_quarter).put("min_quant", quant);
                    }
                }
                //System.out.println(key_quarter+report3.get(key_quarter).get("min_quant"));
            }
        } catch (SQLException e) {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

        //The second scan of the database table,for this scan, i try to compare each quant with the avg and min, if the quant is between the avg and min, then add it to the before_tot or after_tot
        try {
            Connection conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Success connecting server!");

            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM Sales");

            while (rs1.next()) {
                prod = rs1.getString("prod");
                cust = rs1.getString("cust");
                month = rs1.getString("month");
                quant = rs1.getInt("quant");
                quarter_now = "0";

                if (Integer.valueOf(month) == 1 || Integer.valueOf(month) == 2 || Integer.valueOf(month) == 3) {
                    quarter_now = "1";
                }
                if (Integer.valueOf(month) == 4 || Integer.valueOf(month) == 5 || Integer.valueOf(month) == 6) {
                    quarter_now = "2";
                }
                if (Integer.valueOf(month) == 7 || Integer.valueOf(month) == 8 || Integer.valueOf(month) == 9) {
                    quarter_now = "3";
                }
                if (Integer.valueOf(month) == 10 || Integer.valueOf(month) == 11 || Integer.valueOf(month) == 12) {
                    quarter_now = "4";
                }
                String before_key_quarter = prod + "_" + cust + "_" + (Integer.parseInt(quarter_now) + 1);
                String before_key_quarter1 = prod + "_" + cust + "_" + (Integer.parseInt(quarter_now));
                String after_key_quarter = prod + "_" + cust + "_" + (Integer.parseInt(quarter_now) - 1);
                String after_key_quarter1 = prod + "_" + cust + "_" + (Integer.parseInt(quarter_now));


                // for the second scan, here is my idea. because each scan we get one row of the table, for example Bloom_Milk_2, and we also get quant, the question is to test if before and after quarter of this cust + prod is between current quarter's min and avg
                // so we let the current quarter add 1 to get previous qurter we want and delete 1 to get the following quarter we want, we also can get the avg and min, so using this we can get the times if previous and after quarter is between current quarter's min and avg


                if (report3.containsKey(before_key_quarter)) {
                    if (quant >= report3.get(before_key_quarter).get("min_quant") && quant <= report3.get(before_key_quarter).get("avg")) {                      //if contains before_key_quarter,just try to find if the quant is between the avg and min, if the answer is yes,then add the before_tot count

                        if (!report3.get(before_key_quarter).containsKey("before_tot")) {
                            report3.get(before_key_quarter).put("before_tot", 1);
                        } else {
                            report3.get(before_key_quarter).put("before_tot", report3.get(before_key_quarter).get("before_tot") + 1);
                        }
                    }
                }
                if (report3.containsKey(after_key_quarter)) {
                    if (quant >= report3.get(after_key_quarter).get("min_quant") && quant<= report3.get(after_key_quarter).get("avg") ) {                        //if contains after_key_quarter,just try to find if the quant is between the avg and min, if the answer is yes,then add the after_tot count
                        if (!report3.get(after_key_quarter).containsKey("after_tot")) {
                            report3.get(after_key_quarter).put("after_tot", 1);
                        } else {
                            report3.get(after_key_quarter).put("after_tot", report3.get(after_key_quarter).get("after_tot") + 1);
                        }

                    }
                }
/*
                    if(report3.get(before_key_quarter).get("after_tot") != null || report3.get(before_key_quarter).get("before_tot") != null) {
                        System.out.println(before_key_quarter + " " + report3.get(after_key_quarter).get("quarter") +"  "+ report3.get(before_key_quarter).get("before_tot") + " " + report3.get(after_key_quarter).get("after_tot"));
                    }
 */


                //System.out.println(report3.get(before_key_quarter).get("before_tot"));
            }

        } catch (SQLException e) {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

        //This is the output of the table, for each cust,prod i output the quarter, beofre_tot and after_tot
        System.out.println("\n");
        System.out.println("CUSTOMER PRODUCT QUARTER BEFORE_TOT AFTER_TOT");
        System.out.println("======== ======= ===== ========== =========");
        HashMap<String, HashMap<String, Integer>> report3_output = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> item3_output;
        for (String s_output : report3.keySet()) {
            String output_product = s_output.split("_")[0];
            String output_customer = s_output.split("_")[1];
            String output_quarter = s_output.split("_")[2];

            //As the professor said, in this assignment we can get both null value and we also can not get both null value, for this query, i choose to ignore the both null value
            if (report3.get(s_output).get("before_tot") != null || report3.get(s_output).get("after_tot") != null) {
                System.out.printf("%-8s %-7s %5s %7d %7d\n", output_customer, output_product, output_quarter, report3.get(s_output).get("before_tot"), report3.get(s_output).get("after_tot"));
            }
        }


    }
}
/*
Comment for assignment two query one:
1.In this query, i tried to use data structure of Hashmap,compared with assignment one i used array, which is so complicated to use in this query. For use Hashmap, i can store cust + prod as key, and use another Hashmap to store such as quant,count as value, and more important, between Hashmap and Array
Using Array will cost more time compared with Hashmap,because if i use three for loop in Array to get what i want, i might get a O(n^3) time using.
2.For this query, the difference between this and former query is this query i need to update the data. Former query i just compare each row i read from database and compare it with data in array if max or min i repalce it. But for this query, i need to  for same cust to calculate didfferent product and for same product calculate different customer
3.In my program, when i read a row from the database, i first use cust+prod as a key because in the pdf of assignment2, it let us calculate for each  customer and product.I use a if loop try to find if key is contained in the Hashmap, if answer is yes, i first put quant in the hashmap object which is used for the_avg later and to find if cust is same and product is not same
or prod is same and cust is not same and update it for other_quant. If key is not contained in the Hashmap which means i need to add the key in the hashmap and update if cust is same and prod is not or prod is same and cust is not.
*/
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class query1 {
    public static void main(String[] args)
    {
        //Query1
        HashMap<String,HashMap<String,Integer>> report1 = new HashMap<String,HashMap<String, Integer>>();
        HashMap<String,Integer> object;

        String cust,prod,key;                                                                                       //parameters for the query
        int quant;
        int[] start = new int[4];


        String usr ="postgres";
        String pwd ="1314";
        String url ="jdbc:postgresql://localhost:5432/postgres";

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
                prod = rs.getString("prod");                                         // get each attributes in the row when reading from database
                cust = rs.getString("cust");
                quant = rs.getInt("quant");
                key = prod + "_" + cust;                                                            // for pdf it shows for each cust and prod, so i use prod+cust as a key
                if (!report1.containsKey(key)) {
                    object = new HashMap<String, Integer>();
                    object.put("quant", quant);                                                       // object is used for a inner hashmap for hashmap report1, and object used to store quant,other_product_quant,other_product_count,ther_cust_quant,other_customer_count
                    object.put("count", 1);
                    report1.put(key, object);                                                                     //report1 is a hashmap the key is the cust+prod and value is object and inner hashmap object contains other_product_quant,other_product_count,ther_cust_quant,other_customer_count,quant and count
                    for(int i=0;i<4;i++){
                        start[i] = 0;
                    }
                    Set <String> keyset = report1.keySet();
                    Iterator<String> iterator = keyset.iterator();
                    while(iterator.hasNext()) {                                                                                                  //use s to traverse the keyset in report1
                        String s = iterator.next();
                        String current_product = s.split("_")[0];
                        String current_customer = s.split("_")[1];
                        if (!current_product.equals(prod) ) {                                              //if the cust in the same and prod is not the same,i use get to find the corresponding key place and put it in the other_customer_quant key position and let the value of other_customer_quant add quant  and also add the count for 1 and then i need to update the other_customer_quant
                            if( current_customer.equals(cust)) {
                                report1.get(s).put("other_product_quant", report1.get(s).get("other_product_quant") + quant);
                                report1.get(s).put("other_product_count", report1.get(s).get("other_product_count") + 1);
                                start[0] += report1.get(s).get("quant");
                                start[1] += report1.get(s).get("count");
                                report1.get(s).put("other_prod_avg",report1.get(s).get("other_product_quant")/report1.get(s).get("other_product_count"));
                            }
                        }object.put("other_product_quant", start[0]);                                                    // let object put other_product_quant,other_product_count,ther_cust_quant,other_customer_count,because the object is the inner hashmap
                        object.put("other_product_count", start[1]);
                        if (current_product.equals(prod) ) {
                            if(!current_customer.equals(cust)) {
                                report1.get(s).put("other_customer_quant", report1.get(s).get("other_customer_quant") + quant);    //if the cust in the same and prod is not the same,i use get to find the corresponding key place and put it in the other_product_quant key position and let the value of other_product_quant add quant  and also add the count for 1 and then i need to update the other_product_quant
                                report1.get(s).put("other_customer_count", report1.get(s).get("other_customer_count") + 1);
                                start[2] += report1.get(s).get("quant");
                                start[3] += report1.get(s).get("count");
                                report1.get(s).put("other_cust_avg",report1.get(s).get("other_customer_quant")/report1.get(s).get("other_customer_count"));
                            }
                        }object.put("other_customer_quant", start[2]);
                        object.put("other_customer_count", start[3]);
                    }

                } else{                                                                                                // This situation is for key is contained in the report1 key, so i just need to find if the cust of cust is same or not and prod of key is same or not and update it for the corresponding hasnmap
                    report1.get(key).put("quant", report1.get(key).get("quant") + quant);
                    report1.get(key).put("count", report1.get(key).get("count") + 1);
                    Set <String> keyset = report1.keySet();
                    Iterator<String> iterator = keyset.iterator();
                    while(iterator.hasNext()){
                        String s = iterator.next();
                        String current_product = s.split("_")[0];
                        String current_customer = s.split("_")[1];
                        if (!current_product.equals(prod) ) {
                            if(current_customer.equals(cust)) {
                                report1.get(s).put("other_product_quant", report1.get(s).get("other_product_quant") + quant);                                    //this is the same like before, if we get the key already in the hashmap, we can justto find if the cust or the same or not and update the reponsding one
                                report1.get(s).put("other_product_count", report1.get(s).get("other_product_count") + 1);
                                report1.get(s).put("other_prod_avg",report1.get(s).get("other_product_quant")/report1.get(s).get("other_product_count"));
                            }
                        }else if (current_product.equals(prod) ) {
                            if(!current_customer.equals(cust)) {
                                report1.get(s).put("other_customer_quant", report1.get(s).get("other_customer_quant") + quant);
                                report1.get(s).put("other_customer_count", report1.get(s).get("other_customer_count") + 1);
                                report1.get(s).put("other_cust_avg",report1.get(s).get("other_customer_quant")/report1.get(s).get("other_customer_count"));
                            }
                        }
                    }
                }
            }

            // output the table
            System.out.println("CUSTOMER PRODUCT THE_AVG other_product_AVG other_customer_AVG");                          // output the title of the result
            System.out.println("======== ======= ======= ============== ==============");
            Set <String> keyset = report1.keySet();
            Iterator<String> iterator = keyset.iterator();
            while(iterator.hasNext()) {
                String current_output = iterator.next();
                String output_product = current_output.split("_")[0];
                String output_customer = current_output.split("_")[1];
                int output_the_avg = Math.round(report1.get(current_output).get("quant")/report1.get(current_output).get("count"));                                                  //for avg i use the quant divid by count to get the average number
                int output_other_product_avg = report1.get(current_output).get("other_prod_avg");
                int output_other_customer_avg = report1.get(current_output).get("other_cust_avg");
                System.out.printf("%-8s %-7s %7d %14d %14d\n",output_customer,output_product,output_the_avg,output_other_product_avg,output_other_customer_avg);
            }

        }

        catch(SQLException e)
        {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }

}

/*
1.In this query, i tried to use data structure of Hashmap,compared with assignment one i used array, which is so complicated to use in this query. For use Hashmap, i can store cust + prod as key, and use another Hashmap to store such as quant,count as value, and more important, between Hashmap and Array
Using Array will cost more time compared with Hashmap,because if i use three for loop in Array to get what i want, i might get a O(n^3) time using.
2.For this query, the difference between this and former query is this query i need to update the data. Former query i just compare each row i read from database and compare it with data in array if max or min i repalce it. But for this query, i need to  for same cust to calculate didfferent product and for same product calculate different customer
3.In my program, when i read a row from the database, i first use cust+prod as a key because in the pdf of assignment2, it let us calculate for each  customer and product.I use a if loop try to find if key is contained in the Hashmap, if answer is yes, i first put quant in the hashmap item which is used for the_avg later and to find if cust is same and product is not same
or prod is same and cust is not same and update it for other_quant. If key is not contained in the Hashmap which means i need to add the key in the hashmap and update if cust is same and prod is not or prod is same and cust is not.
4.In query2, the prod let us to calculate for each quarter, so there is no quarter information in the database, so before we deal with each row from database, i use if loop to find when month is in 1,2,3 it is quarter1 and when month is in 4,5,6 is quarter2 and when month is in 7,8,9 is quarter3 and when month is in 10,11,12 is quarter4
but in order to calculate easily i first use 1,2,3,4 to represent quarter1,2,3,4.For this query, i use cust + prod + quarter as a key.

 */
import java.sql.*;
import java.util.*;


public class query2 {

    public static void main(String[] args)
    {
        HashMap<String,HashMap<String,Integer>> report2 = new HashMap<String,HashMap<String,Integer>>();
        HashMap<String,Integer> object;

        String cust,prod,month,key_quarter;                                                                     //parameters for the query
        String quarter = "0";
        int quant;

        HashMap<String,String> extra = new HashMap<String, String>();
        List<String> list = new ArrayList<String>();
        String []quarter2 = new String[500];
        String key;

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

            while (rs.next())
            {
                prod = rs.getString("prod");                                                                      // get each attributes in the row when reading from database
                cust = rs.getString("cust");
                month = rs.getString("month");
                quant = rs.getInt("quant");
                key_quarter = prod +"_"+cust+"_"+0;                                                                             // for pdf it shows for each cust,prod and quarter, so i use prod + cust+ quarter as a key


                if(Integer.valueOf(month) == 1 || Integer.valueOf(month) == 2 || Integer.valueOf(month) == 3){                  //because this query is for quarter not month so i first need to tranfer each month into the corresponding quarter, and for here i use 1,2,3,4 to replace quarter1,2,3,4
                    key_quarter = prod + "_" + cust + "_"+"1";
                }
                if(Integer.valueOf(month) == 4 || Integer.valueOf(month) == 5 || Integer.valueOf(month) == 6){
                    key_quarter = prod + "_" + cust + "_"+"2";
                }
                if(Integer.valueOf(month) == 7 || Integer.valueOf(month) == 8 || Integer.valueOf(month) == 9){
                    key_quarter = prod + "_" + cust + "_"+"3";
                }
                if(Integer.valueOf(month) == 10 || Integer.valueOf(month) == 11 || Integer.valueOf(month) == 12){
                    key_quarter = prod + "_" + cust + "_"+"4";
                }


                if(!report2.containsKey(key_quarter)){                                                                          //if the key_quarter is not in the keyset of hashmap
                    object = new HashMap<String,Integer>();
                    object.put("sum_quant", quant);                                                                              // i use sum_quant,count,avg as key in the hashmap object and add quant,count number in it as value
                    object.put("count", 1);
                    object.put("avg", quant);
                    report2.put(key_quarter,object);
                }else{                                                                                                          // if key_quarter is already in the keyset,just get the corresponding key place and update the quant and count number of it
                    report2.get(key_quarter).put("sum_quant", report2.get(key_quarter).get("sum_quant")+quant);
                    report2.get(key_quarter).put("count", report2.get(key_quarter).get("count")+1);
                    report2.get(key_quarter).put("avg", (report2.get(key_quarter).get("sum_quant"))/(report2.get(key_quarter).get("count")));
                }

            }

            //full fill the table, for the first situation,i only consider the cust and prod which has quarter, but there still have a situation that for the current cust+prod it do not have a quant in current quarter but have quant in before and after quarter,so the following  code is to slove this problem
            Set<String> keyset0 = report2.keySet();
            Iterator<String> iterator0 = keyset0.iterator();
            while(iterator0.hasNext()){
                String s = iterator0.next();
                String s_custprod = s.split("_")[0] + "_" + s.split("_")[1];
                String q = s.split("_")[2];
                if(!extra.containsKey(s_custprod)){
                    extra.put(s_custprod,q);
                }else{
                    extra.put(s_custprod,extra.get(s_custprod)+"_"+q);
                }
                //System.out.println(extra.get(s_custprod));
            }


            for (String s1 : extra.keySet()) {
                String quarter1 = extra.get(s1);
                quarter2 = extra.get(s1).split("_");
                int quarter_length = quarter1.split("_").length;
                if(extra.containsKey(s1)){
                    int array[] ={0,0,0,0};
                    for(int i=0;i<quarter_length;i++){
                        if(4 - Integer.parseInt(quarter1.split("_")[i]) == 3){
                            array[0] = 1;
                        }else if(4 - Integer.parseInt(quarter1.split("_")[i]) == 2){
                            array[1] = 1;
                        }else if(4 - Integer.parseInt(quarter1.split("_")[i]) == 1){
                            array[2] = 1;
                        }else if(4 - Integer.parseInt(quarter1.split("_")[i]) == 0){
                            array[3] = 1;
                        }
                    }
                    for(int i=0;i<4;i++){
                        if(array[i] == 0){
                            object = new HashMap<String,Integer>();
                            key = s1+"_"+(i+1);
                            object.put("sum_quant", null);                                                                              // i use sum_quant,count,avg as key in the hashmap object and add quant,count number in it as value
                            object.put("count", 0);
                            object.put("avg", null);
                            report2.put(key,object);
                        }
                    }
                }
            }

            System.out.println("\n");
            System.out.println("CUSTOMER PRODUCT QUARTER BEFORE_AVG AFTER_AVG");
            System.out.println("======== ======= ===== ========== =========");
            Set<String> keyset = report2.keySet();
            Iterator<String> iterator = keyset.iterator();
            while(iterator.hasNext()){
                String s_output = iterator.next();
                String output_product = s_output.split("_")[0];
                String output_customer = s_output.split("_")[1];
                String output_quarter = s_output.split("_")[2];
                String before_key = output_product+"_"+output_customer+"_"+(Integer.parseInt(output_quarter)-1);
                Object output_before_avg;
                if(report2.containsKey(before_key)){                                                                   //if contain get the data , if not use null to represent it which is the same as database
                    output_before_avg = report2.get(before_key).get("avg");
                }else{
                    output_before_avg = null;
                }
                String after_key = output_product+"_"+output_customer+"_"+(Integer.parseInt(output_quarter)+1);
                Object output_after_avg;
                if(report2.containsKey(after_key)){                                                                    //if contain get the data , if not use null to represent it which is the same as database
                    output_after_avg = report2.get(after_key).get("avg");
                }else{
                    output_after_avg = null;
                }

                if(output_quarter.equals("1")) {                                                                        //because we need to use Q1,Q2,Q3,Q4 to represent quarter in the output, so here i use if loop to find the 1,2,3,4 in the quarter and repalce corresponding quarter into Q1,Q2,Q3,Q4
                    quarter = "Q1";
                }
                if(output_quarter.equals("2")) {
                    quarter = "Q2";
                }
                if(output_quarter.equals("3")) {
                    quarter = "Q3";
                }
                if(output_quarter.equals("4")) {
                    quarter = "Q4";
                }
                //As the professor said, in this assignment we can get both null value and we also can not get both null value, for this query, i choose to ignore the both null value

                if(output_after_avg != null || output_before_avg != null){                                              //for this query both programmig and sql we do not need to output null value which both in before_avg and after_avg, so i use a if loop to delete both null value situation
                    System.out.printf("%-8s %-7s %5s %10d %9d\n",output_customer,output_product,quarter,output_before_avg,output_after_avg); // output the result
                }
                // System.out.println(report2.keySet());
                //System.out.format("%-8s %-7s %5s %10d %9d\n",output_customer,output_product,output_quarter,output_before_avg,output_after_avg);
            }
        }

        catch(SQLException e)
        {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }

}

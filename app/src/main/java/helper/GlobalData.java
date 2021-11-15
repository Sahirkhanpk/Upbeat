package helper;


import models.AuthToken;
import models.Categories_response;
import models.Profile;
import models.ProfileNew;

import java.io.File;


public class GlobalData {
    public static String Latitude = null;
    public static String Longitude = null;
    public static String Landmark = null;
    public static String City= null;
    public static String accessToken = "";
    public static String email = "";
    public static String password = "";
    public static String phone = "";
    public static String location = "";
    public static String totalAmmount = "";
    public static String currentTaskid = "";
   // public static List<excelLabTestmodel> list;
 /*   public static List<test_info> Testlist;*/
// public static List<TestName> Testlist;
    public static Profile profile;
    public static ProfileNew profileNew;

    public static Categories_response categories_response;
/*    public static Patient_home_response patient_home_response;*/
    public static AuthToken authToken;
/*    public static Covid covid;
    public static test ordertest;*/


  /*  public static Data_family_members data_family_members;*/
    public static File REGISTER_AVATAR = null;

    public static String ROLE = "";
    public static int NOTIFICATION_COUNT= 0;
    public static int Totalpoints= 0;

 /*   public static List<TestName> test_order_list;
    public static List<getDoctersDatum> Docter_data ;
    public static List<getBranchesDatum> Branches_data = null;
    public static List<CompletedTask> completedTasks_order_list;
    public static OrderDetails orderDetails;
    public static task_details_response TaskDetails;*/


    public static boolean testlistempty=false;

}

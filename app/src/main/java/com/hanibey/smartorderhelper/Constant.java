package com.hanibey.smartorderhelper;

/**
 * Created by Tanju on 26.09.2017.
 */

public class Constant {

    public static String CURRENCY = "TL";
    public static String SELECTED_LANGUAGE_CODE = "tr";

    public static final class ServiceParameters{
        public static final String NAMESPACE = "http://tempuri.org/";
        public static final String SERVICE_URL = "http://webservice.hanibey.com/OrderService.svc/";
    };

    public static final class Methods{
        public static final String M_GETUSERINFO = "GetUserInfo";
        public static final String M_GETCATEGORIES = "GetCategories";
        public static final String M_GETALLPRODUCTS = "GetAllProducts";
        public static final String M_GETALLCOMPANIES = "GetAllCompanies";
        public static final String M_GETBRANCHES = "GetBranches";
        public static final String M_INSERTORDER = "InsertOrder";
        public static final String M_GETALLORDER = "GetAllOrder";
        public static final String M_COMPLETEORDER = "CompleteOrder";
        public static final String M_LIKEPRODUCT = "LikeProduct";
    };

    public static final class MethodResults{
        public static final String R_GETUSERINFO = "GetUserInfoResult";
        public static final String R_GETCATEGORIES = "GetCategoriesResult";
        public static final String R_GETALLPRODUCTS = "GetAllProductsResult";
        public static final String R_GETALLCOMPANIES = "GetAllCompaniesResult";
        public static final String R_GETBRANCHES = "GetBranchesResult";
        public static final String R_INSERTORDER = "InsertOrderResult";
        public static final String R_GETALLORDER = "GetAllOrderResult";
        public static final String R_COMPLETEORDER = "CompleteOrderResult";
        public static final String R_LIKEPRODUCT = "LikeProductResult";
    };

    public static final class Nodes {
        public static final String AppLog = "AppLog";
        public static final String Customer = "Customer";
        public static final String Category = "Category";
        public static final String Product = "Product";
        public static final String Client = "Client";
        public static final String Order = "Order";
        public static final String OrderItem = "OrderItem";
    };

    public static final class ClientStatus{
        public static final String Offline = "0";
        public static final String Online = "1";
        public static final String Background = "2";
    };

    public static final class OrderStatus{
        public static final String New = "1";
        public static final String Preparing = "2";
        public static final String OnTheTable = "3";
        public static final String AdditionalOrder = "4";
        public static final String Paid = "5";
    };

    public static final class OrderItemStatus{
        public static final String Selected = "0";
        public static final String New = "1";
        public static final String Preparing = "2";
        public static final String OnTheTable = "3";
    };


    public  static  final class LanguageCodeIndex{
        public static final int Tr = 0;
        public static final int En = 1;
        public static final int De = 2;
    }
    public  static  final String[] LanguageCodes = {"tr", "en", "de"};
}

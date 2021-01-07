package com.bonusCaluculator.REST.OpenCRX;

public class Test {
    private OpenCRXAPI openCRXAPI;
    public Test(){
        openCRXAPI = OpenCRXAPI.getInstance();
    }
    public boolean start(){
        try {
            openCRXAPI.getAllAccounts();
            openCRXAPI.getAllProducts();
            openCRXAPI.getProduct("9JMBMVTX2CSMHH2MA4T2TYJFL");
            openCRXAPI.getAccount("97NB4O91UQORTH2MA4T2TYJFL");
            openCRXAPI.getAllSalesOrders();
            openCRXAPI.getOrderPositions("9DTSXR06DLHPM0EBHQA5MAZ7J");
            System.out.println("OpenCRXAPI API test: no errors.");
            return true;
        }catch(Exception e){
            System.out.println("OpenCRXAPI API test: failed.");
            return false;
        }
    }
}

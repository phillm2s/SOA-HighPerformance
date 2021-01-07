package com.bonusCaluculator.REST.OpenCRX;

import com.bonusCaluculator.REST.RESTSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class OpenCRXAPI {
    private static OpenCRXAPI instance;
    private RESTSender sender;
    private String userName ="guest";
    private String password = "guest";
    private String baseUrl = "https://sepp-crm.inf.h-brs.de";

    private OpenCRXAPI(){ sender = new RESTSender(); }

    public static OpenCRXAPI getInstance(){
        if (OpenCRXAPI.instance==null)
            OpenCRXAPI.instance = new OpenCRXAPI();

        return OpenCRXAPI.instance;
    }

    public Map<String, Account> getAllAccounts() throws JSONException, JsonProcessingException {
        String url = baseUrl + "/opencrx-rest-CRX/org.opencrx.kernel.account1/provider/CRX/segment/Standard/account";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        Map<String, Account> accountMap= new HashMap<String,Account>();
        JSONObject accountList = new JSONObject(res.getBody());
        JSONArray ja = accountList.getJSONArray("objects");
        for (int i=0; i< ja.length();i++) {
            String s = ja.get(i).toString();
            Account account = Account.createFromJSON(s);
            accountMap.put(account.fullName, account);
        }
        System.out.println(accountMap.size() + " customers found.");
        return accountMap;
    }

    public Map<String, Product> getAllProducts() throws JSONException, JsonProcessingException {
        String url = baseUrl + "/opencrx-rest-CRX/org.opencrx.kernel.product1/provider/CRX/segment/Standard/product";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        Map<String, Product> productMap= new HashMap<String,Product>();
        JSONObject productList = new JSONObject(res.getBody());
        JSONArray ja = productList.getJSONArray("objects");
        for (int i=0; i< ja.length();i++) {
            String s = ja.get(i).toString();
            Product product = Product.createFromJSON(s);
            productMap.put(product.productNumber, product);
        }
        System.out.println(productMap.size() + " products found.");
        return productMap;
    }

    public Product getProduct(String productRef) throws JSONException, JsonProcessingException {
        String url = baseUrl + "/opencrx-rest-CRX/org.opencrx.kernel.product1/provider/CRX/segment/Standard/product/" + productRef;

        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);


        String s = res.getBody();
        Product product = Product.createFromJSON(s);

        return product;
    }

    public Account getAccount(String accountRef) throws JSONException, JsonProcessingException {
        String url = baseUrl + "/opencrx-rest-CRX/org.opencrx.kernel.account1/provider/CRX/segment/Standard/account/" +accountRef;
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName, password);
        header.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);


        String s = res.getBody();
        Account acc = Account.createFromJSON(s);

        return acc;
    }

    public List<SalesOrder> getAllSalesOrders() throws JSONException, JsonProcessingException {
        String url = baseUrl + "/opencrx-rest-CRX/org.opencrx.kernel.contract1/provider/CRX/segment/Standard/salesOrder";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        List<SalesOrder> salesOrderList= new ArrayList<SalesOrder>();
        JSONObject SalesOrderList = new JSONObject(res.getBody());
        JSONArray ja = SalesOrderList.getJSONArray("objects");
        for (int i=0; i< ja.length();i++) {
            String s = ja.get(i).toString();
            SalesOrder salesOrder = SalesOrder.createFromJSON(s);
            salesOrderList.add(salesOrder);
        }
        System.out.println(salesOrderList.size() + " products found.");
        return salesOrderList;
    }

    public List<Position> getOrderPositions(String salesOrderRef) throws JSONException, JsonProcessingException {
        String url = baseUrl + "/opencrx-rest-CRX/org.opencrx.kernel.contract1/provider/CRX/segment/Standard/salesOrder/" + salesOrderRef + "/position";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(userName,password);
        header.set("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity<String> res = sender.sendRequest(url, HttpMethod.GET, entity);

        List<Position> positionList= new ArrayList<Position>();
        JSONObject jo = new JSONObject(res.getBody());
        JSONArray ja = jo.getJSONArray("objects");
        for (int i=0; i< ja.length();i++) {
            String s = ja.get(i).toString();
            Position position = Position.createFromJSON(s);
            positionList.add(position);
        }
        System.out.println(positionList.size() + " customers found.");
        return positionList;
    }
}

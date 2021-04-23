package com.grophin.ticketingsystemutility.service;

import com.grophin.ticketingsystemutility.config.APIConfig;
import com.grophin.ticketingsystemutility.constans.Constants;
import javassist.bytecode.SyntheticAttribute;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Scanner;

@Service
public class TicketingUtilImpl implements TicketingUtilInterface {

    Scanner sc = new Scanner(System.in);
    @Autowired
    APIConfig apiConfig;

    @Override
    public void Register(String url) {
        try{
            System.out.println("Enter Registration Details.");
        System.out.println("Enter name:");
        String name = sc.next();
        System.out.println("Enter email:");
        String email = sc.next();
        System.out.println("Enter password:");
        String password = sc.next();
        System.out.println("Enter address:");
        String address = sc.next();
        System.out.println("Enter contactnumber:");
        String contact = sc.next();
        System.out.println("Enter id:");
        String id = sc.next();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.ID,id);
        jsonObject.put(Constants.NAME,name);
        jsonObject.put(Constants.EMAIL,email);
        jsonObject.put(Constants.PASSWORD,password);
        jsonObject.put(Constants.CONTACT,contact);
        jsonObject.put(Constants.ADDRESS,address);
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() == 200)
        System.out.println("Registered Successfully");
        else
            System.out.println("Failed to register.");
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }

    }

    @Override
    public void Login(String url,String type) {
        try{
            System.out.println("Enter the email:");
            String email = sc.next();
            System.out.println("Enter the password:");
            String password = sc.next();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url+email)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject res = new JSONObject(body);

            if(response.code() == 200){
                if(type.equals("agent")){
                    String agentEmail = (String) res.get(Constants.AGENTEMAIL);
                    String agentPassword = (String) res.get(Constants.AGENTPASSWORD);
                    if(email.equals(agentEmail) && password.equals(agentPassword)){
                        System.out.println("Agent Logged In");
                        agentChoice(res);
                    }
                }else{
                    String userEmail = (String) res.get(Constants.USEREMAIL);
                    String userPassword = (String) res.get(Constants.USERPASSWORD);
                    if(email.equals(userEmail) && password.equals(userPassword)){
                        System.out.println("User Logged In");
                        userChoice(res);
                    }
                }
            }

            System.out.println("Login failed----");
        }
            catch (Exception ex){
                System.out.println(ex.getLocalizedMessage());
        }
    }

    public void agentChoice(JSONObject jsonObject){
        System.out.println("Enter the choice to process.");
        System.out.println("1.Update Status\n2.List Tickets\n3.Filter Ticket By ID\n4.Filter Ticket By Status\n5.Filter Ticket By AgentId\n6.Filter Ticket By Customer\n7.Update details.\n8.Delete Ticket\n9.Update response.");
        int choice = sc.nextInt();
        String url = null;
        switch (choice){
            case 1:
                url= apiConfig.getTicket().getUpdate().getDetails().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid = sc.next();
                JSONObject obj = new JSONObject();
                obj.put("ticketId",ticketid);
                JSONObject obj1 = new JSONObject();


                    System.out.println("Enter the status:");
                    String status = sc.next();
                    obj1.put("status",status);

                obj.put("dataMap",obj1);
                updateDetails(url,obj.toString());
                break;
            case 2:
                url = apiConfig.getTicket().getFetch().getUrl();
                fetchTicketDetails(url);
                break;
            case 3:
                url=apiConfig.getTicket().getFetch().getId().getUrl();
                System.out.println("Enter the ticket id:");
                String id = sc.next();
                url = url+id;
                fetchTicketDetails(url);
                break;
            case 4:
                url=apiConfig.getTicket().getFetch().getStatus().getUrl();
                System.out.println("Enter the status:");
                String status1 = sc.next();
                url = url+status1;
                fetchTicketDetails(url);
                break;
            case 5:
                url=apiConfig.getTicket().getFetch().getAgent().getUrl();
                System.out.println("Enter the agent id:");
                String agentid = sc.next();
                url = url+agentid;
                fetchTicketDetails(url);
                break;
            case 6:
                url=apiConfig.getTicket().getFetch().getCustomer().getUrl();
                System.out.println("Enter the customer:");
                String cust = sc.next();
                url = url+cust;
                fetchTicketDetails(url);
                break;
            case 7:
                url= apiConfig.getTicket().getUpdate().getDetails().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid1 = sc.next();
                JSONObject obj2 = new JSONObject();
                obj2.put("ticketId",ticketid1);
                JSONObject obj3 = new JSONObject();
                do{
                    System.out.println("Enter the key for filed:");
                    String key = sc.next();
                    System.out.println("Enter the value for filed:");
                    String value = sc.next();
                    obj3.put(key,value);
                    System.out.println("Do You want to continue?");
                }while(sc.next().equalsIgnoreCase("y"));

                obj2.put("dataMap",obj3);
                updateDetails(url,obj2.toString());
                break;
            case 8:
                url=" http://localhost:8881/api/v1/tickets/deleteticket/";
                System.out.println("Enter the ticket id:");
                String ticketid3 = sc.next();
                deleteTicket(url,ticketid3);
                break;
            case 9:
                url= apiConfig.getTicket().getUpdate().getDetails().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid4 = sc.next();
                JSONObject obj4 = new JSONObject();
                obj4.put("ticketId",ticketid4);
                JSONObject obj5 = new JSONObject();


                System.out.println("Enter the status:");
                String status2 = sc.next();
                obj5.put("status",status2);

                obj4.put("dataMap",obj5);
                updateDetails(url,obj4.toString());
                break;
            default:
                System.out.println("Invalid Input");
                break;


        }
    }

    public void userChoice(JSONObject jsonObject){
        System.out.println("Enter the choice to process.");
        System.out.println("1.Create Ticket\n2.List Tickets\n3.Filter Ticket By ID\n4.Filter Ticket By Status\n5.Filter Ticket By AgentId\n6.Filter Ticket By Customer\n7.Update details.\n8.Assign agent.");
        int choice = sc.nextInt();
        String url = null;
        switch (choice){
            case 1:
                url = apiConfig.getTicket().getAdd().getUrl();
                createTicket(url,jsonObject);
                break;
            case 2:
                url = apiConfig.getTicket().getFetch().getUrl();
                fetchTicketDetails(url);
                break;
            case 3:
                url=apiConfig.getTicket().getFetch().getId().getUrl();
                System.out.println("Enter the ticket id:");
                String id = sc.next();
                url = url+id;
                fetchTicketDetails(url);
                break;
            case 4:
                url=apiConfig.getTicket().getFetch().getStatus().getUrl();
                System.out.println("Enter the status:");
                String status = sc.next();
                url = url+status;
                fetchTicketDetails(url);
                break;
            case 5:
                url=apiConfig.getTicket().getFetch().getAgent().getUrl();
                System.out.println("Enter the agent id:");
                String agentid = sc.next();
                url = url+agentid;
                fetchTicketDetails(url);
                break;
            case 6:
                url=apiConfig.getTicket().getFetch().getCustomer().getUrl();
                System.out.println("Enter the customer:");
                String cust = sc.next();
                url = url+cust;
                fetchTicketDetails(url);
                break;
            case 7:
                url= apiConfig.getTicket().getUpdate().getDetails().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid = sc.next();
                JSONObject obj = new JSONObject();
                obj.put("ticketId",ticketid);
                JSONObject obj1 = new JSONObject();
                do{
                    System.out.println("Enter the key for filed:");
                    String key = sc.next();
                    System.out.println("Enter the value for filed:");
                    String value = sc.next();
                    obj1.put(key,value);
                    System.out.println("Do You want to continue?");
                }while(sc.next().equalsIgnoreCase("y"));

                obj.put("dataMap",obj1);
                updateDetails(url,obj.toString());
                break;
            case 8:
                url = apiConfig.getAgent().getAssign().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid1 = sc.next();
                System.out.println("Enter the agent id:");
                String agentId = sc.next();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("ticketId",ticketid1);
                jsonObject1.put("agentId",agentId);
                updateDetails(url,jsonObject1.toString());
                break;

            default:
                System.out.println("Invalid Input");



        }
    }

    public void createTicket(String url,JSONObject res){

        try{
            System.out.println("Enter Ticket Details.");
            System.out.println("Enter id:");
            String id = sc.next();
            System.out.println("Enter type:");
            String type = sc.next();
            String user = res.getString(Constants.USERNAME);
            System.out.println("Enter desciption:");
            String desc = sc.next();
            System.out.println("Enter priority:");
            String priority = sc.next();
            System.out.println("Enter customer:");
            String customer = sc.next();
            System.out.println("Enter deadline:");
            String deadline = sc.next();
            System.out.println("Enter Title:");
            String title = sc.next();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ID,id);
            jsonObject.put(Constants.CUSTOMER,customer);
            jsonObject.put(Constants.PRIORITY,priority);
            jsonObject.put(Constants.DEADLINE,deadline);
            jsonObject.put(Constants.TITLE,title);
            jsonObject.put(Constants.DESCRIPTION,desc);
            jsonObject.put(Constants.TYPE,type);
            jsonObject.put(Constants.USER,user);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            if(response.code() == 200){
                System.out.println("Ticket Created Successfully!!!");
            }
            else{
                System.out.println("Failed to create Ticket!!!");
            }
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void fetchTicketDetails(String url){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            System.out.println(body);
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void updateDetails(String url, String requestString){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestString);
            Request request = new Request.Builder()
                    .url(url)
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if(response.code() == 200){
                System.out.println("Successfullly updated");
            }
            else{
                System.out.println("Failed to update");
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void updateResponse(String url, String requestString){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestString);
            Request request = new Request.Builder()
                    .url(url)
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if(response.code() == 200){
                System.out.println("Successfullly updated");
            }
            else{
                System.out.println("Failed to update");
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void sendEmail(String url, String requestString){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestString);
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if(response.code() == 200)
            System.out.println("Email sent");

        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void deleteTicket(String url, String ticketId){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(url+ticketId)
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if(response.code() == 200){
                System.out.println("Successfully deleted");
            }
            else{
                System.out.println("Not Able to delete.");
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }
}

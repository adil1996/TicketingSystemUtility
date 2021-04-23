package com.grophin.ticketingsystemutility.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grophin.ticketingsystemutility.config.APIConfig;
import com.grophin.ticketingsystemutility.constans.Constants;
import javassist.bytecode.SyntheticAttribute;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Service
public class TicketingUtilImpl implements TicketingUtilInterface {

    Scanner sc = new Scanner(System.in);
    @Autowired
    APIConfig apiConfig;

    @Override
    public void Register(String url) {
        try{
            String sc1= sc.nextLine();
        System.out.println("Enter Registration Details.");
        System.out.println("Enter name:");
        String name = sc.nextLine();
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();
        System.out.println("Enter address:");
        String address = sc.nextLine();
        System.out.println("Enter contactnumber:");
        String contact = sc.nextLine();
        System.out.println("Enter id:");
        String id = sc.nextLine();

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
            System.out.println("Failed to register."+response.body().string());
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
                    else{
                        System.out.println("Login failed----");
                    }
                }else{
                    String userEmail = (String) res.get(Constants.USEREMAIL);
                    String userPassword = (String) res.get(Constants.USERPASSWORD);
                    if(email.equals(userEmail) && password.equals(userPassword)){
                        System.out.println("User Logged In");
                        userChoice(res);
                    }else {
                        System.out.println("Login failed----");
                    }
                }
            }else{
            System.out.println("Login failed----"+response.body().string());
            }
        }
            catch (Exception ex){
                System.out.println(ex.getLocalizedMessage());
        }
    }

    public void agentChoice(JSONObject jsonObject){

        do{
        System.out.println("Enter the choice to process.");
        System.out.println("1.Update Status\n2.List Tickets\n3.Filter Ticket By ID\n4.Filter Ticket By Status\n5.Filter Ticket By AgentId\n6.Filter Ticket By Customer\n7.Update details.\n8.Delete Ticket\n9.Update response.");
        int choice = sc.nextInt();
        String url = null;

        switch (choice){
            case 1:
                url= apiConfig.getTicket().getUpdate().getStatus().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid = sc.next();
                JSONObject obj = new JSONObject();
                obj.put("ticketId",ticketid);
                JSONObject obj1 = new JSONObject();


                    System.out.println("Enter the status:");
                    String status = sc.nextLine();
                    status = sc.nextLine();
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
                String abc3 = sc.nextLine();
                url=apiConfig.getTicket().getFetch().getStatus().getUrl();
                System.out.println("Enter the status:");
                String status1 = sc.nextLine();
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
                String abc1 = sc.nextLine();
                url=apiConfig.getTicket().getFetch().getCustomer().getUrl();
                System.out.println("Enter the customer:");
                String cust = sc.nextLine();
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
                    System.out.println("Enter the key for field:");
                    String key = sc.nextLine();
                    key = sc.nextLine();
                    System.out.println("Enter the value for field:");
                    String value = sc.nextLine();
                    obj3.put(key,value);
                    System.out.println("Do You want to continue?");
                }while(sc.nextLine().equalsIgnoreCase("y"));

                obj2.put("dataMap",obj3);
                updateDetails(url,obj2.toString());
                break;
            case 8:
                url="http://localhost:8881/api/v1/tickets/deleteticket/";
                System.out.println("Enter the ticket id:");
                String ticketid3 = sc.next();
                deleteTicket(url,ticketid3);
                break;
            case 9:
                String ab = sc.nextLine();
                url= "http://localhost:8881/api/v1/tickets/updateresponse";
                System.out.println("Enter the ticket id:");
                String ticketid4 = sc.next();
                JSONObject obj4 = new JSONObject();
                obj4.put("ticketId",ticketid4);
                JSONObject obj5 = new JSONObject();


                System.out.println("Enter the response:");
                String status2 = sc.nextLine();
                status2 = sc.nextLine();
                obj5.put("response",status2);

                obj4.put("dataMap",obj5);
                updateResponse(url,obj4.toString());
                break;
            default:
                System.out.println("Invalid Input");
                break;


        }
            System.out.println("Do you want to continue?");}while (sc.next().equalsIgnoreCase("y"));
    }

    public void userChoice(JSONObject jsonObject){
        String url = null;
        do{
            System.out.println("Enter the choice to process.");
            System.out.println("1.Create Ticket\n2.List Tickets\n3.Filter Ticket By ID\n4.Filter Ticket By Status\n5.Filter Ticket By AgentId\n6.Filter Ticket By Customer\n7.Update details.\n8.Assign agent.");
            int choice = sc.nextInt();
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
                String cust = sc.nextLine();
                cust = sc.nextLine();
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
                    System.out.println("Enter the key for field:");
                    String key = sc.nextLine();
                    key = sc.nextLine();
                    System.out.println("Enter the value for field:");
                    String value = sc.nextLine();
                    obj1.put(key,value);
                    System.out.println("Do You want to continue?");
                }while(sc.nextLine().equalsIgnoreCase("y"));

                obj.put("dataMap",obj1);
                updateDetails(url,obj.toString());
                break;
            case 8:
                url = apiConfig.getAgent().getAssign().getUrl();
                System.out.println("Enter the ticket id:");
                String ticketid1 = sc.next();
                try{
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://localhost:8881/api/v1/agents/getAgents")
                        .method("GET", null)
                        .build();
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<String> listAgent = objectMapper.readValue(body,List.class);
                    System.out.println("Enter the agent id from the available List: "+listAgent);
                    String agentId = sc.next();
                    if(listAgent.contains(agentId)){
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("ticketId",ticketid1);
                    jsonObject1.put("agentId",agentId);
                    updateDetails(url,jsonObject1.toString());}
                    else{
                        System.out.println("Entered Wrong agentId.");
                    }

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }


                break;

            default:
                System.out.println("Invalid Input");
                break;
        }
                System.out.println("Do you want to Continue?");
        }while (sc.next().equalsIgnoreCase("y"));
    }

    public void createTicket(String url,JSONObject res){

        try{
            System.out.println("Enter Ticket Details.");
            String user = res.getString(Constants.USERNAME);
            System.out.println("Enter id:");
            String id = sc.next();

            System.out.println("Enter type:");
            String type = sc.nextLine();
             type = sc.nextLine();

            System.out.println("Enter desciption:");
            String desc = sc.nextLine();

            System.out.println("Enter priority:");
            String priority = sc.nextLine();

            System.out.println("Enter customer:");
            String customer = sc.nextLine();

            System.out.println("Enter deadline:");
            String deadline = sc.nextLine();

            System.out.println("Enter Title:");
            String title = sc.nextLine();

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
                System.out.println("Enter from address:");
                String from = sc.next();

                System.out.println("Enter to address:");
                String to = sc.nextLine();
                to = sc.nextLine();

                System.out.println("Enter content:");
                String content = sc.nextLine();

                System.out.println("Enter subject:");
                String subject = sc.nextLine();

                JSONObject emailObj = new JSONObject();
                emailObj.put("to",to);
                emailObj.put("from",from);
                emailObj.put("content",content);
                emailObj.put("subject",subject);
                emailObj.put("apiKey",apiConfig.getEmail().getKey());
                sendEmail(apiConfig.getEmail().getUrl(),emailObj.toString());

            }
            else{
                System.out.println("Failed to update"+response.body().string());
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
            else
                System.out.println("failed o send"+response.body().string());

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

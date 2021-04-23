package com.grophin.ticketingsystemutility.util;

import com.grophin.ticketingsystemutility.config.APIConfig;
import com.grophin.ticketingsystemutility.service.TicketingUtilInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ProcessRequest implements ProcessRequestInterface{

    @Autowired
    TicketingUtilInterface ticketingUtilInterface;

    @Autowired
    APIConfig apiConfig;

    @Override
    public void processAgent() {
        System.out.println("Enter the choice for the agent.");
        System.out.println("1.Register\n2.Login");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                ticketingUtilInterface.Register(apiConfig.getRegister().getAgent().getUrl());
               break;
            case 2:
                ticketingUtilInterface.Login(apiConfig.getFetch().getAgent().getUrl(),"agent");
                break;
            default:
                System.out.println("Invalid input.");
                break;
        }
    }

    @Override
    public void processUser() {
        System.out.println("Enter the choice for the User.");
        System.out.println("1.Register\n2.Login");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                ticketingUtilInterface.Register(apiConfig.getRegister().getUser().getUrl());
                break;
            case 2:
                ticketingUtilInterface.Login(apiConfig.getFetch().getUser().getUrl(),"user");
                break;
            default:
                System.out.println("Invalid input.");
                break;
        }
    }
}

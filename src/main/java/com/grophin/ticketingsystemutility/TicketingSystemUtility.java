package com.grophin.ticketingsystemutility;

import com.grophin.ticketingsystemutility.config.APIConfig;
import com.grophin.ticketingsystemutility.util.ProcessRequestInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

@SpringBootApplication
@EnableConfigurationProperties({APIConfig.class})
public class TicketingSystemUtility {
    private static final Logger logger = LogManager.getLogger(TicketingSystemUtility.class);

    public static ProcessRequestInterface processRequestInterface;
    public static APIConfig apiConfig;

    @Autowired
    public TicketingSystemUtility(ProcessRequestInterface processRequestInterface,APIConfig apiConfig) {
        TicketingSystemUtility.processRequestInterface = processRequestInterface;
        TicketingSystemUtility.apiConfig = apiConfig;
    }

    public TicketingSystemUtility(){

    }


    public static void main(String [] args){
        try {
            logger.info("************************ Starting the Ticketing System Utility ************************");
            SpringApplication.run(TicketingSystemUtility.class,args);
            TicketingSystemUtility ticketingSystemUtility = new TicketingSystemUtility();
            ticketingSystemUtility.processRequest();
            logger.info("************************ Ended the Ticketing System Utility ************************");
            System.exit(0);
        }
        catch (Exception ex){
            StringWriter stringWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(stringWriter));
            logger.info("Exception while starting the ticketing System service\n"+stringWriter);
        }
    }

    public void processRequest(){
        System.out.println("******************* Ticketing System *******************");

        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("Enter your choice of User.\n1.Agent\n2.User");
            int choice = scanner.nextInt();
        switch (choice){
            case 1:
                processRequestInterface.processAgent();
                break;
            case 2:
                processRequestInterface.processUser();
                break;
            default:
                System.out.println("Invalid Choice");
        }
        System.out.println("Do you want to continue?");
        } while(scanner.next().equalsIgnoreCase("y"));


    }
}

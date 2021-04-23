package com.grophin.ticketingsystemutility.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("api")
public class APIConfig {

    Register register = new Register();
    Fetch fetch =  new Fetch();
    Ticket ticket =  new Ticket();
    Agent agent = new Agent();
    Email email = new Email();

    @Data
    public static class Register{

        User user = new User();
        Agent agent = new Agent();

        @Data
        public static class User{
            private String url;
        }

        @Data
        public static class Agent{
            private String url;
        }

    }

    @Data
    public static class Fetch{
        User user = new User();
        Agent agent = new Agent();

        @Data
        public static class User{
            private String url;
        }

        @Data
        public static class Agent{
            private String url;
        }

    }

    @Data
    public static class Ticket{
        Add add = new Add();
        Fetch fetch = new Fetch();
        Update update = new Update();

        @Data
        public static class Add{
            private String url;
        }

        @Data
        public static class Fetch{

            private String url;
            Id id = new Id();
            Customer customer = new Customer();
            Status status = new Status();
            Agent agent = new Agent();

            @Data
            public static class Id{
                private String url;
            }

            @Data
            public static class Customer{
                private String url;
            }

            @Data
            public static class Status{
                private String url;
            }

            @Data
            public static class Agent{
                private String url;
            }


        }

        @Data
        public static class Update{
            Details details = new Details();
            Status status= new Status();

            @Data
            public static class Details{
                private String url;
            }

            @Data
            public static class Status{
                private String url;
            }
        }
    }

    @Data
    public static class Agent{
        Assign assign = new Assign();
        @Data
        public static class Assign{
            private String url;
        }
    }

    @Data
    public static class Email{
        private String url;
        private String key;

    }
}

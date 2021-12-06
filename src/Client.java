import chainofresponsibility.EssentialHandler;
import chainofresponsibility.LuxuryHandler;
import chainofresponsibility.MiscHandler;
import chainofresponsibility.OrderHandler;
import database.Inventory;
import order.Card;
import order.Item;
import order.Order;
import order.ParticularOrder;

import java.io.*;
import java.util.*;

public class Client {

    // Starting the code from here
    public static void main(String [] args) throws Exception {
        Client cObj = new Client();

        // populating the database (cards and items)
        cObj.populateInventory();
        cObj.populateCards();

        // get order.Order
        Scanner sc = new Scanner(System.in); // creating object of Scanner class
        System.out.println("Enter Order plus comma separated file i/p");

        String ch = "";
        while(true){
            System.out.print("Make your choice: ");
            ch = sc.nextLine(); // reading user's choice

//            System.out.println(ch);
            // If the user enters the order
            if(ch.startsWith("Order")){

                // reading the file
                String filePath = ch.split(",")[1];
                File file = new File(filePath);
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                line = br.readLine();
                boolean first = true;
                Order obj = new Order();

                while(line != null)
                {
                    String [] lines = line.split(",");
                    if (first){
                        first = false;
                        line = br.readLine();
                        continue;
                    }

                    // Removing the extra ""
                    String Item = lines[0].replace("\"","");
                    int quantity = Integer.valueOf(lines[1].replace("\"",""));
                    String cardNumber = lines[2].replace("\"","");

                    // Set orders in the order object
                    ParticularOrder pOrderObj = new ParticularOrder(Item,quantity,cardNumber);
                    List<ParticularOrder> tempList = obj.getOrders();
                    if(tempList == null)tempList = new ArrayList<>();
                    tempList.add(pOrderObj);
                    obj.setOrders(tempList);
                    line = br.readLine();
                }

                // Setting the chain of responsibilities
                OrderHandler h1 = cObj.setChainOfResponsibilites();

                // Handling the chain of responsibilities
                h1.handleOrder(obj);
            }
            else{
                break;
            }
        }

        System.out.println("Thank you for using Spartan database.Inventory Management System. Please do visit again");

    }

    public void populateInventory() throws IOException {
        // reading the dataset csv file
        String filePath = "data/Dataset.csv";
        Inventory inventoryObj = Inventory.getInstance();
        HashSet<Item> presentItems = inventoryObj.getItems();
        HashMap<String,String> presentCategoryMap = inventoryObj.getCategoryMap();
        HashMap<String,Integer> presentCategoryCountMap = inventoryObj.getCategorCountMap();
        HashMap<String,Integer> presentIndividualItemMap = inventoryObj.getIndividualItemMap();
        HashMap<String,Float> presentPricePerQuantityMap = inventoryObj.getPricePerCategoryMap();
        if(presentCategoryMap == null)presentCategoryMap = new HashMap<>();
        if(presentItems == null)presentItems = new HashSet<>();
        if(presentCategoryCountMap == null)presentCategoryCountMap = new HashMap<>();
        if(presentIndividualItemMap == null)presentIndividualItemMap = new HashMap<>();
        if(presentPricePerQuantityMap == null)presentPricePerQuantityMap = new HashMap<>();

        //read the file, line by line from txt
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        line = br.readLine();
        while(line != null)
        {
            String [] lines = line.split(",");
            if (lines[0].equals("Category")){
                line = br.readLine();
                continue;
            }
            Item itemObj = new Item(lines[0],lines[1],Double.valueOf(lines[2]),Double.valueOf(lines[3]));
            presentCategoryMap.put(lines[1],lines[0]);
            presentItems.add(itemObj);
            presentPricePerQuantityMap.put(lines[1],Float.valueOf(lines[3]));
            presentCategoryCountMap.put(lines[0],presentCategoryCountMap.getOrDefault(lines[0],0)+Integer.valueOf(lines[2]));
            presentIndividualItemMap.put(lines[1],presentCategoryCountMap.getOrDefault(lines[1],0)+Integer.valueOf(lines[2]));
            line = br.readLine();
        }
        inventoryObj.setItems(presentItems);
        inventoryObj.setCategoryMap(presentCategoryMap);
        inventoryObj.setCategorCountMap(presentCategoryCountMap);
        inventoryObj.setIndividualCategoryCountMap(presentIndividualItemMap);
        inventoryObj.setPricePerCategoryMap(presentPricePerQuantityMap);
        fr.close();
    }

    public void populateCards() throws IOException {
        // reading the dataset csv file
        String filePath = "data/Cards.csv";
        Inventory inventoryObj = Inventory.getInstance();
        HashSet<Card> presentCards = inventoryObj.getCards();
        if(presentCards == null)presentCards = new HashSet<>();

        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        line = br.readLine();
        while(line != null)
        {
            String [] lines = line.split(",");
            if (lines[0].equals("CardNumber")){
                line = br.readLine();
                continue;
            }
            Card cardObj = new Card((lines[0]));
            presentCards.add(cardObj);
            line = br.readLine();
        }
        inventoryObj.setCards(presentCards);
        fr.close();
    }

    public OrderHandler setChainOfResponsibilites() {
        OrderHandler h1  = new LuxuryHandler();
        OrderHandler h2  = new EssentialHandler();
        OrderHandler h3  = new MiscHandler();

        h1.setNext(h2);
        h2.setNext(h3);

        return h1;
    }
}


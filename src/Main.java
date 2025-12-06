public class Main {
    public static void main(String[] args) {
         RentalManager takis=new RentalManager();
         takis.readCSV();
         takis.printCars();

         ClientManager sakis=new ClientManager();
         sakis.readCSV();
         sakis.printClients();

         Car car=new Car("ΙΚΥ1234","Porche","Macan","Corolla","2019","Ασημί","Διαθέσιμο");

        System.out.println("hello");

    }
}

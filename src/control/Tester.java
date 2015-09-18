package control;

import entity.Customer;
import entity.Employee;
import entity.Orders;
import facade.FacadeControl;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * @author Tobias Jacobsen
 */
public class Tester {

    public static void main(String[] args) {
        //######## Remember to re-run script!! ##############

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("classicmodelsPU");
        EntityManager em = emf.createEntityManager();
        FacadeControl control = new Control();

        //create employees
        control.createEmploye("Tobias", "Jacobsen", "x6945", "tobias.cbs@gmail.com", "CEO");
        control.createEmploye("Niels", "Nielsen", "x4681", "niels@gmail.com", "Dishwasher");
        control.createEmploye("Lars", "Larsen", "x2654", "lars@gmail.com", "CFO");
        TypedQuery<Employee> query1 = em.createNamedQuery("Employee.findByFirstName", Employee.class).setParameter("firstName", "Tobias");
        TypedQuery<Employee> query2 = em.createNamedQuery("Employee.findByFirstName", Employee.class).setParameter("firstName", "Niels");
        TypedQuery<Employee> query3 = em.createNamedQuery("Employee.findByFirstName", Employee.class).setParameter("firstName", "Lars");
        Employee e1 = query1.getSingleResult();
        Employee e2 = query2.getSingleResult();
        Employee e3 = query3.getSingleResult();
        System.out.println("First name is: " + e1.getFirstName());
        System.out.println("First name is: " + e2.getFirstName());
        System.out.println("First name is: " + e3.getFirstName());

        //update customer
        TypedQuery<Customer> query4 = em.createNamedQuery("Customer.findByCustomerName", Customer.class).setParameter("customerName", "Atelier graphique");
        Customer c1 = query4.getSingleResult();
        System.out.println("Old Customer name is: " + c1.getCustomerName());
        c1.setCustomerName("Hansemand");
        control.updateCustomer(c1);
        TypedQuery<Customer> query5 = em.createNamedQuery("Customer.findByCustomerName", Customer.class).setParameter("customerName", "Hansemand");
        Customer c2 = query4.getSingleResult();
        System.out.println("New Customer name is: " + c2.getCustomerName());
        
        //get employee count
        long employeeCount = control.getEmployeCount();
        System.out.println("Employee count is: " + employeeCount);
        
        //get customer in city
        List<Customer> customerList = control.getCustomerInCity("Singapore");
        for (Customer c : customerList) {
            System.out.println(c.getCustomerName() + " lives in Singapore");
        }
        
        //Return the employees with most customers
        List<Employee> employeeList = control.getEmployeMaxCustomers();
        for (Employee e : employeeList) {
            System.out.println(e.getFirstName() + " " + e.getLastName() + " has the highest number of customers");
        }
        
        //Return all orders where status is "On Hold"
        List<Orders> orderList1 = control.getOrdersOnHold();
        for (Orders o : orderList1) {
            System.out.println(o.getOrderNumber() + " has status: " + o.getStatus());
        }
        
        //Return all orders on hold for a given customer (try Customer 144)
        List<Orders> orderList2 = control.getOrdersOnHold(144);
        boolean hasFound = false;
        for (Orders o : orderList2) {
            if (o.getStatus().equals("On Hold")) {
                System.out.println("Customer 144 has order: " + o.getOrderNumber() + " " + o.getStatus() );
                hasFound = true;
            } 
        }
        if (!hasFound) {
            System.out.println("This customer has no orders on hold");
        }
    }
}

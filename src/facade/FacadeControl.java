package facade;

import entity.Customer;
import entity.Employee;
import entity.Orders;
import java.util.List;

/**
 * @author Tobias Jacobsen
 */
public interface FacadeControl {
    //create-edit

    //Just manually query the database, before you execute the statement to get the next free employee number. 
    //A new employee must have an office, assign the office with officeCode = "1" to all new Employees
    public Employee createEmploye(String firstName, String lastName, String extension, String email, String jobTitle);

    //Updates and returns the updated Customer
    public Customer updateCustomer(Customer cust);

    //Querying
    /**
     * Return total employees
     *
     * @return int
     */
    public long getEmployeCount();

    /**
     * Return all customers living in a given city (Barcelona = 1)
     *
     * @param city
     * @return
     */
    public List<Customer> getCustomerInCity(String city);

    /**
     * Return the employees with most customers
     *
     * @return
     */
    public List<Employee> getEmployeMaxCustomers();

    /**
     * Return all orders where status is "On Hold"
     *
     * @return
     */
    public List<Orders> getOrdersOnHold();

    /**
     * Return all orders on hold for a given customer (try Customer 144)
     *
     * @param customerNumber
     * @return
     */
    public List<Orders> getOrdersOnHold(int customerNumber);

}

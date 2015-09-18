package control;

import entity.Customer;
import entity.Employee;
import entity.Office;
import entity.Orders;
import facade.FacadeControl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

/**
 * @author Tobias Jacobsen
 */
public class Control implements FacadeControl {

    EntityManagerFactory emf;
    EntityManager em;

    public Control() {
        emf = Persistence.createEntityManagerFactory("classicmodelsPU");
        em = emf.createEntityManager();
    }

    @Override
    public Employee createEmploye(String firstName, String lastName, String extension, String email, String jobTitle) {
        /*
        A little slow with many employees. Thought it was a fun implementation for this particular dataset,
        because it contains so many "holes" in the numbers.
        Alternatively find the max employeeNumber + 1.
        */
        
        Office office = em.find(Office.class, "1");
        Employee reportsTo = em.find(Employee.class, 1002);

        int id = 1000;
        while (true) {
            TypedQuery<Employee> query = em.createNamedQuery("Employee.findByEmployeeNumber", Employee.class).setParameter("employeeNumber", id);
            try {
                Employee e = query.getSingleResult();
            } catch (NoResultException e) {
                break;
            }
            id++;
        }
        System.out.println("Id is: " + id);

        Employee emp = new Employee();
        emp.setEmployeeNumber(id);
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setExtension(extension);
        emp.setEmail(email);
        emp.setJobTitle(jobTitle);
        emp.setOffice(office);
        emp.setEmployee(reportsTo);
        em.getTransaction().begin();
        em.persist(emp);
        em.getTransaction().commit();
        return emp;
    }

    @Override
    public Customer updateCustomer(Customer cust) {
        Customer c = em.find(Customer.class, cust.getCustomerNumber());
        if (c != null) {
            em.getTransaction().begin();
            c = cust;
            em.getTransaction().commit();
        }
        return c;
    }

    @Override
    public long getEmployeCount() {
        Query q = em.createQuery("SELECT count(x) FROM Employee x");
        long count = (long) q.getSingleResult();
        return count;
    }

    @Override
    public List<Customer> getCustomerInCity(String city) {
        TypedQuery<Customer> query = em.createNamedQuery("Customer.findByCity", Customer.class).setParameter("city", city);
        List<Customer> list = query.getResultList();
        return list;
    }

    @Override
    public List<Employee> getEmployeMaxCustomers() {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.findAll", Employee.class);
        Collection<Employee> results = query.getResultList();
        List<Employee> maxList = new ArrayList();
        int maxSize = 0;
        for (Employee e : results) {
            int size = e.getCustomerCollection().size();
            if (size == maxSize) {
                maxList.add(e);
                maxSize = size;
            } else if (size > maxSize) {
                maxList.clear();
                maxList.add(e);
                maxSize = size;
            }
        }
        return maxList;
    }

    @Override
    public List<Orders> getOrdersOnHold() {
        TypedQuery<Orders> query = em.createNamedQuery("Orders.findByStatus", Orders.class).setParameter("status", "On Hold");
        List<Orders> list = query.getResultList();
        return list;
    }

    @Override
    public List<Orders> getOrdersOnHold(int customerNumber) {
        TypedQuery<Customer> query = em.createNamedQuery("Customer.findByCustomerNumber", Customer.class).setParameter("customerNumber", customerNumber);
        Customer c = query.getSingleResult();
        Collection<Orders> collection = c.getOrdersCollection();
        List<Orders> list = new ArrayList();
        for (Orders order : collection) {
            if (order.getStatus().equals("On Hold")) {
                list.add(order);
            }
        }
        return list;
    }
}

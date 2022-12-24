import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.*;


public class Main {
    public static void main(String[] args) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

        String hql = " from " + PurchaseList.class.getSimpleName();
        List<PurchaseList> purchaseLists = session.createQuery(hql).getResultList();

        Map<String, Integer> students = new HashMap<>();
        Map<String, Integer> courses = new HashMap<>();

        int student = 0;
        int course = 0;

        for (PurchaseList purchaseList : purchaseLists) {

            LinkedPurchaseList list = new LinkedPurchaseList();
            session.beginTransaction();

            if (students.containsKey(purchaseList.getStudentName()) && courses.containsKey(purchaseList.getCourseName())) {
                list.setId(new KeyLinkedPurchaseList(students.get(purchaseList.getStudentName()), courses.get(purchaseList.getCourseName())));
                list.setStudentId(students.get(purchaseList.getStudentName()));
                list.setCourseId(courses.get(purchaseList.getCourseName()));
            } else if (students.containsKey(purchaseList.getStudentName())) {
                course++;
                courses.put(purchaseList.getCourseName(), course);
                list.setId(new KeyLinkedPurchaseList(students.get(purchaseList.getStudentName()), course));
                list.setStudentId(students.get(purchaseList.getStudentName()));
                list.setCourseId(course);
            } else if (courses.containsKey(purchaseList.getCourseName())) {
                student++;
                students.put(purchaseList.getStudentName(), student);
                list.setId(new KeyLinkedPurchaseList(student, courses.get(purchaseList.getCourseName())));
                list.setStudentId(student);
                list.setCourseId(courses.get(purchaseList.getCourseName()));
            } else {
                course++;
                student++;
                courses.put(purchaseList.getCourseName(), course);
                students.put(purchaseList.getStudentName(), student);
                list.setId(new KeyLinkedPurchaseList(students.get(purchaseList.getStudentName()), courses.get(purchaseList.getCourseName())));
                list.setStudentId(students.get(purchaseList.getStudentName()));
                list.setCourseId(courses.get(purchaseList.getCourseName()));
            }
            session.saveOrUpdate(list);
            session.getTransaction().commit();
            System.out.println(list);
        }
        session.close();
        sessionFactory.close();
    }


}
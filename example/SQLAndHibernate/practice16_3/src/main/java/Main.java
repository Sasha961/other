import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

        Courses courses = session.get(Courses.class, 1);
        System.out.println(courses.getName() + " - " + courses.getTeacher().getName());

        Teachers teachers = session.get(Teachers.class, 2);
        List<Courses> teachersId = teachers.getCoursesList();
        System.out.println(teachers.getName() + ":");
        teachersId.stream().map(t -> t.getName()).forEach(System.out::println);

        Teachers teachers1 = session.get(Teachers.class, 10);
        List<Courses> teachersId1 = teachers1.getCoursesList();
        System.out.println(teachers1.getName() + ":");
        teachersId1.stream().map(t -> t.getName()).forEach(System.out::println);

        Students students = session.get(Students.class, 3);
        List<Subscriptions> studentCourses = students.getStudentCourses();
        studentCourses.stream().map(sc -> sc.getCourses().getId()).forEach(System.out::println);


        Subscriptions subscriptionsStudent = session.get(Subscriptions.class, new Key(3, 2));
        System.out.println(subscriptionsStudent.getStudents().getName());

        Subscriptions subscriptionsCourse = session.get(Subscriptions.class, new Key(3, 2));
        System.out.println(subscriptionsCourse.getCourses().getName());


        Courses Courses = session.get(Courses.class, 4);
        List<Subscriptions> coursesList = Courses.getSubscriptionCourse();
        coursesList.stream().map(c -> c.getStudentId()).forEach(System.out::println);

        sessionFactory.close();
    }
}
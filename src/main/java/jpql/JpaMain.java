package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(20);
            em.persist(member);

            // 반환 타입 명확
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // 반환 타입 명확 X
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            // 결과가 하나 이상일 때
            List<Member> resultList = query1.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.id = 20", Member.class);

            // 결과가 하나일 때
            String singleResult = query2.getSingleResult();
            System.out.println("singleResult = " + singleResult);

            // 파라미터 바인딩
            TypedQuery<Member> query5 = em.createQuery("select m from Member m where m.username = :username", Member.class);
            query5.setParameter("username", "member1");

            Member singleResult1 = query5.getSingleResult();
            System.out.println("singleResult1 = " + singleResult1.getUsername());

            tx.commit();
        } catch (
                Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}